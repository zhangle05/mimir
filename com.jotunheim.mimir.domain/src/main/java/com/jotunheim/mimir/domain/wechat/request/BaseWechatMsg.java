package com.jotunheim.mimir.domain.wechat.request;

import com.jotunheim.mimir.domain.wechat.BaseMsg;


public abstract class BaseWechatMsg extends BaseMsg{

	private Long MsgId;

	public Long getMsgId() {
		return MsgId;
	}

	public void setMsgId(Long msgId) {
		MsgId = msgId;
	}

}
