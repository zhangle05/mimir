/**
 * 
 */
package com.jotunheim.mimir.web.interceptors;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author zhangle
 *
 */
public class LocalizationInterceptor extends HandlerInterceptorAdapter {
    private static Log LOG = LogFactory.getLog(LocalizationInterceptor.class);

    @Autowired
    protected MessageSource msgSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        LOG.info("LocalizationInterceptor: Pre-handle, setting message source for:"
                + request.getRequestURI());
        request.setAttribute("msgSource", msgSource);
        Locale locale = localeResolver.resolveLocale(request);
        if(locale == null) {
            locale = Locale.ENGLISH;
        }
        request.setAttribute("locale", locale);
        return true;
    }
}
