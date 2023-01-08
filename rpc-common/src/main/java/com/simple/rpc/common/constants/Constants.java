package com.simple.rpc.common.constants;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
public class Constants {

    /**
     * netty 默认boss程数
     */
    public static final int NETTY_BOSS_DEFAULT_THREADS = 2;

    /**
     * netty 默认工作线程数
     */
    public static final int NETTY_WORK_DEFAULT_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    /**
     * netty boss线名
     */
    public static final String NETTY_BOSS_THREAD_NAME = "SimpleRpc-nettyBoss";

    /**
     * netty 工作线名
     */
    public static final String NETTY_WORK_THREAD_NAME = "SimpleRpc-nettyWork";

    /**
     * 魔数
     */
    public static final short MAGIC = (short) 0xE0F1;

    /**
     * header长度
     */
    public static final int HEADER_SIZE = 16;

    public static final String VERSION_1 = "1";

    /**
     * 请求超时 毫秒
     */
    public static final int REQUEST_TIMEOUT = 30000;

    public final static int HEARTBEAT_CODE = -1;

    public static boolean isHeartBeat(byte extraInfo) {
        return (extraInfo & 32) != 0;
    }

    public static boolean isRequest(byte extraInfo) {
        return (extraInfo & 1) != 1;
    }

}
