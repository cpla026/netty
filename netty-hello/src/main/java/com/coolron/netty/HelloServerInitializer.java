package com.coolron.netty;/**
 * Created by Administrator on 2019/6/20.
 */

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Auther: xf
 * @Date: 2019/6/20 22:24
 * @Description: 初始化器  channel 注册后会执行里面相应的初始化方法
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        // 通过channel 获取对应的管道
        ChannelPipeline pipeline = channel.pipeline();
        /**
         * 通过管道添加 Handler
         * HttpServerCodec 由netty 自己提供的助手类，可以理解为拦截器
         * 当请求到服务端，我们需要做解码，响应到客户端做编码
         */
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());

        // 添加自定义的助手类，返回 “hello netty”
        pipeline.addLast("customHandler",new CustomHandler());
    }
}
