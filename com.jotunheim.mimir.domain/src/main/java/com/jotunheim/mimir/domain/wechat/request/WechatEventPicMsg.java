package com.jotunheim.mimir.domain.wechat.request;

import java.io.Serializable;

import com.jotunheim.mimir.domain.wechat.response.SendPicsInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatEventPicMsg extends BaseEventMsg implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String EventKey;

    private SendPicsInfo SendPicsInfo;

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public SendPicsInfo getSendPicsInfo() {
        return SendPicsInfo;
    }

    public void setSendPicsInfo(SendPicsInfo sendPicsInfo) {
        SendPicsInfo = sendPicsInfo;
    }

    @Override
    public String toString() {
        return "WechatEventPicMsg [EventKey=" + EventKey + ", SendPicsInfo="
                + SendPicsInfo + "]";
    }

}
