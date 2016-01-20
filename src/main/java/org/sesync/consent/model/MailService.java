/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.model;

import java.util.Date;
import java.util.List;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.sesync.consent.entities.ProjectApproval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author National Socio-Environmental Synthesis Center
 */
@Service()
public class MailService {
    @Value("${mail.server}")
    private String mailServer;
    @Value("${app.prefix}")
    private String appUrl;
    
    public void sendMail(InstanceModel im, List<ProjectApproval> approvals) throws EmailException{
        String messageBody = im.createMessageBody(approvals);
        String subject = im.getConfig().getMailSubject();
        String mailFrom = im.getConfig().getMailFrom();
        
        Email email = new SimpleEmail();
        email.setHostName(mailServer);
        email.setFrom(mailFrom);
        email.addTo(approvals.get(0).getEmail(), approvals.get(0).getName());
        email.setSubject(subject);
        email.setMsg(messageBody);
        email.send();
        for (ProjectApproval pa : approvals) {
            pa.setEmailSent(new Date());
        }
       
    }
    
    public void sendAdminLink(InstanceModel im, String mailTo) throws EmailException {
        Email email = new SimpleEmail();
        String mailFrom = im.getConfig().getMailFrom();
        email.setHostName(mailServer);
        email.setFrom(mailFrom);
        email.addTo(mailTo);
        email.setSubject("Admin URL");
        email.setMsg("\n\nAdmin Link: "+appUrl+"/"+im.getName()+"/admin/"+im.getInstanceKey()+"\n\n");
        email.send();
    }
}
