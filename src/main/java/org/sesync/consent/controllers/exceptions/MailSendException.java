/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sesync.consent.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author National Socio-Environmental Synthesis Center
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error Sending Mail")
public class MailSendException extends RuntimeException{

    public MailSendException(String message) {
        super(message);
    }

    public MailSendException(Throwable cause) {
        super(cause);
    }
    
}
