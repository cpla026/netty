package com.coolron.muxin.util;

/**
 * Created by Administrator on 2019/6/23.
 */
public enum MsgActionEnum {

    CONNECT(1,"第一次(或重连)初始化连接"),
    CHAT(2, "聊天消息"),
    SINGNED(3,"消息签收"),
    KEEPALIVE(4, "客户端保持心跳");

    public final Integer type;
    public final String content;

    MsgActionEnum(Integer type, String content){
        this.type = type;
        this.content = content;
    }
}
