package com.simple.rpc.test.provider.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@Data
public class RpcTestrequest implements Serializable {

    private String name;
    private int age;

}
