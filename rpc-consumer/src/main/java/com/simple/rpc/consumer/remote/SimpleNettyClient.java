package com.simple.rpc.consumer.remote;

import com.simple.rpc.common.codec.RpcNettyDecoder;
import com.simple.rpc.common.codec.RpcNettyEncoder;
import com.simple.rpc.common.constants.Constants;
import com.simple.rpc.common.netty.EventLoopGroupFactory;
import com.simple.rpc.common.protocol.Message;
import com.simple.rpc.common.protocol.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
public class SimpleNettyClient {

    private final EventLoopGroup worker;
    private final ChannelFuture channelFuture;

    public SimpleNettyClient(String host, int port) throws Exception {
        worker = EventLoopGroupFactory.create(Constants.NETTY_WORK_DEFAULT_THREADS, Constants.NETTY_WORK_THREAD_NAME);

        // 客户端启动类程序
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("NettyDecoder", new RpcNettyDecoder());
                pipeline.addLast("NettyEncoder", new RpcNettyEncoder());
                pipeline.addLast("ClientHandler", new SimpleNettyClientHandler());
            }
        });

        // 开启客户端监听，连接到远程节点，阻塞等待直到连接完成
        channelFuture = bootstrap.connect(host, port).sync();
    }

    /**
     * 发送消息
     */
    public void sendRequest(Message<Request> message) {
        channelFuture.channel().writeAndFlush(message);
    }

    public boolean isOpen() {
        return channelFuture.channel().isOpen();
    }

    public void close() {
        channelFuture.channel().closeFuture();
        worker.shutdownGracefully();
    }

}
