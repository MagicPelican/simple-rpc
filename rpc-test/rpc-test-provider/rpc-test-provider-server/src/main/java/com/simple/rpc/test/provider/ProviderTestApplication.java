package com.simple.rpc.test.provider;

import com.simple.rpc.common.annotation.EnableSimpleRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@EnableSimpleRpc(packages={"com.simple.rpc.test.provider.facade"})
@SpringBootApplication
public class ProviderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderTestApplication.class, args);
    }

}
