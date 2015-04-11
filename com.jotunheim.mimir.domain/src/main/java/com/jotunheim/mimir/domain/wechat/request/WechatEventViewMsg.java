package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("xml")
public class WechatEventViewMsg extends BaseEventMsg {

	private String EventKey;

	public WechatEventViewMsg() {
		super();
		Event = WX_MSG_TYPE_EVENT_VIEW;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

}
