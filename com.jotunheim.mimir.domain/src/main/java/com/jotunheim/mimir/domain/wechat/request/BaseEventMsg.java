package com.jotunheim.mimir.domain.wechat.request;

import com.jotunheim.mimir.domain.wechat.BaseMsg;


public abstract class BaseEventMsg extends BaseMsg {

	/**
	 * 用户关注
	 */
	public static final String WX_MSG_TYPE_EVENT_SUBSCRIBE = "subscribe";

	/**
	 * 用户取消关注
	 */
	public static final String WX_MSG_TYPE_EVENT_UNSUBSCRIBE = "unsubscribe";
	/**
	 * 扫描事件
	 */
	public static final String WX_MSG_TYPE_EVENT_SCAN = "scan";

	/**
	 * 上报地理位置事件
	 */
	public static final String WX_MSG_TYPE_EVENT_LOCATION = "location";
	/**
	 * 自定义菜单事件
	 */
	public static final String WX_MSG_TYPE_EVENT_CLICK = "click";

	/**
	 * 点击菜单跳转链接时的事件推送
	 */
	public static final String WX_MSG_TYPE_EVENT_VIEW = "view";

	/**
	 * 弹出系统拍照发图的事件推送
	 */
	public static final String WX_MSG_TYPE_EVENT_PIC_SYSPHOTO ="pic_sysphoto";
	
	/**
	 * 弹出拍照或者相册发图的事件推送
	 */
	public static final String WX_MSG_TYPE_EVENT_PIC_PHOTO_OR_ALBUM="pic_photo_or_album";
	
	
	public static final String WX_MSG_TYPE_EVENT_PIC_WEIXIN = "pic_weixin";
	
	public static final String WX_MSF_TYPE_EVENT_TEMPLATE_JOBFINISH="TEMPLATESENDJOBFINISH";
	
	protected String Event;
	
	public BaseEventMsg() {
		MsgType = WX_MSG_TYPE_EVENT;
	}
	
	
	

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}
	
	
	
}
