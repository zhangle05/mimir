package com.jotunheim.mimir.domain.wechat.response;

import com.jotunheim.mimir.domain.wechat.BaseMsg;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 转接到多客服系统
 * @author chengshengru
 *
 */
@XStreamAlias("xml")
public class WechatTransferMsg extends BaseMsg {

	public WechatTransferMsg() {
		MsgType=WX_MSG_TYPE_TRANSFER;
	}

	
}
