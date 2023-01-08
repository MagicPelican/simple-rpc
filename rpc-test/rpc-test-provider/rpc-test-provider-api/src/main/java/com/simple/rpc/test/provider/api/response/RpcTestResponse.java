package com.simple.rpc.test.provider.api.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@Data
public class RpcTestResponse implements Serializable {

    private String name;

    private String cardNo;

    private LocalDateTime registryTime;

}
