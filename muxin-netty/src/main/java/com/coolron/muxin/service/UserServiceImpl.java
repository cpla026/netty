package com.coolron.muxin.service;/**
 * Created by Administrator on 2019/6/23.
 */

import com.coolron.muxin.dao.ChatMsgMapper;
import com.coolron.muxin.netty.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: xf
 * @Date: 2019/6/23 22:14
 * @Description:
 */
@Service
public class UserServiceImpl {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    /**
     * 保存聊天消息到数据库
     *
     * @param chatMsg
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String saveMsg(ChatMsg chatMsg) {
        com.coolron.muxin.domain.ChatMsg msgDB = new com.coolron.muxin.domain.ChatMsg();
        msgDB.setMsg(chatMsg.getMsg());
        msgDB.setCreateTime(new Date());
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setSignFlag(0);
        msgDB.setId(UUID.randomUUID().toString());

        chatMsgMapper.insert(msgDB);

        return msgDB.getId();
    }

    /**
     * 批量签收消息
     * @param msgIdList
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMsgSigned(List<String> msgIdList) {
        chatMsgMapper.batchUpdateMsgSigned(msgIdList);
    }
}
