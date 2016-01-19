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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.sesync.consent.model.InstanceFactory;
import org.sesync.consent.model.InstanceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    
    private ModelAndView getModel(InstanceModel im) {
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin");
        mav.addObject("im", im);
        return mav;
    }
    
    @RequestMapping(value = "/{instance}/admin", method = RequestMethod.GET)
    public ModelAndView adminGet(@PathVariable("instance") String instance) {
        InstanceModel im = instanceFactory.getInstance(instance);
        return getModel(im);
    }
    
    @RequestMapping(value = "/{instance}/admin", method = RequestMethod.POST)
    public ModelAndView sendMail(@PathVariable("instance") String instance, @RequestParam("code") String[] codeList) {
        InstanceModel im = instanceFactory.getInstance(instance);
        Set<String> codes = new HashSet<>();
        codes.addAll(Arrays.asList(codeList));
        for (String code : codes) {
            LOG.info("Sending to: " + code);
            im.sendMailTo(code);
        }
        
        return getModel(im);
    }
    
}
