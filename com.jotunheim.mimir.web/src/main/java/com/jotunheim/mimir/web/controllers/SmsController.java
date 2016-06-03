package com.jotunheim.mimir.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jotunheim.mimir.dao.UserDao;
import com.jotunheim.mimir.domain.User;
import com.jotunheim.mimir.web.service.CacheService;
import com.jotunheim.mimir.web.service.SmsService;
import com.jotunheim.mimir.web.utils.SharedConstants;
import com.jotunheim.mimir.web.utils.StringHelper;

@RequestMapping("/sms")
@Controller
public class SmsController {

    private static Log LOG = LogFactory.getLog(SmsController.class);

    private static final int ACTION_REGISTER = 1;

    @Autowired
    private SmsService smsService;

    @Autowired
    private CacheService cacheUtils;

    @Autowired
    private UserDao userDao;

    /**
     * 发送手机验证码
     * 
     * @param p
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public @ResponseBody String sendSmsCode(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "p", required = true) String phoneNumber,
            @RequestParam(value = "captcha", required = true) String captchaCode,
            @RequestParam(value = "action", required = false) Integer action
            ) {
        LOG.info("send sms auth code, phone number is:" + phoneNumber + ", action is:" + action);
        if (action == null) {
            action = ACTION_REGISTER;
        }
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession(true);
        String realCode = (String) session.getAttribute(CaptchaController.CAPTCHA_KEY);
        if (!captchaCode.equals(realCode)) {
            result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            result.put(SharedConstants.AJAX_MSG_KEY, "图形验证码不正确");
            LOG.info("captcha code wrong.");
            return result.toString();
        }
        Long lastTime = cacheUtils.getCache(phoneNumber);
        long now = System.currentTimeMillis();
        if (ACTION_REGISTER == action) {
            // verify code for register, check existence of the mobile phone
            User u = userDao.findByMobile(phoneNumber);
            if (null != u) {
                result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                result.put(SharedConstants.AJAX_MSG_KEY, "手机号码已被使用");
                LOG.info("Find user by mobile failed.");
                return result.toString();
            }
        }
        LOG.info("checking cache, last time is:" + lastTime + ", now is:" + now);
        if (lastTime != null && now - lastTime < 60000) {  // do not re-send within one minute
            result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            result.put(SharedConstants.AJAX_MSG_KEY, "验证码发送中");
            LOG.info("SMS not send, do not re-send within one minute. result is:" + result);
        } else if (lastTime != null && now - lastTime < 3 * 60000) {
            // pretend to send. 
            // wait for the last one to reach the client with 3 minutes
            // The sending time may take several minutes due to network jam
            result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
            result.put(SharedConstants.AJAX_MSG_KEY, "success");
            LOG.info("Pretend to send, result is:" + result);
        } else {
            LOG.info("send SMS to:" + phoneNumber);
            if (StringUtils.isEmpty(phoneNumber)) {
                result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                result.put(SharedConstants.AJAX_MSG_KEY, "手机号不能为空");
            } else if (!StringHelper.isMobile(phoneNumber)) {
                result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                result.put(SharedConstants.AJAX_MSG_KEY, "手机号格式错误");
            }
            else {
                // check counts
                String countKey = phoneNumber + "_count";
                Integer count = cacheUtils.getCache(countKey);
                LOG.info("checking count cache, key is:" + countKey + ", count is:" + count);
                if (count == null) {
                    count = 0;
                }
                if(smsService.isTestNumber(phoneNumber)) {
                    LOG.info(phoneNumber + " is test phone, no verify code is sent, return OK directly.");
                    result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
                    result.put(SharedConstants.AJAX_MSG_KEY, "success");
                    return result.toString();
                }
                if (count >= 5 && !smsService.isTestNumber(phoneNumber)) {
                    result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                    result.put(SharedConstants.AJAX_MSG_KEY, "获取验证码次数过多，请24小时后重试");
                    LOG.info("Send count exceeds quota." + result);
                    return result.toString();
                }
                boolean isSuccess = smsService.sendVerifyCode(phoneNumber, 6, 180);
                if (isSuccess) {
                    count ++;
                    cacheUtils.delCache(phoneNumber);
                    cacheUtils.addCache(phoneNumber, now);
                    cacheUtils.delCache(countKey);
                    cacheUtils.addCache(countKey, 24 * 60 * 60, count);
                    session.setAttribute(CaptchaController.CAPTCHA_KEY, "");
                    result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
                    result.put(SharedConstants.AJAX_MSG_KEY, "success");
                    LOG.info("Send success, result is:" + result);
                } else {
                    result.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_SYSTEM_ERROR);
                    result.put(SharedConstants.AJAX_MSG_KEY, "发送失败");
                    LOG.info("Send SMS failed.");
                }
            }
        }
        return result.toString();
    }

    @RequestMapping(value = "/sms/check", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    private @ResponseBody String checkSmsCode(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "p", required = true) String phoneNumber,
            @RequestParam(value = SharedConstants.AJAX_CODE_KEY, required = true) String code
            ) {
        JSONObject obj = new JSONObject();
        LOG.debug("Check sms code, number is:" + phoneNumber + ", code is:" + code);
        if (StringHelper.isMobile(phoneNumber)
                && StringUtils.isNotEmpty(code)) {
            if(smsService.isTestNumber(phoneNumber)) {
                LOG.info(phoneNumber + " is test phone, return OK.");
                obj.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
                obj.put(SharedConstants.AJAX_MSG_KEY, "ok");
                return obj.toString();
            }
            boolean success = smsService.checkVerifyCode(phoneNumber, code);
            LOG.debug("Check sms code result:" + success);
            if (success) {
                obj.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_OK);
                obj.put(SharedConstants.AJAX_MSG_KEY, "ok");
            } else {
                obj.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
                obj.put(SharedConstants.AJAX_MSG_KEY, "验证码错误");
            }
        } else {
            obj.put(SharedConstants.AJAX_CODE_KEY, SharedConstants.AJAXCODE_CLIENT_DATA_ERROR);
            obj.put(SharedConstants.AJAX_MSG_KEY, "手机号格式错误");
        }
        LOG.debug("Check sms ready to return:" + obj);
        return obj.toString();
    }
}
