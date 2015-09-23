/**
 * 
 */
package com.jotunheim.mimir.web.interceptors;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.service.AccountService;
import com.jotunheim.mimir.web.service.AccountService.LoginData;
import com.jotunheim.mimir.web.utils.CookieUtils;
import com.jotunheim.mimir.web.utils.RoleAccessLevel;
import com.jotunheim.mimir.web.utils.SharedConstants;

/**
 * @author dannyzha
 *
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static Log LOG = LogFactory.getLog(AuthenticationInterceptor.class);

    @Autowired
    private UserRoleDao roleDao;

    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        LOG.info("AuthenticationInterceptor: Pre-handle, request URI is:"
                + request.getRequestURI());
        checkCookie(request, response);
        if (handler instanceof HandlerMethod) {
            HandlerMethod hmethod = (HandlerMethod) handler;
            if (!checkLogin(hmethod, request, response)) {
                return false;
            }
        }
        return true;
    }

    private void checkCookie(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            //判断是否有登录用户，无登录用户尝试从Cookie登录
            User user = (User)request.getSession().getAttribute("loginUser");
            if(user == null){
                Cookie cookie = cookieUtils.getCookie(request,
                        SharedConstants.USER_COOKIE_KEY);
                LOG.debug("cookie object is:" + cookie);
                if (cookie != null) {
                    String userCookie = cookie.getValue();
                    LoginData ld = accountService.loginByCookie(userCookie);
                    if(ld.statusCode == AccountService.ACCOUNT_LOGIN_SUCCEED) {
                        user = ld.realUser;
                        request.getSession().setAttribute("loginUser", user);
                        // renew the cookie
                        cookieUtils.removeCookie(response,
                                SharedConstants.USER_COOKIE_KEY);
                        cookieUtils.setCookie(response,
                                SharedConstants.USER_COOKIE_KEY, userCookie,
                                SharedConstants.COOKIE_EXPIRE_TIME);
                    }
                }
            }
            UserRole role = (UserRole)request.getSession().getAttribute("userRole");
            if(role == null && user != null){
                role = roleDao.findById(user.getRoleID());
                request.getSession().setAttribute("userRole", role);
            }

        } catch (Exception ex) {
            LOG.error("read user from cookie failed. error is:"
                    + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean checkLogin(HandlerMethod hmethod,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Method method = hmethod.getMethod();
        // 用户登录
        Login login = hmethod.getBean().getClass().getAnnotation(Login.class);
        if (login == null || !login.required()) {
            login = method.getAnnotation(Login.class);
        }
        if (login != null && login.required()) {
            LOG.info("login required.");
            User user = (User) request.getSession().getAttribute("loginUser");
            boolean isWechat = (Boolean) request.getAttribute("isWechat");
            boolean isMobile = (Boolean) request.getAttribute("isMobile");
            boolean isJson = method.getAnnotation(ResponseBody.class) != null;
            String partner = (String) request.getAttribute("partner");
            if (user == null) {
                LOG.info("AuthenticationInterceptor: recirecting "
                        + request.getRequestURI() + " to /account/login");
                LOG.debug("###request uri is:" + request.getRequestURI());
                if (isWechat) {
                    //user = autoLoginWithOpenID(request);
                }
                if (user == null) {
                    error400(request, response, isMobile, isJson, partner);
                    return false;
                }
            } else {
                LOG.debug("user is:" + user.getUserNickName());
                int pageLevel = login.role();
                UserRole role = roleDao.findById(user.getRoleID());
                LOG.debug("role is:" + role + ", level is:"
                        + role.getAccessLevel());
                if (role != null) {
                    request.getSession().setAttribute("userRole", role);
                }
                LOG.debug("page accessLevel is:" + pageLevel
                        + ", role accessLevel is:" + role.getAccessLevel());
                if (pageLevel > role.getAccessLevel()) {
                    error401(role, response, isMobile, isJson);
                    return false;
                }
            }
        }
        LOG.info("AuthenticationInterceptor: Pass the interceptor, no recirection.");
        return true;
    }

    private void error400(HttpServletRequest request, HttpServletResponse response, boolean isMobile,
            boolean isJson, String partner) throws IOException {
        // user in session is empty
        StringBuilder builder = new StringBuilder(request
                .getRequestURL().toString());
        String qs = request.getQueryString();
        if (!StringUtils.isEmpty(qs)) {
            builder.append("?");
            builder.append(qs);
        }
        if (isJson) {
            String redirctUrl = "/wap/account/login";
            JSONObject obj = new JSONObject();
            obj.put("code", 400);
            obj.put("msg", "Invalidate Request, please login.");
            if (!isMobile) {
                redirctUrl = "/account/login";
            }
            obj.put("url", redirctUrl);
            PrintWriter write = response.getWriter();
            write.write(obj.toString());
            write.flush();
        } else {
            String returnurl = URLEncoder.encode(
                    builder.toString(), "UTF-8");
            LOG.debug(returnurl);
            if (isMobile) {
                response.sendRedirect(String
                        .format("/wap/account/login?partner=%s&returnurl=%s",
                                partner, returnurl));
            } else {
                response.sendRedirect(String.format(
                        "/account/login?partner=%s&returnurl=%s",
                        partner, returnurl));
            }
        }
    }

    private void error401(UserRole role, HttpServletResponse response, boolean isMobile,
            boolean isJson) throws IOException {
        // user level smaller than the page level
        String redirctUrl = "/";
        if (isMobile) {
            switch (role.getAccessLevel()) {
            case RoleAccessLevel.ADMIN:
                break;
            case RoleAccessLevel.SUPERVISOR:
                break;
            case RoleAccessLevel.USER:
                redirctUrl = "/wap";
                break;
            default:
                break;
            }
        } else {
            switch (role.getAccessLevel()) {
            case RoleAccessLevel.ADMIN:
                break;
            case RoleAccessLevel.SUPERVISOR:
                break;
            case RoleAccessLevel.USER:
                redirctUrl = "/";
                break;
            default:
                break;
            }
        }
        if (isJson) {
            JSONObject obj = new JSONObject();
            obj.put("code", 401);
            obj.put("msg", "Unauthorized Request");
            obj.put("url", redirctUrl);
            PrintWriter write = response.getWriter();
            write.write(obj.toString());
            write.flush();
        } else {
            response.sendRedirect(redirctUrl);
        }
    }
}
