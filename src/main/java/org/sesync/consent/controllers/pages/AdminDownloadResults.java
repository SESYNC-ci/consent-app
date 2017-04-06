/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.controllers.pages;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.sesync.consent.controllers.exceptions.InstanceNotFoundException;
import org.sesync.consent.controllers.exceptions.ResultGenerationException;
import org.sesync.consent.entities.ProjectApproval;
import org.sesync.consent.model.InstanceFactory;
import org.sesync.consent.model.InstanceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Servive to download a results csv.
 *
 * @author msmorul
 */
@Controller
public class AdminDownloadResults {

    private static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));

    private static final Logger LOG = LoggerFactory.getLogger(AdminDownloadResults.class);
    @Autowired
    private InstanceFactory instanceFactory;

    @RequestMapping(value = "/{instance}/admin/{key}/results.csv", method = RequestMethod.GET, produces = {"text/csv"})
    public ResponseEntity<String> downloadResponsesCSV(
            @PathVariable("instance") String instance,
            @PathVariable("key") String key,
            HttpServletResponse response
    ) {
        InstanceModel im = instanceFactory.getInstance(instance);

        if (!key.equals(im.getInstanceKey())) {
            throw new InstanceNotFoundException("Invalid admin key");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"responses.csv\"");
        headers.setContentType(MEDIA_TYPE);
        
        StringWriter out = new StringWriter();
        try {
        CSVPrinter cp = new CSVPrinter(out, CSVFormat.EXCEL);

        // Write headers
        cp.print(InstanceModel.NAME_HDR);
        cp.print(InstanceModel.EMAIL_HDR);
        cp.print("Date Contacted");
        cp.print(InstanceModel.PROJECT_HDR);
        cp.print(InstanceModel.SITE_HDR);
        cp.print("Responded");
        cp.print("Date Responded");
        cp.print("Consented");
        for (String addlField : im.getConfig().getAdditionalFields().keySet()) {
            cp.print(addlField);
        }
        cp.println();

        // Iterate and write approval object
        for (ProjectApproval pa : im.getApprovals()) {
            cp.print(pa.getName());
            cp.print(pa.getEmail());
            cp.print(pa.getEmailSent());
            cp.print(pa.getProject());
            cp.print(pa.getSite());
            cp.print((pa.isHasResponded() ? "yes" : "no"));
            cp.print(pa.getRespondedAt());
            cp.print((pa.isHasConsented() ? "yes" : "no"));
            for (String addlField : im.getConfig().getAdditionalFields().keySet()) {
                cp.print(pa.getAdditionalFields().get(addlField));
            }
            cp.println();
        }

        cp.flush();
        cp.close();
        } catch (IOException e) {
            LOG.error("Error writing csv ",e);
            throw new ResultGenerationException(e);
        }
        return new ResponseEntity<>(out.toString(), headers, HttpStatus.OK);

    }
}
