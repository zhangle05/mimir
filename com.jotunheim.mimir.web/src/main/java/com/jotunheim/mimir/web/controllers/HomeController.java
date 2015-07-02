/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dannyzha
 *
 */
@RequestMapping("/")
@Controller
public class HomeController {
    private static Log LOG = LogFactory.getLog(HomeController.class);

    /**
     * Constructor
     */
    public HomeController() {
        LOG.debug("Creating HomeController.");
    }

    @RequestMapping("")
    public String goHome() {
        LOG.debug("Go home.");
        return "index";
    }

}
