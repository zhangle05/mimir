/**
 * 
 */
package com.jotunheim.mimir.web.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.domain.UserRole;
import com.jotunheim.mimir.web.annotation.Login;
import com.jotunheim.mimir.web.utils.RoleAccessLevel;

/**
 * @author zhangle
 *
 */

@RequestMapping("/supervisor")
@Controller
@Login(role = RoleAccessLevel.SUPERVISOR)
public class SupervisorController {
    private static Log LOG = LogFactory
            .getLog(SupervisorController.class);

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "text/html")
    public String supervisorHome(Model uiModel, HttpServletRequest request) {
        LOG.debug("supervisor home.");
        return "redirect:/supervisor/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = "text/html")
    public String adminIndex(Model uiModel, HttpServletRequest request) {
        LOG.debug("create login form.");
        User user = (User) request.getSession().getAttribute("loginUser");
        LOG.debug("user is:" + user);
        UserRole role = (UserRole) request.getSession().getAttribute("userRole");
        LOG.debug("role is:" + role);
        if(role == null || role.getAccessLevel() < RoleAccessLevel.ADMIN) {
            return "redirect:/account/login";
        }
        uiModel.addAttribute("user", user);
        return "supervisor/index";
    }
}
