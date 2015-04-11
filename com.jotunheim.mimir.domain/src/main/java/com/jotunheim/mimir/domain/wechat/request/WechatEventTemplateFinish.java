package com.jotunheim.mimir.domain.wechat.request;

import org.apache.commons.lang.StringUtils;

/**
 * 模板消息推送
 * @author chengshengru
 *
 */
public class WechatEventTemplateFinish extends BaseEventMsg {

	
	private String Status;
	
	private Long MsgID;

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Long getMsgID() {
		return MsgID;
	}

	public void setMsgID(Long msgID) {
		MsgID = msgID;
	}

	public WechatEventTemplateFinish() {
		Event = WX_MSF_TYPE_EVENT_TEMPLATE_JOBFINISH;
	}
	
	
	public int getStatusCode(){
		if(StringUtils.isNotEmpty(Status)){
			if(Status.contains("success")){ //发送成功
				return 1;
			}else if(Status.contains("block")){ //用户拒绝
				return 2;
			}else if(Status.contains("failed")){ //微信服务器错误
				return 3;
			}
		}
		return 0;
	}
	
}
