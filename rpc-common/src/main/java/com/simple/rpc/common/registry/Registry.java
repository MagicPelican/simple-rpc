package com.simple.rpc.common.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.InstanceSerializer;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.Collections;


/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Slf4j
public class Registry {

    private static final ServiceDiscovery<ServiceMetadata> serviceDiscovery;
    private final static InstanceSerializer<ServiceMetadata> serializer = new JsonInstanceSerializer<>(ServiceMetadata.class);

    private final static String zkRootPath = "/simpleRpc/services";

    static {
        try {
            CuratorFramework curatorFramework = ZookeeperClient.buildCuratorFramework();
            // 初始化ServiceDiscovery
            serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetadata.class)
                    .client(curatorFramework)
                    .basePath(zkRootPath)
                    .serializer(serializer)
                    .build();
            serviceDiscovery.start(); // 启动ServiceDiscovery
        } catch (Exception e) {
            throw new IllegalStateException("Create zookeeper service discovery failed.", e);
        }
    }

    public static void registryServer(ServiceMetadata serviceMetadata) throws Exception {
        String name = serviceMetadata.getRpcRegistryName();
        String host = serviceMetadata.getHost();
        int port = serviceMetadata.getPort();
        String id = getProviderId(host, port);
        ServiceInstanceBuilder builder = ServiceInstance.builder()
                .id(id)
                .name(name)
                .address(host)
                .port(port)
                .payload(serviceMetadata);

        serviceDiscovery.registerService(builder.build());
    }

    /**
     * 根据服务名获取服务节点
     */
    public static Collection<ServiceInstance<ServiceMetadata>> getInstances(String serviceName) {
        try {
            return serviceDiscovery.queryForInstances(serviceName);
        } catch (Exception e) {
            log.error("Failed to get service Instances by registry ,serviceName: {}", serviceName, e);
        }
        return Collections.emptyList();
    }

    /**
     * 获取 Provider id
     */
    public static String getProviderId(String host, int port) {
        return host + ":" + port;
    }

    /**
     * 解析 Provider id
     */
    public static ServiceInfo getPServiceByProviderId(String providerId) {
        final String[] split = providerId.split(":");
        return new ServiceInfo(split[0], Integer.parseInt(split[1]));
    }


}
