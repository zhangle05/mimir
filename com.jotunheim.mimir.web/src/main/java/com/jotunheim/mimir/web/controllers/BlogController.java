/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jotunheim.mimir.common.utils.StringUtils;
import com.jotunheim.mimir.dao.BlogDao;
import com.jotunheim.mimir.domain.Blog;
import com.jotunheim.mimir.web.utils.SharedConstants;

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
        List<Blog> blogList = blogDao.listBlogs(0, 10);
        for(Blog b : blogList) {
            b.setTitle(StringEscapeUtils.escapeHtml(b.getTitle()));
            b.setAbstra(StringEscapeUtils.escapeHtml(b.getAbstra()));
            b.setHtmlBody(StringEscapeUtils.escapeHtml(b.getHtmlBody()));
        }
        uiModel.addAttribute("blogs", blogList);
        return "blog";
    }

    @RequestMapping(value = "/ajax", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public @ResponseBody String ajaxBlogList(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "p", required = true) Integer page,
            @RequestParam(value = "ps", required = true) Integer pageSize) {
        LOG.debug("Ajax list blogs.");
        if(page < 0) {
            page = 0;
        }
        List<Blog> blogList = blogDao.listBlogs(page, pageSize);
        for(Blog b : blogList) {
            b.setTitle(StringEscapeUtils.escapeHtml(b.getTitle()));
            b.setAbstra(StringEscapeUtils.escapeHtml(b.getAbstra()));
            b.setHtmlBody(StringEscapeUtils.escapeHtml(b.getHtmlBody()));
        }
        JSONObject json = new JSONObject();
        json.accumulate("code", SharedConstants.AJAXCODE_OK);
        json.accumulate("msg", "ok");
        json.accumulate("count", blogDao.getBlogCount());
        json.accumulate("blogs", blogList);
        LOG.debug("Ajax list blogs done, result is:" + json.toString());
        return json.toString();
    }

    @RequestMapping(value = "/detail/{id}")
    public String blogDetail(Model uiModel, HttpServletRequest request,
            @PathVariable(value = "id") Long blogId) {
        LOG.debug("Get blog detail of:" + blogId);
        Blog blog = blogDao.findById(blogId);
        LOG.debug("Get blog done, result is:" + blog);
        if(blog != null) {
            LOG.debug("Blog abstract is:" + blog.getAbstra());
            if(StringUtils.isEmpty(blog.getAbstra())) {
                String plainBody = StringUtils.filterHtml(blog.getHtmlBody());
                blog.setAbstra(plainBody.substring(0, 100) + "...");
                blogDao.attachDirty(blog);
            }
        }
        uiModel.addAttribute("blog", blog);
        return "blog_detail";
    }

    public static void main(String[] args) {
        System.out.println(StringEscapeUtils.escapeHtml("container.append('<div class=\"load\"><img src=\"/img/load.gif\" width=\"6%\" /></div>');  "));
    }
}
