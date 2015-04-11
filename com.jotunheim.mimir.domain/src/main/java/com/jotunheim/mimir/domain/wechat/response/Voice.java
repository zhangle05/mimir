package com.jotunheim.mimir.domain.wechat.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Voice")
public class Voice {

	public String MediaId;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

}
