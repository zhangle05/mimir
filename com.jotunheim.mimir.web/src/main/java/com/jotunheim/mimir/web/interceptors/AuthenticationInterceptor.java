/**
 * 
 */
package com.jotunheim.mimir.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author dannyzha
 *
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static Log LOG = LogFactory.getLog(AuthenticationInterceptor.class);

    public static final String OPEN_ID_ENCYPT_KEY = "5bbe32495d6fa2e5";

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        LOG.info("AuthenticationInterceptor: Pre-handle, request URI is:"
                + request.getRequestURI());
        return true;
//        String openId = request.getParameter("openId");
//
//        if (StringUtils.isNotEmpty(openId)) {
//            /**
//             * 登录是添加from字段，防止OpenId 正确时，还对其进行无效解密
//             */
//            String from = request.getParameter("from");
//            if (StringUtils.isEmpty(from)) {
//                try {
//                    openId = AESUtils.Decrypt(openId, OPEN_ID_ENCYPT_KEY);
//                    request.setAttribute("openId", openId);
//                } catch (Exception e) {
//                    openId = "";
//                    LOG.info("AuthenticationInterceptor: Decode OpenId Error, request URI is:"
//                            + request.getRequestURI());
//                }
//            }
//        }
//        String userAgent = request.getHeader("User-Agent");
//
//        if (userAgent.toLowerCase().indexOf("micromessenger") != -1) {
//            request.setAttribute("isWechat", true);
//        } else {
//            request.setAttribute("isWechat", false);
//        }
//
//        // Avoid a redirect loop for some urls
//        if (!request.getRequestURI().equals("/users/create")
//                && !request.getRequestURI().equals("/account/login")
//                && !request.getRequestURI().equals("/wap/account/login") // 移动端登录
//                && !request.getRequestURI().equals("/uncaughtException")
//                && !request.getRequestURI().equals("/resourceNotFound")
//                && !request.getRequestURI().equals("/dataAccessFailure")
//                && !request.getRequestURI().equals("/home")
//                && !request.getRequestURI().startsWith("/ajax")) {
//            User user = (User) request.getSession().getAttribute("loginUser");
//            if (user == null) {
//                LOG.info("AuthenticationInterceptor: recirecting "
//                        + request.getRequestURI() + " to /account/login");
//
//                StringBuilder builder = new StringBuilder(request
//                        .getRequestURL().toString());
//                builder.append("?");
//                @SuppressWarnings("unchecked")
//                Enumeration<String> enumeration = request.getParameterNames();
//                String name = null;
//                while (enumeration.hasMoreElements()) {
//                    name = (String) enumeration.nextElement();
//                    builder.append(name);
//                    builder.append("=");
//                    builder.append(request.getParameter(name));
//                    builder.append("&");
//                }
//
//                String returnurl = URLEncoder.encode(builder.toString(),
//                        "UTF-8");
//                LOG.debug(returnurl);
//
//                if (userAgent.indexOf("Mobile") > -1) {
//                    response.sendRedirect(String.format(
//                            "/wap/account/login?returnurl=%s", returnurl));
//
//                } else {
//                    response.sendRedirect(String.format(
//                            "/account/login?returnurl=%s", returnurl));
//                }
//                return false;
//            }
//        }
//        LOG.info("AuthenticationInterceptor: Pass the interceptor, no recirection.");
//        return true;
    }
}
