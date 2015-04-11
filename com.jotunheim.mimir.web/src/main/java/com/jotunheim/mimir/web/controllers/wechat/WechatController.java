/**
 * 
 */
package com.jotunheim.mimir.web.controllers.wechat;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jotunheim.mimir.common.utils.EncoderHandler;
import com.jotunheim.mimir.common.utils.StringUtils;

/**
 * @author zhangle
 *
 */
@RequestMapping("/wechat")
@Controller
public class WechatController {
    private static Log LOG = LogFactory
            .getLog(WechatController.class);

    public static final String AppID = "wx00e970849009f9f6";
    public static final String AppSecret = "b4abfa1991d6014a0ea99d37eb495f88";
    public static final String WX_TOKEN = "5LyY5LyY5oiR54ix5L2g";

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String signature = request.getParameter("signature");
        String echostr = request.getParameter("echostr");
        LOG.debug("get api request");
        LOG.debug("timestamp is:" + timestamp + ", nonce is:" + nonce);
        if (StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce)) {
            return;
        }
        List<String> list = new ArrayList<String>();
        list.add(WX_TOKEN);
        list.add(nonce);
        list.add(timestamp);
        Collections.sort(list);
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
        }
        String str = EncoderHandler.encode("SHA1", builder.toString());
        LOG.debug("str is:" + str + ", signature is:" + signature);
        if (str.equals(signature)) {
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                writer.write(echostr);
                writer.flush();
            } catch (Exception e) {
                LOG.error("Handler Wechat Signature Request Error:", e);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        } else {
            LOG.error("Invalid Wechat Signature Request:"
                    + request.getRemoteAddr());
        }
    }

    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            //BaseMsg msg = MessageUtils.parseMsg(request);
            LOG.debug("Recieve Wechat Message:" + request);
            String result = "";
            //String result = wechatService.handleWechatMsg(msg);
            LOG.debug("Response Wechat Message:" + result);
            if (!StringUtils.isEmpty(result)) {
                writer = response.getWriter();
                writer.write(result);
                writer.flush();
            }
        } catch (Exception e) {
            LOG.error("Handler Wechat Request Error:", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignore) {
                }
            }
        }

    }
}
