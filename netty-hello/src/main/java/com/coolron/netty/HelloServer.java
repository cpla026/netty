package com.coolron.netty;/**
 * Created by Administrator on 2019/6/20.
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Auther: xf
 * @Date: 2019/6/20 22:03
 * @Description: 实现客户端发送一个请求，服务器会返回hello netty
 */
public class HelloServer {

    public static void main(String[] args){
        // 定义一对线程组
        // 主线程组 用于接收客户端的连接，但是不做任何处理，跟老板一样 不做事
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 从线程组 老板线程组会把任务丢给他  让手下线程组去做事
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // netty 服务器的创建，ServerBootstrap 启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)   // 设置主从线程组
                    .channel(NioServerSocketChannel.class) // 设置nio双向通道
                    .childHandler(new HelloServerInitializer());                   // 子处理器 用于处理 workerGroup

            // 启动 Server， 设置8088 为启动的端口，启动方式为 同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
