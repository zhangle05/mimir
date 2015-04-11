package com.jotunheim.mimir.web.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jotunheim.mimir.domain.wechat.BaseMsg;
import com.jotunheim.mimir.domain.wechat.request.BaseEventMsg;
import com.jotunheim.mimir.domain.wechat.request.BaseWechatMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatEventClickMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatEventLocationMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatEventPicMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatEventScanMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatEventSubscribeMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatEventTemplateFinish;
import com.jotunheim.mimir.domain.wechat.request.WechatEventViewMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatImageMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatLinkMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatLocationMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatTextMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatVideoMsg;
import com.jotunheim.mimir.domain.wechat.request.WechatVoiceMsg;
import com.jotunheim.mimir.domain.wechat.response.SendPicsInfo;

public class MessageUtils {

    /**
     * 解析微信发来的请求（XML）
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request)
            throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    public static BaseMsg parseMsg(HttpServletRequest request) throws Exception {
        Map<String, String> data = parseXml(request);
        BaseMsg msg = null;
        String msgType = data.get("MsgType");
        if (!BaseMsg.WX_MSG_TYPE_EVENT.equalsIgnoreCase(msgType)) {
            BaseWechatMsg wechat = null;
            if (BaseMsg.WX_MSG_TYPE_TEXT.equalsIgnoreCase(msgType)) {
                WechatTextMsg text = new WechatTextMsg();
                text.setContent(data.get("Content"));
                wechat = text;
            } else if (BaseMsg.WX_MSG_TYPE_IMAGE.equalsIgnoreCase(msgType)) {
                WechatImageMsg image = new WechatImageMsg();
                image.setPicUrl(data.get("PicUrl"));
                image.setMediaId(data.get("MediaId"));
                wechat = image;
            } else if (BaseMsg.WX_MSG_TYPE_VOICE.equalsIgnoreCase(msgType)) {
                WechatVoiceMsg voice = new WechatVoiceMsg();
                voice.setFormat(data.get("Format"));
                voice.setMediaId(data.get("MediaId"));
                voice.setRecognition(data.get("Recognition"));
                wechat = voice;
            } else if (BaseMsg.WX_MSG_TYPE_VIDEO.equalsIgnoreCase(msgType)) {
                WechatVideoMsg video = new WechatVideoMsg();
                video.setThumbMediaId(data.get("ThumbMediaId"));
                video.setMediaId(data.get("MediaId"));
                wechat = video;
            } else if (BaseMsg.WX_MSG_TYPE_LOCATION.equalsIgnoreCase(msgType)) {
                WechatLocationMsg location = new WechatLocationMsg();
                location.setLocation_X(Double.valueOf(data.get("Location_X")));
                location.setLocation_Y(Double.valueOf(data.get("Location_Y")));
                location.setScale(Integer.valueOf(data.get("Scale")));
                location.setLabel(data.get("Label"));
                wechat = location;

            } else if (BaseMsg.WX_MSG_TYPE_LINK.equalsIgnoreCase(msgType)) {
                WechatLinkMsg link = new WechatLinkMsg();
                link.setDescription(data.get("Description"));
                link.setTitle(data.get("Title"));
                link.setUrl(data.get("Url"));
                wechat = link;
            }
            if (wechat != null) {
                wechat.setMsgId(Long.valueOf(data.get("MsgId")));
            }
            msg = wechat;
        } else {
            String eventType = data.get("Event");

            if (BaseEventMsg.WX_MSG_TYPE_EVENT_SUBSCRIBE
                    .equalsIgnoreCase(eventType)
                    || BaseEventMsg.WX_MSG_TYPE_EVENT_UNSUBSCRIBE
                            .equalsIgnoreCase(eventType)) {
                WechatEventSubscribeMsg subscrible = new WechatEventSubscribeMsg();
                subscrible.setEvent(eventType);
                subscrible.setEventKey(data.get("EventKey"));
                subscrible.setTicket(data.get("Ticket"));
                msg = subscrible;
            } else if (BaseEventMsg.WX_MSG_TYPE_EVENT_CLICK
                    .equalsIgnoreCase(eventType)) {
                WechatEventClickMsg click = new WechatEventClickMsg();
                click.setEventKey(data.get("EventKey"));
                msg = click;
            } else if (BaseEventMsg.WX_MSG_TYPE_EVENT_LOCATION
                    .equalsIgnoreCase(eventType)) {
                WechatEventLocationMsg location = new WechatEventLocationMsg();
                location.setLatitude(Double.valueOf(data.get("Latitude")));
                location.setLongitude(Double.valueOf(data.get("Longitude")));
                location.setPrecision(Double.valueOf(data.get("Precision")));
                msg = location;
            } else if (BaseEventMsg.WX_MSG_TYPE_EVENT_SCAN
                    .equalsIgnoreCase(eventType)) {
                WechatEventScanMsg scan = new WechatEventScanMsg();
                scan.setEventKey(data.get("EventKey"));
                scan.setTicket(data.get("Ticket"));
                msg = scan;
            } else if (BaseEventMsg.WX_MSG_TYPE_EVENT_VIEW
                    .equalsIgnoreCase(eventType)) {
                WechatEventViewMsg view = new WechatEventViewMsg();
                view.setEventKey(data.get("EventKey"));
                msg = view;
            } else if (BaseEventMsg.WX_MSG_TYPE_EVENT_PIC_PHOTO_OR_ALBUM
                    .equalsIgnoreCase(eventType)
                    || BaseEventMsg.WX_MSG_TYPE_EVENT_PIC_SYSPHOTO
                            .equalsIgnoreCase(eventType)
                    || BaseEventMsg.WX_MSG_TYPE_EVENT_PIC_WEIXIN
                            .equalsIgnoreCase(eventType)) {

                WechatEventPicMsg pic = new WechatEventPicMsg();
                pic.setEvent(eventType);
                pic.setEventKey(data.get("EventKey"));
                try {
                    SendPicsInfo info = new SendPicsInfo();
                    info.setCount(Integer.valueOf(data.get("Count")));
                } catch (Exception e) {
                }
                msg = pic;
            } else if (BaseEventMsg.WX_MSF_TYPE_EVENT_TEMPLATE_JOBFINISH
                    .equalsIgnoreCase(eventType)) {
                WechatEventTemplateFinish template = new WechatEventTemplateFinish();
                template.setEvent(eventType);
                template.setStatus(data.get("Status"));
                msg = template;
            }
        }
        if (msg != null) {

            msg.setToUserName(data.get("ToUserName"));
            msg.setFromUserName(data.get("FromUserName"));
            msg.setCreateTime(data.get("CreateTime"));
        }
        return msg;
    }
}
