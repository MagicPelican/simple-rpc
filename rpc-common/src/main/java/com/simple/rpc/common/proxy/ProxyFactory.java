package com.simple.rpc.common.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
public class ProxyFactory {

    /**
     * 获取代理类
     */
    public static <T> T getProxy(Class<T> clazz, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(interceptor);
        enhancer.setUseCache(true);
        return (T) enhancer.create();
    }

}
