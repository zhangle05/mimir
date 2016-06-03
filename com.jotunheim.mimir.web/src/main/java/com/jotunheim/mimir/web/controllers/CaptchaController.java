/**
 * 
 */
package com.jotunheim.mimir.web.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jotunheim.mimir.web.utils.CaptchaUtils;
import com.jotunheim.mimir.web.utils.SharedConstants;

/**
 * @author zhangle
 *
 */
@RequestMapping("/captcha")
@Controller
public class CaptchaController {

    private static Log LOG = LogFactory.getLog(CaptchaController.class);
    private static final int CAPTCHA_LENGTH = 4;
    public static final String CAPTCHA_KEY = "captcha";
    private static final int CAPTCHA_IMG_WIDTH = 150;
    private static final int CAPTCHA_IMG_HEIGHT = 60;

    /**
     * 显示图形验证码
     *
     * @param p
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = "image/jpeg")
    public void displayCaptchaImage(HttpServletRequest request,
            HttpServletResponse response) {
        LOG.info("display captcha image");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成随机字串
        String verifyCode = CaptchaUtils.generateVerifyCode(CAPTCHA_LENGTH);
        // 存入会话session
        HttpSession session = request.getSession(true);
        session.setAttribute(CAPTCHA_KEY, verifyCode.toLowerCase());
        // 生成图片
        try {
            CaptchaUtils.outputImage(CAPTCHA_IMG_WIDTH, CAPTCHA_IMG_HEIGHT, response.getOutputStream(),
                    verifyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证图形验证码是否正确
     *
     * @param p
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String checkCaptchaCode(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "code", required = false) String code) {
        LOG.info("check captcha code, code is:" + code);
        JSONObject json = new JSONObject();
        if (code == null || "".equals(code)) {
            json.put("code", SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.put("msg", "code is empty!");
            return json.toString();
        }
        HttpSession session = request.getSession(true);
        String realCode = (String) session.getAttribute(CAPTCHA_KEY);
        if (!code.equals(realCode)) {
            json.put("code", SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            json.put("msg", "captcha code wrong!");
            return json.toString();
        }
        json.put("code", SharedConstants.AJAXCODE_OK);
        json.put("msg", "success!");
        return json.toString();
    }
}
