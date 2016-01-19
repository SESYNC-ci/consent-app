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
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid instance")
public class InstanceNotFoundException extends RuntimeException{

    public InstanceNotFoundException(String message) {
        super(message);
    }
    
}
