package com.coolron.muxin.netty;/**
 * Created by Administrator on 2019/6/23.
 */

import java.io.Serializable;

/**
 * @Auther: xf
 * @Date: 2019/6/23 21:25
 * @Description:
 */
public class ChatMsg implements Serializable {

    private String senderId;   // 发送者用户id
    private String receiverId; // 接收者用户id
    private String msg;         // 聊天内容
    private String msgId;       // 用于消息的签收

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
