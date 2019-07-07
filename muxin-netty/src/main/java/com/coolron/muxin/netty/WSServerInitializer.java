package com.coolron.muxin.netty;/**
 * Created by Administrator on 2019/6/22.
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Auther: xf
 * @Date: 2019/6/22 22:05
 * @Description: 初始化器
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // websocket 基于 http 协议 需要http编解码器
        pipeline.addLast(new HttpServerCodec());

        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        // 对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        // 几乎在netty的编程都会使用到此Handler
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));

        /******************** 以上用于支持 http 协议 *****************/

        /**
         * websocket 服务器处理的协议，用于指定给客户端连接访问的路由：/ws
         * 本Handler 会帮你处理一些复杂的事
         * 会处理握手动作：handshaking(close, ping, pong) ping + pong = 心跳
         * 对于websocket 讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        // 自定义的Handler
        pipeline.addLast(new ChatHandler());

    }
}
