/**
 * 
 */
package com.jotunheim.mimir.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jotunheim.mimir.dao.BlogDao;
import com.jotunheim.mimir.domain.Blog;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.domain.data.RoleAccessLevel;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.utils.SharedConstants;

/**
 * @author zhangle
 *
 */
@RequestMapping("/admin/blog")
@Controller
@Login(role = RoleAccessLevel.ADMIN)
public class BlogAdminController {
    private static Log LOG = LogFactory.getLog(BlogAdminController.class);

    @Autowired
    private BlogDao blogDao;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html")
    public String adminHome(Model uiModel, HttpServletRequest request) {
        LOG.debug("blog admin home.");
        return "redirect:/admin/index";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "text/html")
    public String listBlogs(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "page", required = false) Integer page) {
        LOG.debug("get blog list.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        if(page == null) {
            page = 0;
        }
        uiModel.addAttribute("count", blogDao.getBlogCount());
        uiModel.addAttribute("blogs", blogDao.listBlogs(page, SharedConstants.DEFAULT_PAGE_SIZE));
        return "admin/blog_list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET, produces = "text/html")
    public String addBlogForm(Model uiModel, HttpServletRequest request) {
        LOG.debug("create add-blog form.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        uiModel.addAttribute("returnUrl", "/admin/blog/list");
        return "admin/add_blog";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String addBlog(HttpServletRequest request, @Valid Blog blog,
            BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        LOG.debug("add blog");
        JSONObject json = new JSONObject();
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_ROLE_ACCESS_LEVEL_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "Only the administrator can add blogs.");
            return json.toString();
        }
        if(StringUtils.isEmpty(blog.getTitle())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "标题不能为空!");
            return json.toString();
        }
        if(StringUtils.isEmpty(blog.getHtmlBody())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "内容不能为空!");
            return json.toString();
        }
        try {
            if(StringUtils.isEmpty(blog.getAuthor())) {
                User admin = (User) request.getSession().getAttribute("loginUser");
                blog.setAuthor(admin.getUserName());
            }
            if(StringUtils.isEmpty(blog.getAbstra())) {
                int len = SharedConstants.DEFAULT_ABSTRA_LENGTH < blog.getHtmlBody().length() ?
                        SharedConstants.DEFAULT_ABSTRA_LENGTH : blog.getHtmlBody().length() / 2;
                blog.setAbstra(blog.getHtmlBody().substring(0, len));
            }
            blog.setCreateTime(new java.util.Date());
            blog.setLastUpdateTime(new java.util.Date());
            blogDao.persist(blog);
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET, produces = "text/html")
    public String deleteBlog(Model uiModel, @Valid Blog blog, BindingResult bindingResult, HttpServletRequest request) {
        LOG.debug("deleting blog.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        Blog realBlog = blogDao.findById(blog.getId());
        if(realBlog == null) {
            uiModel.addAttribute("errorMsg", "blog'" + blog.getId() + "'不存在!");
            return "uncaught_exception";
        }
        blogDao.delete(blog);
        return "redirect:/admin/blog/list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET, produces = "text/html")
    public String updateBlogForm(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "bid", required = true) Long blogId) {
        LOG.debug("create edit-blog form.");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        Blog blog = blogDao.findById(blogId);
        if(blog == null) {
            uiModel.addAttribute("errorMsg", "没找到'" + blogId + "'对应的blog!");
            return "uncaught_exception";
        }
        LOG.info("blog found:" + blog.getTitle());
        uiModel.addAttribute("blog", blog);
        return "admin/edit_blog";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String editBlog(Model uiModel, HttpServletRequest request, @Valid Blog blog, BindingResult bindingResult) {
        LOG.debug("edit blog");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        JSONObject json = new JSONObject();
        LOG.debug("blog id is:" + blog.getId());
        Blog realBlog = blogDao.findById(blog.getId());
        if(realBlog == null) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "没找到'" + blog.getId() + "'对应的blog!");
            return json.toString();
        }
        if(StringUtils.isEmpty(blog.getTitle())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "标题不能为空!");
            return json.toString();
        }
        if(StringUtils.isEmpty(blog.getHtmlBody())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "内容不能为空!");
            return json.toString();
        }
        try {
            LOG.debug("========= fields to edit: ==================");
            LOG.debug("title: " + blog.getTitle());
            realBlog.setTitle(blog.getTitle());
            LOG.debug("html body:" + blog.getHtmlBody());
            realBlog.setHtmlBody(blog.getHtmlBody());
            if(StringUtils.isEmpty(blog.getAbstra())) {
                int len = SharedConstants.DEFAULT_ABSTRA_LENGTH < blog.getHtmlBody().length() ?
                        SharedConstants.DEFAULT_ABSTRA_LENGTH : blog.getHtmlBody().length() / 2;
                realBlog.setAbstra(blog.getHtmlBody().substring(0, len));
            }
            realBlog.setLastUpdateTime(new java.util.Date());
            blogDao.attachDirty(realBlog);
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

}
