/**
 * 
 */
package com.jotunheim.mimir.web.admin;

import java.util.ArrayList;
import java.util.List;

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

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.domain.data.RoleAccessLevel;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.utils.SharedConstants;

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

    @Autowired
    private UserRoleDao roleDao;

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
    public String listUsers(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "page", required = false) Integer page) {
        LOG.debug("get user list.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        if(page == null) {
            page = 0;
        }
        initRoleList(uiModel, role);
        boolean isSuper = (role.getId() == RoleAccessLevel.ROLE_ID_SUPERVISOR);
        uiModel.addAttribute("count", userDao.getUserCount(isSuper));
        uiModel.addAttribute("users", userDao.listUsers(page, 10, isSuper));
        return "admin/user_list";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.GET, produces = "text/html")
    public String addUserForm(Model uiModel, HttpServletRequest request) {
        LOG.debug("create add-user form.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        initRoleList(uiModel, role);
        uiModel.addAttribute("returnUrl", "/admin/userlist");
        return "admin/add_user";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String addUser(HttpServletRequest request, @Valid User user, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        LOG.debug("add user");
        JSONObject json = new JSONObject();
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_ROLE_ACCESS_LEVEL_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "Only the administrator can add users.");
            return json.toString();
        }
        if(StringUtils.isEmpty(user.getUserName())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "用户名不能为空!");
            return json.toString();
        }
        if(StringUtils.isEmpty(user.getUserPassword())) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "用户密码不能为空!");
            return json.toString();
        }
        UserRole addedRole = roleDao.findById(user.getRoleID());
        if(addedRole == null) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "新增用户未设置权限!");
            return json.toString();
        }
        if(addedRole.getAccessLevel() >= role.getAccessLevel()) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "新用户权限高于当前用户权限!");
            return json.toString();
        }
        try {
            userDao.persist(user);
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

    @RequestMapping(value = "/deluser", method = RequestMethod.GET, produces = "text/html")
    public String deleteUser(Model uiModel, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {
        LOG.debug("deleting user.");
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        User realUser = userDao.findById(user.getId());
        if(realUser == null) {
            uiModel.addAttribute("errorMsg", "用户'" + user.getId() + "'不存在!");
            return "uncaught_exception";
        }
        UserRole userRole = roleDao.findById(realUser.getRoleID());
        LOG.debug("user role is:" + userRole + ", login level:" + role.getAccessLevel()
                + ", user level:" + (userRole == null ? 0 : userRole.getAccessLevel()));
        if(userRole == null || userRole.getAccessLevel() >= role.getAccessLevel()) {
            uiModel.addAttribute("errorMsg", "没有权限删除" + userRole.getRoleName() + "用户'" + realUser.getUserName() + "'!");
            return "uncaught_exception";
        }
        userDao.delete(user);
        return "redirect:/admin/userlist";
    }

    private void initRoleList(Model uiModel, UserRole loginRole) {
        List<UserRole> roleList = new ArrayList<UserRole>();
        UserRole userRole = roleDao.findById(RoleAccessLevel.ROLE_ID_USER);
        UserRole adminRole = roleDao.findById(RoleAccessLevel.ROLE_ID_ADMIN);
        if(loginRole == null || loginRole.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return;
        } else {
            if(userRole != null) {
                roleList.add(userRole);
            }
            if(loginRole.getAccessLevel() == RoleAccessLevel.SUPERVISOR) {
                if(adminRole != null) {
                    roleList.add(adminRole);
                }
            }
        }
        uiModel.addAttribute("roles", roleList);
    }
}
