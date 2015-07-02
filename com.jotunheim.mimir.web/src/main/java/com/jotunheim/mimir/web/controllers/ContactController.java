/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangle
 *
 */

@RequestMapping("/contact")
@Controller
public class ContactController {
    private static Log LOG = LogFactory.getLog(ContactController.class);

    /**
     * Constructor
     */
    public ContactController() {
        LOG.debug("Creating ContactController.");
    }

    @RequestMapping("/")
    public String listWorks() {
        LOG.debug("List contacts.");
        return "contact";
    }
}
