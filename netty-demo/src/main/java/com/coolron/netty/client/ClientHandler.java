package com.coolron.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

/**
 * @author Administrator
 * @title: ClientHandler
 * @date 2019/6/28 16:04
 * @description:
 * 处理服务端返回的数据
 */
public class ClientHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            String value = ((ByteBuf) msg).toString(Charset.defaultCharset());
            System.out.println("服务器端返回的数据:" + value);
        }

        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
        ctx.channel().attr(key).set("客户端处理完毕");

        //把客户端的通道关闭
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().id() + " 发生了错误,此连接被关闭" );
        ctx.close();
        System.out.println("client >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        cause.printStackTrace();
    }
}
