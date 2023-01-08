package com.simple.rpc.common.bean;

import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.common.proxy.ProxyFactory;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
public class BeanManager {

    private static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    /**
     * 获取并注册bean
     */
    public static Object getAndRegisterBean(Class<?> clazz, MethodInterceptor interceptor) {
        String name = BeanManager.getName(clazz);

        Object bean = BeanManager.getBean(name);
        if (bean != null) {
            return bean;
        }
        return BeanManager.registerBean(name, clazz, interceptor);
    }

    public static <T> T registerBean(String serviceName, Class<T> clazz, MethodInterceptor interceptor) {
        T proxy = ProxyFactory.getProxy(clazz, interceptor);
        BEAN_MAP.put(serviceName, proxy);
        return proxy;
    }

    public static Object getBean(String serviceName) {
        return BEAN_MAP.get(serviceName);
    }


    public static String getName(Class<?> clazz) {
        if (clazz.isInterface()) {
            return standardizeClassName(clazz.getName());
        }

        final Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            throw SimpleRpcResponseEnum.classNotImplemented.getException("No interface is implemented, class is " + clazz.getName());
        }

        return standardizeClassName(interfaces[0].getName());
    }

    private static String standardizeClassName(String className) {
        // 代理类处理
        if (className.contains("$$")) {
            className = className.split("$$")[0];
        }
        return className;
    }


    public static Map<String, Object> getBeanMap() {
        return BEAN_MAP;
    }


}
