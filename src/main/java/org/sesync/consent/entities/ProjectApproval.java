/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author National Socio-Environmental Synthesis Center
 */
public class ProjectApproval {
    private String email;
    private String project;
    private String site;
    private String name;
    private String urlCode;
    private Date emailSent;
    private boolean hasConsented = false;
    private Date respondedAt;
    private boolean hasResponded = false;
    private Map<String,String> additionalFields = new HashMap<>();

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public String getUrlCode() {
        return urlCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getEmailSent() {
        return emailSent;
    }

    public String getProject() {
        return project;
    }

    public Date getRespondedAt() {
        return respondedAt;
    }

    public String getSite() {
        return site;
    }

    public boolean isHasConsented() {
        return hasConsented;
    }

    public boolean isHasResponded() {
        return hasResponded;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailSent(Date emailSent) {
        this.emailSent = emailSent;
    }

    public void setHasConsented(boolean hasConsented) {
        this.hasConsented = hasConsented;
    }

    public void setHasResponded(boolean hasResponded) {
        this.hasResponded = hasResponded;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setRespondedAt(Date respondedAt) {
        this.respondedAt = respondedAt;
    }

    public void setSite(String site) {
        this.site = site;
    }
    
    
    
}
