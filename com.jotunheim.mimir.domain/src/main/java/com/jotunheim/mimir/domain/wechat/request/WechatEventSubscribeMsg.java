package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatEventSubscribeMsg extends BaseEventMsg {
	
	private String EventKey;

	private String Ticket;

	public boolean isSubscribe(){
		return BaseEventMsg.WX_MSG_TYPE_EVENT_SUBSCRIBE.equalsIgnoreCase(Event);
	}



	@Override
	public String toString() {
		return "WechatEventSubscribeMsg [EventKey=" + EventKey + ", Ticket="
				+ Ticket + ", Event=" + Event + ", ToUserName=" + ToUserName
				+ ", FromUserName=" + FromUserName + ", CreateTime="
				+ CreateTime + ", MsgType=" + MsgType + "]";
	}



	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}


	
	
	
	
}
