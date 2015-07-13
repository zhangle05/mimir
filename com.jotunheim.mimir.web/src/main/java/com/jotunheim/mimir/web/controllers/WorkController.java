/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jotunheim.mimir.dao.PhotoDao;
import com.jotunheim.mimir.domain.Blog;
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


    @RequestMapping(value = "/ajax")
    public @ResponseBody String ajaxWorkList(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "p", required = true) Integer page,
            @RequestParam(value = "ps", required = true) Integer pageSize) {
        LOG.debug("Ajax list works.");
        if(page <= 0) {
            page = 1;
        }
        List<Photo> blogList = photoDao.listPhotos(page, pageSize);
        JSONObject json = new JSONObject();
        json.accumulate("count", photoDao.getPhotoCount());
        json.accumulate("photos", blogList);
        LOG.debug("Ajax list works done, result is:" + json.toString());
        return json.toString();
    }
}
