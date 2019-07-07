package com.coolron.muxin.domain;

import java.util.Date;

public class FriendsRequest {
    private String id;

    private String sendUserId;

    private String acceptUserId;

    private Date requestDataTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId == null ? null : sendUserId.trim();
    }

    public String getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId == null ? null : acceptUserId.trim();
    }

    public Date getRequestDataTime() {
        return requestDataTime;
    }

    public void setRequestDataTime(Date requestDataTime) {
        this.requestDataTime = requestDataTime;
    }
}