/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author zhangle
 *
 */
public class AbstractBaseController {

    private static Log LOG = LogFactory.getLog(AbstractBaseController.class);

    @Autowired
    protected MessageSource msgSource;

    @Autowired
    private LocaleResolver localeResolver;

    /**
     * get localized message by message key and arguments
     * 
     * @param request
     * @param messageKey
     * @param args
     * @return
     */
    protected String getLocalizedMessage(HttpServletRequest request,
            String messageKey, String... args) {
        Locale locale = localeResolver.resolveLocale(request);
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        LOG.info("getting message, key:" + messageKey + ", locale:" + locale);
        return msgSource.getMessage(messageKey, args, locale);
    }
}
