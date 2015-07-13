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

import com.jotunheim.mimir.dao.BlogDao;
import com.jotunheim.mimir.domain.Blog;

/**
 * @author zhangle
 *
 */
@RequestMapping("/blog")
@Controller
public class BlogController {
    private static Log LOG = LogFactory.getLog(BlogController.class);

    @Autowired
    private BlogDao blogDao;

    /**
     * Constructor
     */
    public BlogController() {
        LOG.debug("Creating BlogController.");
    }

    @RequestMapping("/")
    public String listBlogs(Model uiModel, HttpServletRequest request) {
        LOG.debug("List blogs.");
        List<Blog> blogList = blogDao.listBlogs(1, 10);
        uiModel.addAttribute("blogs", blogList);
        return "blog";
    }

    @RequestMapping(value = "/ajax")
    public @ResponseBody String ajaxBlogList(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "p", required = true) Integer page,
            @RequestParam(value = "ps", required = true) Integer pageSize) {
        LOG.debug("Ajax list blogs.");
        if(page <= 0) {
            page = 1;
        }
        List<Blog> blogList = blogDao.listBlogs(page, pageSize);
        JSONObject json = new JSONObject();
        json.accumulate("count", blogDao.getBlogCount());
        json.accumulate("blogs", blogList);
        LOG.debug("Ajax list blogs done, result is:" + json.toString());
        return json.toString();
    }
}
