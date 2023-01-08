package com.simple.rpc.provider;

import com.simple.rpc.common.bean.BeanManager;
import com.simple.rpc.common.registry.Registry;
import com.simple.rpc.common.registry.ServiceMetadata;
import com.simple.rpc.common.utils.IpUtil;
import com.simple.rpc.provider.remote.SimpleNettyServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Slf4j
public class ProviderStart {

    /**
     * 启动rpc服务
     */
    public static void start() throws Exception {
        Map<String, Object> beanMap = BeanManager.getBeanMap();
        if (beanMap.isEmpty()) {
            log.info("There are no services that require registration");
            return;
        }

        // 启动 provider netty 服务
        SimpleNettyServer nettyServer = new SimpleNettyServer();
        nettyServer.start(18888);

        // 向注册中心注册服务
        String localIp = IpUtil.getLocalIp();
        int port = 18888;
        String serviceName = "rpcService";
        for (String name : beanMap.keySet()) {
            ServiceMetadata metadata = new ServiceMetadata();
            metadata.setHost(localIp);
            metadata.setPort(port);
            metadata.setRpcRegistryName(name);
            metadata.setServiceName(serviceName);
            Registry.registryServer(metadata);
        }
    }

}
