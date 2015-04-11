package com.jotunheim.mimir.domain.wechat.response;

import com.jotunheim.mimir.domain.wechat.BaseMsg;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatVideoResMsg extends BaseMsg {

    private Video Video;

    public WechatVideoResMsg() {
        super();
        MsgType = WX_MSG_TYPE_VIDEO;
    }

    public Video getVideo() {
        return Video;
    }

    public void setVideo(Video video) {
        Video = video;
    }

}
