package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 文本消息
 * @author chengshengru
 *
 */
@XStreamAlias("xml")
public class WechatTextMsg extends BaseWechatMsg {
	
	private String Content;

	
	public WechatTextMsg() {
		MsgType=WX_MSG_TYPE_TEXT;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	@Override
	public String toString() {
		return "WechatTextMsg [Content=" + Content + "]";
	}
	
	
}
