package com.coolron.muxin.netty;/**
 * Created by Administrator on 2019/6/23.
 */

import java.io.Serializable;

/**
 * @Auther: xf
 * @Date: 2019/6/23 21:21
 * @Description: 消息实体类
 *
 */
public class DataContent implements Serializable{

    private Integer action;     // 动作类型
    private ChatMsg chatMsg;    // 用户的聊天内容entity
    private String extand;      // 扩展字段

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public ChatMsg getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(ChatMsg chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getExtand() {
        return extand;
    }

    public void setExtand(String extand) {
        this.extand = extand;
    }
}
