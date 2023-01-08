package com.simple.rpc.consumer.framewrok;

import com.simple.rpc.consumer.annotation.SimpleRpcResource;
import com.simple.rpc.consumer.bean.ResourceInit;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Component
public class SimpleRpcPostProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public SimpleRpcPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        do {
            for (final Field field : clazz.getDeclaredFields()) {
                final SimpleRpcResource annotation = AnnotationUtils.findAnnotation(field, SimpleRpcResource.class);
                if (annotation != null) {
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, bean, processInjectionPoint(field.getType()));
                }
            }
            for (final Method method : clazz.getDeclaredMethods()) {
                final SimpleRpcResource annotation = AnnotationUtils.findAnnotation(method, SimpleRpcResource.class);
                if (annotation != null) {
                    final Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length != 1) {
                        throw new BeanDefinitionStoreException(
                                "Method " + method + " doesn't have exactly one parameter.");
                    }
                    ReflectionUtils.makeAccessible(method);
                    ReflectionUtils.invokeMethod(method, bean,
                            processInjectionPoint(paramTypes[0]));
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return bean;
    }

    // 获取代理类
    protected Object processInjectionPoint(final Class<?> clazz) {
        return ResourceInit.resourceInit(clazz);
    }

}
