package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatEventClickMsg extends BaseEventMsg {

    private String EventKey;

    public WechatEventClickMsg() {
        super();
        Event = WX_MSG_TYPE_EVENT_CLICK;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

}
