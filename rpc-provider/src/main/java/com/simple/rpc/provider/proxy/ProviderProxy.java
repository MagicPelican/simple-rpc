package com.simple.rpc.provider.proxy;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Slf4j
public class ProviderProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        return proxy.invokeSuper(obj, args);
    }
}
