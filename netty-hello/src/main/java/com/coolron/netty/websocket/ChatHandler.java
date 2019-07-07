package com.coolron.netty.websocket;/**
 * Created by Administrator on 2019/6/22.
 */

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

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
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) 
            throws Exception {

        String content = msg.text();
        System.out.println("接受到数据：" + content);

        for (Channel channel : clients) {
            channel.writeAndFlush(
                    new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到的消息：" + content)
            );
        }

        // 和上面的方法效果一致
        //clients.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到的消息：" + content));
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
        clients.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发HandlerRemoved , ChannelGroup 会自动移除对应客户端的channel
        // clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id：" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id：" + ctx.channel().id().asShortText());
    }
}
