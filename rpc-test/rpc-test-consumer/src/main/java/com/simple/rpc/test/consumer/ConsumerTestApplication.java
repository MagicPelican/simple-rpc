package com.simple.rpc.test.consumer;

import com.simple.rpc.common.annotation.EnableSimpleRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@EnableSimpleRpc
@SpringBootApplication
public class ConsumerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerTestApplication.class, args);
    }

}
