package com.jotunheim.mimir.domain.wechat.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatEventLocationMsg extends BaseEventMsg {

	private Double Latitude;
	
	private Double Longitude;
	
	private Double Precision;

	public WechatEventLocationMsg() {
		super();
		Event = WX_MSG_TYPE_EVENT_LOCATION;
	}

	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public Double getPrecision() {
		return Precision;
	}

	public void setPrecision(Double precision) {
		Precision = precision;
	}

}
