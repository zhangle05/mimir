/**
 * 
 */
package com.jotunheim.mimir.web.admin;

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
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.domain.data.RoleAccessLevel;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.service.AccountService;
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
    private AccountService accountService;

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
        uiModel.addAttribute("page", page);
        uiModel.addAttribute("count", userDao.getUserCount(isSuper));
        uiModel.addAttribute("users", userDao.listUsers(page, SharedConstants.DEFAULT_PAGE_SIZE, isSuper));
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
        int userLevel = RoleAccessLevel.getAccessLevel(user.getRoleID());
        if(userLevel == 0) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "新增用户未设置权限!");
            return json.toString();
        }
        if(userLevel >= role.getAccessLevel()) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "新用户权限高于当前用户权限!");
            return json.toString();
        }
        try {
            if(!accountService.addUser(user, json)) {
                return json.toString();
            }
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
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        User realUser = userDao.findById(user.getId());
        if(realUser == null) {
            uiModel.addAttribute("errorMsg", "用户'" + user.getId() + "'不存在!");
            return "uncaught_exception";
        }
        JSONObject json = new JSONObject();
        if(!checkDeletePermission(admin, realUser, json)) {
            uiModel.addAttribute("errorMsg", json.get(SharedConstants.AJAX_MSG_KEY));
            return "uncaught_exception";
        }
        userDao.delete(user);
        return "redirect:/admin/userlist";
    }

    @RequestMapping(value = "/changepswd", method = RequestMethod.GET, produces = "text/html")
    public String changePswdForm(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "uid", required = false) Long userID) {
        LOG.debug("create change-password form.");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }

        User u=userDao.findById(userID);
        if(u == null){
            uiModel.addAttribute("errorMsg", "没找到对应的用户!");
            return "uncaught_exception";
        }
        JSONObject json = new JSONObject();
        if(!checkChangePermission(admin, u, json)){
            uiModel.addAttribute("errorMsg", json.get(SharedConstants.AJAX_MSG_KEY));
            return "uncaught_exception";
        }
        uiModel.addAttribute("uid", userID);
        return "admin/change_pswd";
    }

    @RequestMapping(value = "/changepswd", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String changePswd(Model uiModel, HttpServletRequest request, HttpServletRequest httpServletRequest,
            @RequestParam(value = "uid", required = false) Long userID,
            @RequestParam(value = "userPswd", required = false) String pswd,
            @RequestParam(value = "confirmPswd", required = false) String confirmPswd
            ) {
        LOG.debug("change password for user:" + userID);
        JSONObject json = new JSONObject();
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        User realUser = userDao.findById(userID);
        if(realUser == null) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "没找到对应的用户!");
            return json.toString();
        }
        LOG.debug("real user role id is:" + realUser.getRoleID());
        if(!checkChangePermission(admin, realUser, json)) {
            return json.toString();
        }
        LOG.debug("pswd is:" + pswd);
        try {
            if(!accountService.changePswdByAdmin(realUser, pswd, confirmPswd, json)) {
                return json.toString();
            }
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

    @RequestMapping(value = "/edituser", method = RequestMethod.GET, produces = "text/html")
    public String updateUserForm(Model uiModel, HttpServletRequest request,
            @RequestParam(value = "uid", required = false) Long userID) {
        LOG.debug("create edit-user form.");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        if(userID == null) {
            uiModel.addAttribute("errorMsg", "uid不能为空!");
            return "uncaught_exception";
        }
        User u = userDao.findById(userID);
        if(u == null) {
            uiModel.addAttribute("errorMsg", "没找到对应的用户!");
            return "uncaught_exception";
        }
        JSONObject json = new JSONObject();
        if(!checkChangePermission(admin, u, json)){
            uiModel.addAttribute("errorMsg", json.get(SharedConstants.AJAX_MSG_KEY));
            return "uncaught_exception";
        }
        LOG.info("find user:" + u.getUserNickName());
        uiModel.addAttribute("user", u);
        return "admin/edit_user";
    }

    @RequestMapping(value = "/edituser", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String editUser(Model uiModel, HttpServletRequest request, @Valid User user, BindingResult bindingResult) {
        LOG.debug("edit user");
        User admin = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("admin is:" + admin);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        JSONObject json = new JSONObject();
        LOG.debug("user id is:" + user.getId());
        User realUser = userDao.findById(user.getId());
        if(realUser == null) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "没找到对应的用户!");
            return json.toString();
        }
        if(!checkChangePermission(admin, realUser, json)){
            return json.toString();
        }
        try {
            LOG.debug("========= fields to edit: ==================");
            LOG.debug("nick name: " + user.getUserNickName());
            realUser.setUserNickName(user.getUserNickName());
            LOG.debug("phone number:" + user.getPhoneNumber());
            if(!StringUtils.isEmpty(user.getPhoneNumber())) {
                realUser.setPhoneNumber(user.getPhoneNumber());
            }
            userDao.attachDirty(realUser);
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "success");
        } catch (Exception ex) {
            ex.printStackTrace();
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex + "-" + ex.getMessage());
        }
        return json.toString();
    }

    private boolean checkChangePermission(User admin, User u, JSONObject json) {
        LOG.debug("checkChangePermission, user role id is:" + u.getRoleID());
        int userLevel = RoleAccessLevel.getAccessLevel(u.getRoleID());
        if(userLevel >= RoleAccessLevel.getAccessLevel(admin.getRoleID())
                && admin.getId() != u.getId()) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "无权限修改" + userLevel + "级用户'" + u.getUserName() + "'!");
            return false;
        }
        return true;
    }

    private boolean checkDeletePermission(User admin, User u, JSONObject json) {
        LOG.debug("checkDeletePermission, user role id is:" + u.getRoleID());
        if(!checkChangePermission(admin, u, json)) {
            return false;
        }
        if(admin.getId() == u.getId()) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "无法删除自己!");
            return false;
        }
        int userLevel = RoleAccessLevel.getAccessLevel(u.getRoleID());
        if(userLevel >= RoleAccessLevel.ROLE_ID_SUPERVISOR) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "无权限删除超级用户'" + u.getUserName() + "'!");
            return false;
        }
        return true;
    }

    private void initRoleList(Model uiModel, UserRole loginRole) {
        List<UserRole> roleList = RoleAccessLevel.getRoleList(loginRole);
        uiModel.addAttribute("roles", roleList);
    }
}
