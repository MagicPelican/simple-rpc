package com.simple.rpc.common.registry;

import lombok.Data;

/**
 * @Author: zhenghao
 * @Date: 2023/1/2
 */
@Data
public class ServiceInfo {

    private String host;

    private int port;

    public ServiceInfo() {
    }

    public ServiceInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }

}
