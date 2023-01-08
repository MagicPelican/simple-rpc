package com.simple.rpc.common.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @Author: zhenghao
 * @Date: 2023/1/2
 */
public class EventLoopGroupFactory {

    /**
     * 创建 EventLoopGroup 实例
     *
     * @param threadNum      线程数
     * @param threadPoolName 线程池名
     */
    public static EventLoopGroup create(int threadNum, String threadPoolName) {
        DefaultThreadFactory threadFactory = new DefaultThreadFactory(threadPoolName, true);
        return new NioEventLoopGroup(threadNum, threadFactory);
    }

}
