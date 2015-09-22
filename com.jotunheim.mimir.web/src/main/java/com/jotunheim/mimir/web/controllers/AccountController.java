/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.web.service.AccountService;
import com.jotunheim.mimir.web.service.AccountService.LoginData;
import com.jotunheim.mimir.web.utils.RoleAccessLevel;

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
        LoginData data = accountService.login(user.getUserName(), user.getUserPassword());
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
