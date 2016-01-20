/*
 * Copyright 2015 National Socio-Environmental Synthesis Center.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sesync.consent.controllers.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.mail.EmailException;
import org.sesync.consent.controllers.exceptions.MailSendException;
import org.sesync.consent.entities.ProjectApproval;
import org.sesync.consent.model.InstanceFactory;
import org.sesync.consent.model.InstanceModel;
import org.sesync.consent.model.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Root controller to return /
 *
 * @author msmorul
 */
@Controller
public class Admin {

    private static final Logger LOG = LoggerFactory.getLogger(Admin.class);

    @Autowired
    private InstanceFactory instanceFactory;
    @Autowired
    private MailService mailService;

    private ModelAndView getModel(InstanceModel im) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin");
        mav.addObject("im", im);
        return mav;
    }

    @RequestMapping(value = "/{instance}/admin", method = RequestMethod.GET)
    public String askAdmin(@PathVariable("instance") String instance) {

        return "askemail";
    }

    @RequestMapping(value = "/{instance}/admin", method = RequestMethod.POST)
    public String sendEmail(@PathVariable("instance") String instance,
            @RequestParam("email") String email) {

        InstanceModel im = instanceFactory.getInstance(instance);
        for (String e : im.getConfig().getAdminEmails()) {
            if (e.equals(email)) {
                try {
                    mailService.sendAdminLink(im, email);
                } catch (EmailException ex) {
                    LOG.equals(ex); // silently log to avoid spilling info
                }
            }
        }
        return "emailresult";
    }

    @RequestMapping(value = "/{instance}/admin/{key}", method = RequestMethod.GET)
    public ModelAndView adminGet(
            @PathVariable("instance") String instance,
            @PathVariable("key") String key) {

        InstanceModel im = instanceFactory.getInstance(instance);
        if (key.equals(im.getInstanceKey())) {
            return getModel(im);
        } else {
            RedirectView rv = new RedirectView("admin/..");
            rv.setStatusCode(HttpStatus.FOUND);
            return new ModelAndView(rv);
        }
    }

    @RequestMapping(value = "/{instance}/admin/{key}", method = RequestMethod.POST)
    public ModelAndView sendMail(
            @PathVariable("instance") String instance,
            @PathVariable("key") String key,
            @RequestParam("code") int[] projectList) {

        InstanceModel im = instanceFactory.getInstance(instance);

        if (!key.equals(im.getInstanceKey())) {

            RedirectView rv = new RedirectView("admin/..");
            rv.setStatusCode(HttpStatus.FOUND);
            return new ModelAndView(rv);
        }

        List<ProjectApproval> approvals = new ArrayList<>(projectList.length);
        for (int idx : projectList) {
            approvals.add(im.getApprovals().get(idx));
        }
        try {
            LOG.debug("Sending {} requests", approvals.size());
            im.sendMailTo(approvals);
        } catch (EmailException | IOException e) {
            throw new MailSendException(e);
        }

        return getModel(im);
    }

}
