/**
 * 
 */
package com.jotunheim.mimir.web.controllers.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jotunheim.mimir.domain.User;

/**
 * @author zhangle
 *
 */
@RequestMapping("/wxauth")
@Controller
public class AuthCallbackController {
    private static Log LOG = LogFactory
            .getLog(AuthCallbackController.class);

    @RequestMapping(value = "/authed", method = RequestMethod.GET, produces = "text/html")
    public String authedGet(@Valid User user, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        LOG.info("AuthCallbackController: authorized.");
        return "wechat/authorized";
    }

    @RequestMapping(value = "/authed", method = RequestMethod.POST, produces = "text/html")
    public String authedPost(@Valid User user, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        LOG.info("AuthCallbackController: authorized.");
        return "wechat/authorized";
    }
}
