package com.coolron.muxin.dao;

import com.coolron.muxin.domain.ChatMsg;

import java.util.List;

public interface ChatMsgMapper {
    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    void batchUpdateMsgSigned(List<String> msgIdList);
}