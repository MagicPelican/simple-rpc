package com.simple.rpc.provider.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface SimpleRpcService {

    /**
     * 这个bean注入到Spring容器中的名字
     */
    String beanName() default "";

}
