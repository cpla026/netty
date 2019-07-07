package com.coolron.muxin.netty;/**
 * Created by Administrator on 2019/6/22.
 */

import com.alibaba.fastjson.JSON;
import com.coolron.muxin.service.UserServiceImpl;
import com.coolron.muxin.util.MsgActionEnum;
import com.coolron.muxin.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: xf
 * @Date: 2019/6/22 22:21
 * @Description: 处理消息的Handler
 * 
 * TextWebSocketFrame：在netty中，用于为websocket专门处理文本的对象，frame是消息的载体
 *
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的channel
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) 
            throws Exception {

        // 获取客户端传输过来的消息
        String content = msg.text();
        System.out.println("接受到数据：" + content);

        Channel currentChannel = channelHandlerContext.channel();

        /*
         * 1、获取客户端发送来的消息
         * 2、判断消息类型，根据不同的类型来处理不同的业务
         *   2.1、当websocket 第一次open的时候，初始化channel，把用户的channel 和 userID 关联起来
         *   2.2、聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态【未签收】
         *   2.3、签收消息类型，针对具体的消息签收，修改数据库中对应消息的签收状态【已签收】
         *   2.4、心跳类型的消息，
         */
        // 1、
        DataContent dataContent = JSON.parseObject(content, DataContent.class);
        // 2
        Integer action = dataContent.getAction();
        if(action == MsgActionEnum.CONNECT.type){
            // 2.1、
            String senderId = dataContent.getChatMsg().getSenderId();
            UserChannelRel.put(senderId, currentChannel);

            /*********************** 测试 ***********************/
            for (Channel channel : users){
                System.out.println(channel.id().asLongText());
            }
            UserChannelRel.outPut();
            /*********************** 测试 ***********************/

        } else if(action == MsgActionEnum.CHAT.type){
            // 2.2、
            ChatMsg chatMsg = dataContent.getChatMsg();
            String msgTest = chatMsg.getMsg();
            String receiverId = chatMsg.getReceiverId();
            String senderId = chatMsg.getSenderId();
            // 保存消息到数据库，标记未签收
            UserServiceImpl userService = (UserServiceImpl)SpringUtil.getBean("userServiceImpl");
            String msgId = userService.saveMsg(chatMsg);

            chatMsg.setMsgId(msgId);
            // 发送消息
            // 从全局用户Channel关系中获取接收方的channel
            Channel receiverChannel = UserChannelRel.get(receiverId);
            if(null == receiverChannel){
                // receiverChannel 为null 代表用户离线，推送消息(不是netty推送，第三方推送：JPush，个推，小米推送)
            }else {
                // 当 receiverChannel 不为空的时候，从 ChannelGroup 中查找对应的channel是否存在
                Channel findChannel = users.find(receiverChannel.id());
                if(null != findChannel){
                    // 用户在线 阿静消息推送到前端
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMsg)));
                } else {
                    // 用户离线 推送消息

                }
            }

        } else if(action == MsgActionEnum.SINGNED.type){
            // 2.3、
            UserServiceImpl userService = (UserServiceImpl)SpringUtil.getBean("userServiceImpl");
            // 扩展字段在signed 类型的消息中，代表需要去签收的消息id，逗号间隔
            String msgIdStr = dataContent.getExtand();
            String[] msgIds = msgIdStr.split(",");

            List<String> msgIdList = new ArrayList<>();
            for (String mid : msgIds) {
               if(StringUtils.isNotBlank(mid)){
                   msgIdList.add(mid);
               }
            }
            System.out.println(msgIdList.toString());

            if(null != msgIdList && !msgIdList.isEmpty() && msgIdList.size() > 0){
                // 批量签收
                userService.updateMsgSigned(msgIdList);

            }

        } else if(action == MsgActionEnum.KEEPALIVE.type){
            // 2.4、心跳

        }


        /************************** 简单实验 **********************************/
        /* for (Channel channel : users) {
            channel.writeAndFlush(
                    new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到的消息：" + content)
            );
        }

        // 和上面的方法效果一致
        //users.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到的消息：" + content));
    */
    }

    /**
     * 当客户端连接服务器之后 (打开连接)
     * 获取客户端的channel  并且放到 ChannelGroup 中进行管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        users.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发HandlerRemoved , ChannelGroup 会自动移除对应客户端的channel
        // users.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id：" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id：" + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常后关闭连接（关闭channel），随后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());

    }
}
