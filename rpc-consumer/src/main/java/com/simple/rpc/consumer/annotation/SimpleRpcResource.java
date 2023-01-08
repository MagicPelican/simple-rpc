package com.simple.rpc.consumer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface SimpleRpcResource {

}
