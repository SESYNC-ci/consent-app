/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for the config.json file. 
 * 
 * @author National Socio-Environmental Synthesis Center
 */
public class InstanceConfig {

    private String title = "";
    private String mailFrom = "";
    private String mailSubject = "";
    private String projectDescription = "";
    private String submissionComplete = "";
    private String[] adminEmails = new String[0];
    private Map<String,String> additionalFields = new HashMap<>();

    @JsonCreator
    public InstanceConfig(@JsonProperty("title") String title,
            @JsonProperty("mailFrom") String mailFrom,
            @JsonProperty("mailSubject") String mailSubject,
            @JsonProperty("projectDescription") String projectDescription,
            @JsonProperty("submissionComplete") String submissionComplete,
            @JsonProperty("adminEmails") String[] adminEmails,
            @JsonProperty("additionalFields") Map<String,String> additionalFields) {
        this.title = title;
        this.mailFrom = mailFrom;
        this.mailSubject = mailSubject;
        this.projectDescription = projectDescription;
        this.submissionComplete = submissionComplete;
        this.adminEmails = adminEmails;
        this.additionalFields = Collections.unmodifiableMap(additionalFields);
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }
    
    public String[] getAdminEmails() {
        return adminEmails;
    }

    public String getSubmissionComplete() {
        return submissionComplete;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public String getTitle() {
        return title;
    }
}
