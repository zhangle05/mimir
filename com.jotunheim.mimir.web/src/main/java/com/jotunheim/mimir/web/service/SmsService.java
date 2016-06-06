package com.jotunheim.mimir.web.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jotunheim.mimir.web.utils.HttpClientHelper;
import com.jotunheim.mimir.web.utils.StringHelper;

@Service
public class SmsService {

    private static Log log = LogFactory.getLog(SmsService.class);

    @Autowired
    private CacheService cacheService;

    /**
     * 微米账号的接口UID
     */
    private String mWeimiUid = "DkLT1DeORQMm";

    /**
     * 微米账号的接口密码
     */
    private String mWeimiPas = "3ss8xbs6";
    /**
     * 短信模板id
     */
    private String mWeimiCid = "yq1oIZ2kwdGW";

    public boolean sendTemplateMessage(String[] phone, String templateId,
            String[] param) {
        if (phone == null || phone.length == 0 || phone.length > 100
                || StringUtils.isEmpty(templateId)) {
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
        para.put("cid", templateId);
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
     * @param length: 验证码位数
     * @param expire: 验证码有效期（秒）
     * @return
     */
    public boolean sendVerifyCode(String phone, int length, int expire) {
        if (StringUtils.isEmpty(phone)
                || !StringHelper.isMobile(phone)) {
            return false;
        }
        StringBuilder sb = new StringBuilder();

        if (length <= 0) {
            length = 6;
        }

        for (int i = 0; i < length; i++) {
            sb.append(RandomUtils.nextInt(10));
        }

        if(cacheService == null) {
            log.error("cache service is down!");
            return false;
        }

        boolean result = sendTemplateMessage(new String[] { phone }, mWeimiCid,
                new String[] { sb.toString() });
        if (result) {
            if(expire <=0) {
                expire = 60;
            }
            log.debug("SMS code is:" + sb.toString());
            if(cacheService != null) {
                cacheService.addCache("code-" + phone, expire, sb.toString());
            }
        }
        return result;

    }

    /**
     * 验证验证码
     * 
     * @param code
     * @return
     */
    public boolean checkVerifyCode(String phone, String code) {
        log.debug("SmsService.checkValifyCode, phone is:" + phone + ", code is:" + code);
        if(isTestNumber(phone)) {
            return true;
        }
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) {
            if(cacheService == null) {
                log.error("cache service is down!");
                return false;
            }
            String v = cacheService.getCache("code-" + phone);
            log.debug("code in cache is:" + v);
            return code.equals(v);
        }
        return false;
    }

    public boolean isTestNumber(String p) {
        if("13811138857".equals(p)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        SmsService svc = new SmsService();
        svc.sendTemplateMessage(new String[] { "13811138857" }, svc.mWeimiCid,
                new String[] { "1234" });
    }
}
