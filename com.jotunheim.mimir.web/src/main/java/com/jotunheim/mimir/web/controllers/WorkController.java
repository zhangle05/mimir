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

@RequestMapping("/work")
@Controller
public class WorkController {
    private static Log LOG = LogFactory.getLog(WorkController.class);

    /**
     * Constructor
     */
    public WorkController() {
        LOG.debug("Creating WorkController.");
    }

    @RequestMapping("/")
    public String listWorks() {
        LOG.debug("List works.");
        return "work";
    }
}
