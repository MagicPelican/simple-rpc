package com.simple.rpc.provider.bean;

import com.simple.rpc.common.bean.BeanManager;
import com.simple.rpc.provider.proxy.ProviderProxy;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
public class ServiceBeanInit {

    private static final ProviderProxy proxy = new ProviderProxy();

    /**
     * 获取并初始化bean
     */
    public static Object getAndInitBean(Class<?> clazz) {
        return BeanManager.getAndRegisterBean(clazz, proxy);
    }

}
