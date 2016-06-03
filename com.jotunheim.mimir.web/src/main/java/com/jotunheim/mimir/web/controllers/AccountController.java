/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.domain.data.RoleAccessLevel;
import com.jotunheim.mimir.web.service.AccountService;
import com.jotunheim.mimir.web.service.AccountService.LoginResult;
import com.jotunheim.mimir.web.utils.CookieUtils;
import com.jotunheim.mimir.web.utils.SharedConstants;

/**
 * @author dannyzha
 *
 */

@RequestMapping("/account")
@SessionAttributes({"loginUser","userRole"})
@Controller
public class AccountController {
    private static Log LOG = LogFactory
            .getLog(AccountController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao roleDao;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private AccountService accountService;

    /**
     * Constructor
     */
    public AccountController() {
        LOG.debug("Creating LoginController.");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "text/html")
    public String loginForm(Model uiModel, HttpServletRequest request) {
        LOG.debug("create login form.");
        User user = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("user is:" + user);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        uiModel.addAttribute("returnurl", request.getParameter("returnurl"));
        if(user == null || role == null) {
            return "account/login";
        }
        else if(role.getAccessLevel() == RoleAccessLevel.ADMIN) {
            return "redirect:/admin";
        }
        else if(role.getAccessLevel() == RoleAccessLevel.SUPERVISOR) {
            return "redirect:/supervisor";
        }
        return "account/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html")
    public String login(@Valid User user, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        LOG.debug("perform login.");
        if (bindingResult.hasErrors()) {
            return "account/login";
        }
        uiModel.asMap().clear();
        Locale locale = localeResolver.resolveLocale(httpServletRequest);
        LoginResult data = accountService.login(user.getUserName(), user.getUserPassword());
        if (data != null) {
            if (data.statusCode == AccountService.ACCOUNT_NOT_EXIST) {
                // user name not exist
                LOG.debug("User " + user.getUserName() + " does not exist.");
                populateEditForm(uiModel, user, messageSource.getMessage(
                        "login_user_not_exist",
                        new String[] { user.getUserName() }, locale));
                return "account/login";
            } else if (data.statusCode == AccountService.ACCOUNT_WRONG_PASSWORD) {
                LOG.debug("User " + user.getUserName() + ", password wrong.");
                populateEditForm(uiModel, user, messageSource.getMessage(
                        "login_wrong_password",
                        new String[] { user.getUserName() }, locale));
                return "account/login";
            } else if (data.statusCode == AccountService.ACCOUNT_LOGIN_SUCCEED){
                String returnurl=httpServletRequest.getParameter("returnurl");
                User realUser = data.realUser;
                uiModel.addAttribute("loginUser", realUser);
                UserRole role = roleDao.findById(realUser.getRoleID());
                LOG.debug("role is:" + role);
                if(role != null) {
                    uiModel.addAttribute("userRole", role);
                    if(!StringUtils.isEmpty(returnurl)){
                        return "redirect:"+returnurl;
                    }
                    if(role.getAccessLevel() == RoleAccessLevel.USER) {
                        return "redirect:/";
                    }
                    else if(role.getAccessLevel() == RoleAccessLevel.ADMIN) {
                        return "redirect:/admin";
                    }
                    else if(role.getAccessLevel() == RoleAccessLevel.SUPERVISOR) {
                        return "redirect:/supervisor";
                    }
                }
            }
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/ajaxLogin", method = RequestMethod.POST)
    public @ResponseBody String ajaxLogin(Model uiModel,
            HttpServletRequest httpServletRequest,
            HttpServletResponse response,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "password", required = true) String password) {
        LOG.debug("ajax login, userName:" + userName + ", password:" + password);
        uiModel.asMap().clear();
        JSONObject json = new JSONObject();
        LoginResult data = accountService.login(userName, password);
        try {
            if (data.statusCode == AccountService.ACCOUNT_NOT_EXIST) {
                // user name not exist
                String msg = "User " + userName + " does not exist.";
                LOG.debug(msg);
                json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                json.accumulate(SharedConstants.AJAX_MSG_KEY, msg);
                return json.toString();
            } else if (data.statusCode == AccountService.ACCOUNT_WRONG_PASSWORD) {
                String msg = "User " + userName + ", password wrong.";
                LOG.debug(msg);
                json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                json.accumulate(SharedConstants.AJAX_MSG_KEY, msg);
                return json.toString();
            } else if (data.statusCode == AccountService.ACCOUNT_LOGIN_SUCCEED){
                String returnurl=httpServletRequest.getParameter("returnurl");
                User realUser = data.realUser;
                uiModel.addAttribute("loginUser", realUser);
                UserRole role = roleDao.findById(realUser.getRoleID());
                LOG.debug("role is:" + role);
                String url = returnurl;
                if(role != null) {
                    uiModel.addAttribute("userRole", role);
                    if(role.getAccessLevel() == RoleAccessLevel.USER) {
                        url = "/";
                    }
                    else if(role.getAccessLevel() == RoleAccessLevel.ADMIN) {
                        url = "/admin";
                    }
                    else if(role.getAccessLevel() == RoleAccessLevel.SUPERVISOR) {
                        url = "/supervisor";
                    }
                }
                String cookie = accountService.generateUserCookie(userName, password);
                cookieUtils.setCookie(response, SharedConstants.USER_COOKIE_KEY, cookie, SharedConstants.COOKIE_EXPIRE_TIME);
                json.accumulate("url", url);
                json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
                json.accumulate(SharedConstants.AJAX_MSG_KEY, "OK");
                return json.toString();
            }
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "unknow reason");
            return json.toString();
        } catch (Exception ex) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex.toString() + ":" + ex.getMessage());
            return json.toString();
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET, produces = "text/html")
    public String registerForm(Model uiModel, HttpServletRequest request) {
        LOG.debug("create register form.");
        return "account/register";
    }

    @RequestMapping(value = "/ajaxCreate", method = RequestMethod.POST)
    public @ResponseBody String ajaxCreate(Model uiModel,
            HttpServletRequest httpServletRequest,
            HttpServletResponse response,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "phone", required = false) String phoneNumber,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "role", required = false) Long roleID) {
        LOG.debug("ajax create, userName:" + userName + ", password:" + password);
        uiModel.asMap().clear();
        JSONObject json = new JSONObject();
        if(StringUtils.isEmpty(userName)) {
            json.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.put(SharedConstants.AJAX_MSG_KEY, "user name is empty!");
            return json.toString();
        }
        try {
            User user = new User();
            user.setUserName(userName);
            user.setUserPassword(password);
            user.setPhoneNumber(phoneNumber);
            user.setUserNickName(nickName);
            user.setRoleID(roleID);
            accountService.addUser(user, json);
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, "OK");
            return json.toString();
        } catch (Exception ex) {
            json.accumulate(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
            json.accumulate(SharedConstants.AJAX_MSG_KEY, ex.toString() + ":" + ex.getMessage());
            return json.toString();
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "text/html")
    public String logout(Model uiModel, HttpServletRequest request) {
        LOG.debug("logout.");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("userRole");
        request.getSession().removeAttribute("cart");
        uiModel.asMap().clear();
        return "account/login";
    }

    private void populateEditForm(Model uiModel, User user, String message) {
        uiModel.addAttribute("errorMessage", message);
         uiModel.addAttribute("userName", user.getUserName());
    }
}
