package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 图片消息
 * 
 * @author chengshengru
 *
 */
@XStreamAlias("xml")
public class WechatImageMsg extends BaseWechatMsg {

	private String PicUrl;

	private String MediaId;

	public WechatImageMsg() {
		MsgType = WX_MSG_TYPE_IMAGE;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	@Override
	public String toString() {
		return "WechatImageMsg [PicUrl=" + PicUrl + ", MediaId=" + MediaId
				+ "]";
	}

}
