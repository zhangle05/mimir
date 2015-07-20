/**
 * 
 */
package com.jotunheim.mimir.web.ajax;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.jotunheim.mimir.web.utils.CookieUtils;

/**
 * @author zhangle
 *
 */
@Controller
@RequestMapping("/settings")
@SessionAttributes({ "locale" })
public class SettingsController {
    private static Log LOG = LogFactory.getLog(SettingsController.class);

    @Autowired
    private CookieUtils cookieUtils;

    @Autowired
    private LocaleResolver localeResolver;

    /**
     * Constructor
     */
    public SettingsController() {
        LOG.debug("Creating SettingsController.");
    }

    @RequestMapping("/lang/{locale}")
    public @ResponseBody String changeLocale(Model uiModel, HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable(value = "locale") String localeStr) {
        LOG.debug("SettingsController.changeLocale");
        JSONObject json = new JSONObject();
        Locale locale = null;
        if("zh".equals(localeStr)) {
            locale = Locale.CHINESE;
        }
        else {
            locale = Locale.ENGLISH;
        }
        localeResolver.setLocale(request, response, locale);
        json.accumulate("result", "1");
        json.accumulate("msg", "ok");
        return json.toString();
    }

    @RequestMapping("/check/conn")
    public @ResponseBody String checkConn(Model uiModel, HttpServletRequest request,
            HttpServletResponse response) {
        LOG.debug("SettingsController.checkConn");
        JSONObject json = new JSONObject();
        json.accumulate("result", "1");
        json.accumulate("msg", "ok");
        return json.toString();
    }
}
