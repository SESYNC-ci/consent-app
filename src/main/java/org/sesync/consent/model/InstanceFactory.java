/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Map<String, InstanceModel> instances = new HashMap<>();

    @PostConstruct
    private void loadConfigs() {
        LOG.debug("Loading instances from: " + configDir);
    }

    public InstanceModel getInstance(String instance) {
        return instances.get(instance);
    }

}
