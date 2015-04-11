package com.jotunheim.mimir.web.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.jotunheim.mimir.web.utils.HttpClientHelper;

@Service
public class SmsService {

    private static Log log = LogFactory.getLog(SmsService.class);

    /**
     * 微米账号的接口UID
     */
    private String mWeimiUid = "acsPaFSGCwvN";

    /**
     * 微米账号的接口密码
     */
    private String mWeimiPas = "fuugwsb4";

    private String mWeimiCid = "lSPPPUHavvQb";

    private String mWeimiFindPasCid = "gIzbdGbbfFiN";

    public boolean sendTemplateMessage(String[] phone, String cid,
            String[] param) {
        if (phone == null || phone.length == 0 || phone.length > 100
                || StringUtils.isEmpty(cid)) {
            return false;
        }

        Map<String, String> para = new HashMap<String, String>();

        para.put("mob", StringUtils.join(phone, ','));

        para.put("uid", mWeimiUid);

        para.put("pas", mWeimiPas);

        /**
         * 接口返回类型：json、xml、txt。默认值为txt
         */
        para.put("type", "json");
        para.put("cid", cid);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                para.put("p" + (i + 1), param[i]);
            }
        }

        try {

            String result = HttpClientHelper.convertStreamToString(
                    HttpClientHelper.post(
                            "http://api.weimi.cc/2/sms/send.html", para),
                    "UTF-8");
            JSONObject res = JSONObject.fromObject(result);
            if (res != null) {
                int code = res.optInt("code", -1);
                if (code == 0) {
                    return true;
                } else {
                    log.error("Send Template Message Error:" + res.toString());
                }

            }
        } catch (Exception e) {
            log.error("Send Template Message Error", e);
        }
        return false;
    }

    public boolean sendMessage(String[] phone, String content) {

        if (phone == null || phone.length == 0 || StringUtils.isEmpty(content)) {
            return false;
        }

        Map<String, String> para = new HashMap<String, String>();

        para.put("mob", StringUtils.join(phone, ','));

        para.put("uid", mWeimiUid);

        para.put("pas", mWeimiPas);

        /**
         * 接口返回类型：json、xml、txt。默认值为txt
         */
        para.put("type", "json");

        para.put("con", content);

        try {

            String result = HttpClientHelper.convertStreamToString(
                    HttpClientHelper.post(
                            "http://api.weimi.cc/2/sms/send.html", para),
                    "UTF-8");
            JSONObject res = JSONObject.fromObject(result);
            if (res != null) {
                int code = res.optInt("code", -1);
                if (code == 0) {
                    return true;
                } else {
                    log.error("Send  Message Error:" + res.toString());
                }

            }
        } catch (Exception e) {
            log.error("Send Message Error", e);
        }

        return false;
    }

    /**
     * 发送验证码
     * 
     * @param length
     * @param expire
     * @return
     */
    public boolean sendValifyCode(HttpSession session, String phone,
            int length, int expire) {
        if (StringUtils.isEmpty(phone)
                || !com.jotunheim.mimir.common.utils.StringUtils.isMobile(phone)) {
            return false;
        }
        StringBuilder sb = new StringBuilder();

        if (length <= 0) {
            length = 6;
        }

        for (int i = 0; i < length; i++) {
            sb.append(RandomUtils.nextInt(10));
        }

        boolean result = sendTemplateMessage(new String[] { phone }, mWeimiCid,
                new String[] { sb.toString() });
        if (result) {
            session.setAttribute("code-" + phone, sb.toString());
        }
        return result;

    }

    /**
     * 验证验证码
     * 
     * @param code
     * @return
     */
    public boolean checkValifyCode(HttpSession session, String phone,
            String code) {
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) {
            String v = (String) session.getAttribute("code-" + phone);
            return code.equals(v);
        }
        return false;
    }

    /**
     * 发送密码
     * 
     * @param phone
     * @param pwd
     * @return
     */
    public boolean sendPwd(String phone, String pwd) {

        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(pwd)) {

            boolean result = sendTemplateMessage(new String[] { phone },
                    mWeimiFindPasCid, new String[] { pwd });
            return result;
        }
        return false;

    }
}
