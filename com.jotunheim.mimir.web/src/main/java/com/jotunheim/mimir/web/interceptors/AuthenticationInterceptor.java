/**
 * 
 */
package com.jotunheim.mimir.web.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jotunheim.mimir.dao.UserRoleDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.web.service.AccountService;
import com.jotunheim.mimir.web.service.AccountService.LoginData;
import com.jotunheim.mimir.web.utils.CookieUtils;
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
}
