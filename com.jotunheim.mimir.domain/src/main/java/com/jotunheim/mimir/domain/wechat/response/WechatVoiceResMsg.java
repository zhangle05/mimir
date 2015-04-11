package com.jotunheim.mimir.domain.wechat.response;

import com.jotunheim.mimir.domain.wechat.BaseMsg;

public class WechatVoiceResMsg extends BaseMsg {

    private Voice Voice;

    public WechatVoiceResMsg() {
        super();
        MsgType = WX_MSG_TYPE_VOICE;
    }

    public Voice getVoice() {
        return Voice;
    }

    public void setVoice(Voice voice) {
        Voice = voice;
    }

}
