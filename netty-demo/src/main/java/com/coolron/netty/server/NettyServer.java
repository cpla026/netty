package com.coolron.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author Administrator
 * @title: NettyServer
 * @date 2019/6/28 14:21
 * @description: netty 服务器
 *
 */
public class NettyServer {

    public NettyServer(){}

    /**
     * 启动 netty
     */
    public void start(){

        /**
         * 1、定义两个线程组，用来处理客户端通道的accept和读写事件
         * 主线程组：mainGroup 用来处理accept事件，
         * 从线程组：childgroup 用来处理通道的读写事件
         * mainGroup 获取客户端连接，连接接收到之后再将连接转发给workerGroup去处理
         *
         * EventLoopGroup：用来处理IO操作的多线程事件循环器
         */
        // 主线程组 负责接收客户端连接线程  但是不做任何处理
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        // 从线程组 负责处理客户端i/o事件、task任务、监听任务组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // netty通过ServerBootstrap启动服务端
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置主从线程组
        serverBootstrap.group(mainGroup, workerGroup);

        // BACKLOG用于构造服务端套接字ServerSocket对象，
        // 标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        //是否启用心跳保活机制  保持长连接，2小时无数据激活心跳机制
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        // 2、绑定服务端通道  使用 NioServerSocketChannel 作为服务器端的通道实现
        serverBootstrap.channel(NioServerSocketChannel.class);

        // 3、绑定handler，处理读写事件
        // 服务器初始化
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            // 服务器初始化
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                // 通过channel 获取对应的管道
                ChannelPipeline pipeline = socketChannel.pipeline();

                /**
                 * 通过管道处理 handler
                 */
                //编码通道处理
                pipeline.addLast("decode", new StringDecoder(CharsetUtil.UTF_8));
                //转码通道处理
                pipeline.addLast("encode", new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new ServerHandler());
            }
        });

        try {
            // 4、启动server，并且设置8088为启动的端口号，同时启动方式为同步
            ChannelFuture future = serverBootstrap.bind(8088).sync();
            // 服务器关闭监听
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
