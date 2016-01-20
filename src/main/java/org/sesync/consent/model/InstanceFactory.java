/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.sesync.consent.controllers.exceptions.InstanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Manage multiple instances. Each 'instance' is stored in a sub directory
 * containins welcoming email, user list and the json data file. This dir shoudl
 * be writable by the application.
 *
 * @author National Socio-Environmental Synthesis Center
 */
@Service()
public class InstanceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InstanceFactory.class);

    @Value("${configuration.dir}")
    private String configDir;
    
    @Autowired
    private MailService mailService;
    
    private final Map<String, InstanceModel> instances = new HashMap<>();

    private static final FileFilter configFilter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && pathname.canWrite();
        }
    };

    /**
     * Scan instance directory and create instances for each sub directory
     */
    @PostConstruct
    private void loadConfigs() {
        LOG.debug("Loading instances from: " + configDir);
        File dir = new File(configDir);
        if (!dir.isDirectory()) {
            throw new RuntimeException("Cannot load config directory " + configDir);
        }
        for (File f : dir.listFiles(configFilter)) {
            LOG.debug("Loading instance: " + f.getName());
            try {
                InstanceModel im = InstanceModel.createInstance(f, mailService);
                instances.put(im.getName(), im);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * 
     * @param instance instance to retrieve
     * @return
     * @throws InstanceNotFoundException if an instance w/ the supplied name wasn't found
     */
    public InstanceModel getInstance(String instance) throws InstanceNotFoundException {
        if (instances.containsKey(instance)) {
            return instances.get(instance);
        } else {
            throw new InstanceNotFoundException("Cannot load " + instance);
        }
    }

}
