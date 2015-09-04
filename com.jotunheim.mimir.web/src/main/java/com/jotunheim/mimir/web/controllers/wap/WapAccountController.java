package com.jotunheim.mimir.web.controllers.wap;

import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.jotunheim.mimir.common.utils.CipherHelper;
import com.jotunheim.mimir.common.utils.URLHelper;
import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.web.utils.RoleAccessLevel;

@RequestMapping("/wap/account")
@SessionAttributes({ "loginUser", "userRole" })
@Controller
public class WapAccountController {

    private static Log LOG = LogFactory.getLog(WapAccountController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao roleDao;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    /**
     * Constructor
     */
    public WapAccountController() {
        LOG.debug("Creating WapAccountController.");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "text/html")
    public String loginForm(Model uiModel, HttpServletRequest request,
            HttpServletResponse response) {
        String openId = (String) request.getAttribute("openId");
        Boolean isWechat = (Boolean) request.getAttribute("isWechat");
        String returnurl = request.getParameter("returnurl");
        // 在微信浏览器中访问没有openid是需要做一次跳转，weixin.zuoyetong.com.cn中后去openid
        if (isWechat != null && isWechat && StringUtils.isEmpty(openId)) {
            try {
                StringBuffer url = request.getRequestURL();
                String query = request.getQueryString();
                if (StringUtils.isNotEmpty(query)) {
                    url.append("?").append(query);
                }
                return "redirect:"
                        + String.format(
                                "http://weixin.zuoyetong.com.cn/r?url=%s",
                                URLEncoder.encode(url.toString(), "utf-8"));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        LOG.debug("create login form.");
        User user = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("user is:" + user);
        UserRole role = (UserRole) request.getSession()
                .getAttribute("userRole");
        LOG.debug("role is:" + role);

        uiModel.addAttribute("returnurl", returnurl);

        if (user == null || role == null) {
            return "wap/account/login";
        } else if (role.getAccessLevel() == RoleAccessLevel.ADMIN) {
            return "redirect:/wap/admin/index";
        } else if (role.getAccessLevel() == RoleAccessLevel.SUPERVISOR) {
            return "redirect:/wap/student/list";
        }
        return "account/"
                + URLHelper.encodeUrlPathSegment(String.valueOf(user.getId()),
                        request);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html")
    public String login(@Valid User user, BindingResult bindingResult,
            Model uiModel, HttpServletRequest request) {
        LOG.debug("perform login.");
        if (bindingResult.hasErrors()) {

            uiModel.addAttribute("openId", request.getParameter("openId"));

            return "/wap/account/login";
        }
        uiModel.asMap().clear();
        Locale locale = localeResolver.resolveLocale(request);
        if (userDao != null) {
            User realUser = userDao.findByName(user.getUserName());
            if (null == realUser) {
                // user name not exist
                LOG.debug("User " + user.getUserName() + " does not exist.");
                populateEditForm(uiModel, user, messageSource.getMessage(
                        "login_user_not_exist",
                        new String[] { user.getUserName() }, locale));
                uiModel.addAttribute("openId", request.getParameter("openId"));
                return "/wap/account/login";
            } else if (!passwordMatch(user.getUserPassword(),
                    realUser.getUserPassword())) {
                LOG.debug("User " + user.getUserName() + ", password wrong.");
                populateEditForm(uiModel, user, messageSource.getMessage(
                        "login_wrong_password",
                        new String[] { user.getUserName() }, locale));
                uiModel.addAttribute("openId", request.getParameter("openId"));
                return "/wap/account/login";
            } else {

                String returnurl = request.getParameter("returnurl");
                uiModel.addAttribute("loginUser", realUser);
                UserRole role = roleDao.findById(realUser.getRoleID());
                LOG.debug("role is:" + role);
                if (role != null) {
                    uiModel.addAttribute("userRole", role);
                    String openId = request.getParameter("openId");
                    // 如果是老师和家长，绑定openid
                    if (StringUtils.isNotEmpty(openId)
                            && StringUtils.isEmpty(realUser.getOpenId())) {
                    }

                    if (!StringUtils.isEmpty(returnurl)) {
                        return "redirect:" + returnurl;
                    }
                }
            }
        }
        return "redirect:/wap/student/list";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "text/html")
    public String logout(Model uiModel, HttpServletRequest request) {
        LOG.debug("logout.");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("userRole");
        request.getSession().removeAttribute("cart");
        uiModel.asMap().clear();
        return "wap/account/login";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String accountMain(@PathVariable("id") String id, Model uiModel) {
        // uiModel.addAttribute("user", userDao.getUser(Integer.valueOf(id)));
        // uiModel.addAttribute("userId", id);
        return "account/userinfo";
    }

    private boolean passwordMatch(String pswd, String encryptedPswd) {
        if (null == pswd || "".equals(pswd)) {
            return false;
        } else if (!CipherHelper.encrypt(pswd).equals(encryptedPswd)) {
            return false;
        }
        return true;
    }

    private void populateEditForm(Model uiModel, User user, String message) {
        uiModel.addAttribute("errorMessage", message);
        uiModel.addAttribute("userName", user.getUserName());
        uiModel.addAttribute("password", user.getUserPassword());
    }

}
