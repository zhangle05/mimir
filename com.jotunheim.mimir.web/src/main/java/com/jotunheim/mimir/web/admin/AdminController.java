/**
 * 
 */
package com.jotunheim.mimir.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.utils.RoleAccessLevel;

/**
 * @author zhangle
 *
 */
@RequestMapping("/admin")
@Controller
@Login(role = RoleAccessLevel.ADMIN)
public class AdminController {
    private static Log LOG = LogFactory
            .getLog(AdminController.class);

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html")
    public String adminHome(Model uiModel, HttpServletRequest request) {
        LOG.debug("admin home.");
        return "redirect:/admin/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = "text/html")
    public String adminIndex(Model uiModel, HttpServletRequest request) {
        LOG.debug("admin index.");
        User user = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("user is:" + user);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        uiModel.addAttribute("user", user);
        return "admin/index";
    }

    @RequestMapping(value = "/userlist", method = RequestMethod.GET, produces = "text/html")
    public String listUsers(Model uiModel, HttpServletRequest request) {
        LOG.debug("get user list.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        String pageStr = request.getParameter("page");
        int page = 0;
        try {
            page = Integer.parseInt(pageStr);
        } catch(Exception ex) {
        }
        uiModel.addAttribute("count", userDao.getUserCount());
        uiModel.addAttribute("users", userDao.listUsers(page, 10));
        return "admin/listUser";
    }
    @RequestMapping(value = "/adduser", method = RequestMethod.GET, produces = "text/html")
    public String addUserForm(Model uiModel, HttpServletRequest request) {
        LOG.debug("create add-user form.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        return "admin/addUser";
    }
    @RequestMapping(value = "/adduser", method = RequestMethod.POST, produces = "text/html")
    public String addUser(Model uiModel, HttpServletRequest request, @Valid User user, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        LOG.debug("add user");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        userDao.persist(user);
        return "redirect:/admin/userlist";
    }
    @RequestMapping(value = "/deluser", method = RequestMethod.GET, produces = "text/html")
    public String deleteUser(Model uiModel, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        LOG.debug("deleting user.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        userDao.delete(user);
        return "redirect:/admin/userlist";
    }
}
