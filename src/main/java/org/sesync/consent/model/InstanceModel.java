/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.sesync.consent.entities.InstanceConfig;
import org.sesync.consent.entities.ProjectApproval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * https://click.apache.org/docs/velocity/developer-guide.html
 * https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html
 *
 * @author National Socio-Environmental Synthesis Center
 */
public class InstanceModel {
    private static final Logger LOG = LoggerFactory.getLogger(InstanceModel.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static final String dataFileName = "data.json";
    public static final String contactsFileName = "contacts.csv";
    public static final String emailTemplateFileName = "template.txt";
    public static final String configFileName = "config.json";
    // headers to look for in csv file
    private static final String EMAIL_HDR = "email";
    private static final String PROJECT_HDR = "project";
    private static final String NAME_HDR = "name";
    private static final String SITE_HDR = "site";

    private final File dir;
    private File dataFile;
    private InstanceConfig config;
    private List<ProjectApproval> approvals;
    private MailService mailService;
    
    private InstanceModel(File dir, MailService mailService) {

        this.dir = dir;
        this.mailService = mailService;
    }

    /**
     * Create an instance for hte supplied directory.
     *
     * @param dir directory containing instance config and templates
     * @param mailService autowired mail service
     * @return new instance
     * @throws FileNotFoundException if any components are missing
     */
    public static InstanceModel createInstance(File dir, MailService mailService) throws IOException {

        InstanceModel im = new InstanceModel(dir, mailService);

        // load configuration
        im.config = mapper.readValue(new File(dir, configFileName), InstanceConfig.class);

        // load or create data file
        im.dataFile = new File(dir, dataFileName);
        if (im.dataFile.canWrite()) {
            im.approvals = mapper.readValue(im.dataFile, new TypeReference<List<ProjectApproval>>() {
            });
        } else {
            im.approvals = new ArrayList<>();
            im.saveData();
        }

        // test load email template
        im.loadTemplate();

        // load csv users file and merge w/ data file
        im.loadCsv();

        return im;
    }

    private synchronized void saveData() throws IOException {
        mapper.writeValue(dataFile, approvals);
    }

    private Template loadTemplate() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, dir.getAbsolutePath());
        ve.init();
        return ve.getTemplate(emailTemplateFileName);
    }
    
    public void loadCsv() throws IOException {
        File csvFile = new File(dir, contactsFileName);
        Reader in = new FileReader(csvFile);
        CSVParser cp = CSVFormat.EXCEL.withHeader().parse(in);
        // test to for 'email' and 'project' header
        if (!(cp.getHeaderMap().containsKey(SITE_HDR)
                && cp.getHeaderMap().containsKey(NAME_HDR)
                && cp.getHeaderMap().containsKey(EMAIL_HDR)
                && cp.getHeaderMap().containsKey(PROJECT_HDR))) {
            throw new IOException("Missing email, project, site, or name header in " + csvFile.getAbsolutePath());
        }
        boolean save = false;
        for (CSVRecord c : cp) {
            String email = c.get(EMAIL_HDR);
            String project = c.get(PROJECT_HDR);
            boolean add = true;
            String urlCode = null;
            for (ProjectApproval pa : approvals) {
                if (pa.getEmail().equals(email) && pa.getProject().equals(project)) {
                    add = false;
                }
                if (pa.getEmail().equals(email)) {
                    urlCode = pa.getUrlCode();
                }
            }
            // only add if we havne't found anything
            if (add) {
                ProjectApproval pa = new ProjectApproval();
                pa.setEmail(email);
                pa.setProject(project);
                pa.setName(c.get(NAME_HDR));
                pa.setSite(c.get(SITE_HDR));
                if (urlCode == null) {
                    pa.setUrlCode(RandomStringUtils.randomAlphanumeric(10));
                } else {
                    pa.setUrlCode(urlCode);
                }
                approvals.add(pa);
                save = true;
            }
        }
        if (save) {
            saveData();
        }

    }

    public String getName() {
        return dir.getName();
    }

    public InstanceConfig getConfig() {
        return config;
    }

    public List<ProjectApproval> getApprovalsForCode(String code) {
        List<ProjectApproval> l = new ArrayList<>();
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Missing code");
        }
        for (ProjectApproval pa : approvals) {
            if (code.equals(pa.getUrlCode())) {
                l.add(pa);
            }
        }
        return l;
    }

    public void sendMailTo(List<ProjectApproval> approvals) throws IOException, EmailException{
        Collections.sort(approvals, new Comparator<ProjectApproval>(){
            @Override
            public int compare(ProjectApproval o1, ProjectApproval o2) {
                return o1.getEmail().compareTo(o2.getEmail());
            }
        });
        
        int prev = 0;
        for (int i = 0; i < approvals.size(); i++) {
            if (!approvals.get(i).getEmail().equals(approvals.get(prev).getEmail())) {
                mailService.sendMail(this, approvals.subList(prev, i));
                prev = i;
            }
        }
        mailService.sendMail(this, approvals.subList(prev, approvals.size()));
                
        saveData();
    }
    
    public void setResponse(ProjectApproval pa, boolean approved) throws IOException {
        if (!approvals.contains(pa)) {
            throw new IllegalArgumentException("Approval not in this model");
        }
        pa.setRespondedAt(new Date());
        pa.setHasConsented(approved);
        saveData();
    }

    public List<ProjectApproval> getApprovals() {
        return Collections.unmodifiableList(approvals);
    }
    
    public String getTestMail() {
        return createMessageBody(getApprovalsForCode(approvals.get(0).getUrlCode()));
    }
    
    public String createMessageBody(List<ProjectApproval> approvals) {
        StringWriter sw = new StringWriter();
        Context context = new VelocityContext();

        context.put("projects", approvals);
        context.put("config", config);
        loadTemplate().merge(context, sw);
        return sw.toString();
    }
}
