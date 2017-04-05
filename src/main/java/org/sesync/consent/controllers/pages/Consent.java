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
import java.util.List;
import java.util.Map;
import org.sesync.consent.entities.ProjectApproval;
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
public class Consent {

    private static final Logger LOG = LoggerFactory.getLogger(Consent.class);

    @Autowired
    private InstanceFactory instanceFactory;

    @RequestMapping(value = "/{instance}/consent/{uid}", method = RequestMethod.GET)
    public ModelAndView consentGet(
            @PathVariable("instance") String instance,
            @PathVariable("uid") String uid) {
        InstanceModel im = instanceFactory.getInstance(instance);
        List<ProjectApproval> l = im.getApprovalsForCode(uid);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("approve");
        mav.addObject("im", im);
        mav.addObject("projects", l);

        return mav;
    }

    @RequestMapping(value = "/{instance}/consent/{uid}", method = RequestMethod.POST)
    public ModelAndView receiveResponse(
            @PathVariable("instance") String instance,
            @PathVariable("uid") String uid,
            @RequestParam(value = "project", required = false, defaultValue = "-1") int[] projectList,
            @RequestParam() Map<String, String> otherparams) {

        InstanceModel im = instanceFactory.getInstance(instance);
        List<ProjectApproval> approvals = im.getApprovalsForCode(uid);

        Arrays.sort(projectList);
        LOG.info("approvals {} {}", approvals.size(), Arrays.toString(projectList));
        for (int i = 0; i < approvals.size(); i++) {

            ProjectApproval pa = approvals.get(i);
            pa.setHasConsented(Arrays.binarySearch(projectList, i) >= 0);
            // Additional form parameters are encoded:
            // index_FieldName
            pa.getAdditionalFields().clear();
            // Loop through all additional configured fields and test to see if its contained in this
            // form for this particular approval.
            for (String fieldName : im.getConfig().getAdditionalFields().keySet()) {
                String formField = i + "_" + fieldName;
                LOG.trace("Testing key formField: {}",formField);
                if (otherparams.containsKey(formField)) {
                    LOG.trace("Setting field '{}' val: '{}'",fieldName, otherparams.get(formField));
                    pa.getAdditionalFields().put(fieldName, otherparams.get(formField));
                }

            }
            im.setResponse(pa);
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("thanks");
        mav.addObject("im", im);
        return mav;

    }
}
