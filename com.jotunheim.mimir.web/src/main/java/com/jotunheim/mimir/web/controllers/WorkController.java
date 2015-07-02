/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jotunheim.mimir.dao.PhotoDao;
import com.jotunheim.mimir.domain.Photo;

/**
 * @author zhangle
 *
 */

@RequestMapping("/work")
@Controller
public class WorkController {
    private static Log LOG = LogFactory.getLog(WorkController.class);

    @Autowired
    private PhotoDao photoDao;

    /**
     * Constructor
     */
    public WorkController() {
        LOG.debug("Creating WorkController.");
    }

    @RequestMapping("/")
    public String listWorks(Model uiModel, HttpServletRequest request) {
        LOG.debug("List works.");
        List<Photo> photoList = photoDao.listPhotos(1, 10);
        uiModel.addAttribute("photos", photoList);
        return "work";
    }
}
