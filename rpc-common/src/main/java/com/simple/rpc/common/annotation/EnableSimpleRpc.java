package com.simple.rpc.common.annotation;


import java.lang.annotation.*;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSimpleRpc {

    String[] packages() default {};

}
