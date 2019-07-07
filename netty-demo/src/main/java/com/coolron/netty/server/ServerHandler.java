package com.coolron.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author Administrator
 * @title: ServerHandler
 * @date 2019/6/28 14:58
 * @description: 服务端业务处理器
 * 业务逻辑处理
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler{
//public class ServerHandler extends ChannelInboundHandlerAdapter {

    // 读取客户端通道的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {

        // 获取 channel
        Channel channel = ctx.channel();

        if(o instanceof ByteBuf){
            // 打印接收到客户端的数据
            System.out.println(((ByteBuf)o).toString(Charset.defaultCharset()));
        }

        // 接收到客户端数据后, 给客户端回复消息
        String responseMsg = "I have received your msg!!!";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(responseMsg.getBytes());
        channel.writeAndFlush(buf);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println(ctx.channel().id() + " 发生了错误,此连接被关闭" );
        ctx.close();
        System.out.println("server>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        cause.printStackTrace();
    }
}
