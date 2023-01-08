package com.simple.rpc.common.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
public class ZookeeperClient {

    private static final String address = "localhost:2181";

    public static CuratorFramework buildCuratorFramework() throws InterruptedException {
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, retry);
        client.start();
        client.blockUntilConnected();  // 阻塞当前线程，等待连接成功
        return client;
    }


}
