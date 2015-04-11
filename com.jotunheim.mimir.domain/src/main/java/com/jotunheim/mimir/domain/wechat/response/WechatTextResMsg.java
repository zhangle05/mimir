package com.jotunheim.mimir.domain.wechat.response;

import com.jotunheim.mimir.domain.wechat.BaseMsg;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatTextResMsg extends BaseMsg {

	private String Content;

	public WechatTextResMsg() {
		super();
		MsgType = WX_MSG_TYPE_TEXT;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
