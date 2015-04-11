package com.jotunheim.mimir.domain.wechat.response;

import com.jotunheim.mimir.domain.wechat.BaseMsg;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WechatImageResMsg extends BaseMsg {

    private Image Image;

    public WechatImageResMsg() {
        super();
        MsgType = WX_MSG_TYPE_IMAGE;
    }

    public Image getImage() {
        return Image;
    }

    public void setImage(Image image) {
        this.Image = image;
    }

}
