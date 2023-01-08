package com.simple.rpc.consumer.remote;

import com.simple.rpc.common.constants.Constants;
import com.simple.rpc.common.dto.TwoTuple;
import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.common.protocol.Message;
import com.simple.rpc.common.protocol.MessageFactory;
import com.simple.rpc.common.protocol.Request;
import com.simple.rpc.common.protocol.Response;
import com.simple.rpc.common.registry.Registry;
import com.simple.rpc.common.registry.ServiceInfo;
import com.simple.rpc.common.registry.ServiceMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @Author: zhenghao
 * @Date: 2023/1/2
 */
@Slf4j
public class RpcNetworkConnect {

    /**
     * 服务 providerId 与网络链接的映射
     */
    private static final Map<String, SimpleNettyClient> SERVICE_CONNECT = new ConcurrentHashMap<>();

    /**
     * 调用结果map，messageId : RpcFuture
     */
    private static final Map<String, RpcFuture<Message<Response>>> RPC_FUTURE_MAP = new ConcurrentHashMap<>();

    private static final ReentrantLock lock = new ReentrantLock();

    public static RpcFuture<Message<Response>> getRpcFuture(String msgId) {
        return RPC_FUTURE_MAP.get(msgId);
    }

    /**
     * 重连服务端 todo 增加重试次数逻辑
     */
    private static void reconnect(String providerId) {
        try {
            ServiceInfo serviceInfo = Registry.getPServiceByProviderId(providerId);
            if (lock.tryLock()) {
                try {
                    if (!checkProviderIsError(providerId)) {
                        return;
                    }
                    SimpleNettyClient oldClient = SERVICE_CONNECT.get(providerId);
                    if (oldClient != null) {
                        oldClient.close();
                        SERVICE_CONNECT.remove(providerId);
                    }

                    SimpleNettyClient client = new SimpleNettyClient(serviceInfo.getHost(), serviceInfo.getPort());
                    if (client.isOpen()) {
                        SERVICE_CONNECT.put(providerId, client);
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            log.error("Provider connection failed, Provider={}", providerId, e);
        }
    }


    public static Message<Response> sendRequest(Message<Request> message) {
        final String registryName = message.getContent().getRpcRegistryName();
        final String messageId = message.getHeader().getMessageId();

        // 从注册中心获取 provider
        Collection<ServiceInstance<ServiceMetadata>> instances = Registry.getInstances(registryName);
        if (instances == null || instances.isEmpty()) {
            throw SimpleRpcResponseEnum.noServiceProviderFound.getException4Msg("service name: " + registryName);
        }

        // 随机取一个节点 todo 路由
        List<ServiceInstance<ServiceMetadata>> serviceInstances = instances.stream().filter(ServiceInstance::isEnabled).collect(Collectors.toList());
        if (serviceInstances.isEmpty()) {
            throw SimpleRpcResponseEnum.noServiceProviderFound.getException4Msg("service name: " + registryName);
        }
        int anInt = new Random().nextInt(serviceInstances.size());
        ServiceInstance<ServiceMetadata> instance = serviceInstances.get(anInt);
        String providerId = instance.getId();

        // 获取连接
        if (checkProviderIsError(providerId)) {
            reconnect(providerId);
        }

        SimpleNettyClient nettyClient = SERVICE_CONNECT.get(providerId);
        if (nettyClient == null) {
            throw SimpleRpcResponseEnum.providerUnableToConnect.getException4Msg("provider: " + providerId);
        }

        // 请求
        nettyClient.sendRequest(message);

        // 缓存 RpcFuture
        RpcFuture<Message<Response>> future = new RpcFuture<>();
        RPC_FUTURE_MAP.put(messageId, future);

        // 获取结果
        Message<Response> resMsg;
        try {
            TwoTuple<Boolean, Message<Response>> twoTuple = future.getBySync(Constants.REQUEST_TIMEOUT);
            if (twoTuple.getT1()) {
                resMsg = twoTuple.getT2();
            } else {
                resMsg = MessageFactory.createByErrResponse(message.getHeader(), "Call timeout, request message: " + message.toString());
            }
        } catch (Exception e) {
            log.error("Request provider fail, message={}", message, e);
            resMsg = MessageFactory.createByErrResponse(message.getHeader(), e.getMessage());
        } finally {
            RPC_FUTURE_MAP.remove(messageId);
        }
        return resMsg;
    }

    /**
     * 验证服务方是否无法接通
     */
    private static boolean checkProviderIsError(String providerId) {
        SimpleNettyClient nettyClient = SERVICE_CONNECT.get(providerId);
        return nettyClient == null || !nettyClient.isOpen();
    }

}
