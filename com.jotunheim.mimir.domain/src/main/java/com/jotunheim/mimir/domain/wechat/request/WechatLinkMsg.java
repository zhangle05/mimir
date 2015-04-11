package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 链接消息
 * 
 * @author chengshengru
 *
 */
@XStreamAlias("xml")
public class WechatLinkMsg extends BaseWechatMsg {

	private String Title;
	private String Description;
	private String Url;

	
	
	
	public WechatLinkMsg() {
		MsgType=WX_MSG_TYPE_LINK;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	@Override
	public String toString() {
		return "WechatLinkMsg [Title=" + Title + ", Description=" + Description
				+ ", Url=" + Url + "]";
	}
	
	

}
