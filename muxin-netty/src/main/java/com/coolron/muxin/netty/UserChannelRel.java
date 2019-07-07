package com.coolron.muxin.netty;/**
 * Created by Administrator on 2019/6/23.
 */

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: xf
 * @Date: 2019/6/23 21:51
 * @Description: 用户id 和 channel 的关联关系处理
 *
 */
public class UserChannelRel {

    private static HashMap<String, Channel> manager = new HashMap<>();

    public static void put(String senderId, Channel channel){
        manager.put(senderId, channel);
    }
    public static Channel get(String senderId){
        return manager.get(senderId);
    }

    /**
     * 用于测试
     */
    public static void outPut(){
        for (Map.Entry<String,Channel> entry : manager.entrySet()) {
            System.out.println("UserId: " + entry.getKey()
                    + ", ChannelId: " + entry.getValue().id().asLongText());
        }
    }

}
