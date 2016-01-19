/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author National Socio-Environmental Synthesis Center
 */
public class InstanceConfig {

    private String title = "";
    private String mailFrom = "";
    private String mailSubject = "";
    private String projectDescription = "";

    @JsonCreator
    public InstanceConfig(@JsonProperty("title") String title,
            @JsonProperty("mailFrom") String mailFrom,
            @JsonProperty("mailSubject") String mailSubject,
            @JsonProperty("projectDescription") String projectDescription) {
        this.title = title;
        this.mailFrom = mailFrom;
        this.mailSubject = mailSubject;
        this.projectDescription = projectDescription;
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
