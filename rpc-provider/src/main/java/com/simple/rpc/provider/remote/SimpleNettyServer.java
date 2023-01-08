package com.simple.rpc.provider.remote;

import com.simple.rpc.common.codec.RpcNettyDecoder;
import com.simple.rpc.common.codec.RpcNettyEncoder;
import com.simple.rpc.common.constants.Constants;
import com.simple.rpc.common.netty.EventLoopGroupFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * provider netty 服务
 *
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
public class SimpleNettyServer {

    private ChannelFuture channelFuture;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;



    public void start(int port) throws Exception {
        bossGroup = EventLoopGroupFactory.create(Constants.NETTY_BOSS_DEFAULT_THREADS, Constants.NETTY_BOSS_THREAD_NAME);
        workerGroup = EventLoopGroupFactory.create(Constants.NETTY_WORK_DEFAULT_THREADS, Constants.NETTY_WORK_THREAD_NAME);

        // netty服务器启动类的创建, 辅助工具类，用于服务器通道的一系列配置
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                // 用于构造socketchannel工厂
                .channel(NioServerSocketChannel.class)   //指定NIO的模式
                .childHandler(new ChannelInitializer<SocketChannel>() {  // 子处理器，用于处理workerGroup
                    protected void initChannel(SocketChannel socketChannel) {
                        final ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("NettyDecoder", new RpcNettyDecoder());
                        pipeline.addLast("NettyEncoder", new RpcNettyEncoder());
                        pipeline.addLast("ServerHandler", new SimpleNettyServerHandler());
                    }
                });

        // 启动server，绑定端口
        channelFuture = serverBootstrap.bind(port).sync();
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    /**
     * 关闭
     */
    public void shutDown() throws InterruptedException {
        channelFuture.channel().close().sync();
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().awaitUninterruptibly(5000);

        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().awaitUninterruptibly(5000);
        }
    }
}
