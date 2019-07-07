package com.coolron.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Administrator
 * @title: NettyClient
 * @date 2019/6/28 14:21
 * @description: 客户端
 * 
 */
public class NettyClient {

    private String ip;
    private int port;

    public NettyClient(){}
    public NettyClient(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void start(){
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);

        //  绑定客户端通道
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //注册管道
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast(new ClientHandler());
            }
        });

        try {
            // 连接到服务器
            ChannelFuture future = bootstrap.connect(this.ip, this.port).sync();

            for (int i = 0; i < 10; i++) {
                future.channel().writeAndFlush("I am client: " + i);
                Thread.sleep(3000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clientGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new NettyClient("127.0.0.1",8088).start();
    }

}
