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
@RequestMapping("/blog")
@Controller
public class BlogController {
    private static Log LOG = LogFactory.getLog(BlogController.class);

    /**
     * Constructor
     */
    public BlogController() {
        LOG.debug("Creating BlogController.");
    }

    @RequestMapping("/")
    public String goHome() {
        LOG.debug("Go blog.");
        return "blog";
    }
}
