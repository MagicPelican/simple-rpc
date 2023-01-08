package com.simple.rpc.consumer.bean;

import com.simple.rpc.common.bean.BeanManager;
import com.simple.rpc.consumer.proxy.ProviderRpcProxy;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
public class ResourceInit {

    private final static ProviderRpcProxy proxy = new ProviderRpcProxy();

    public static Object resourceInit(Class<?> clazz) {
        return BeanManager.getAndRegisterBean(clazz, proxy);
    }


}
