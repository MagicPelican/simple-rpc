package com.simple.rpc.common.registry;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Data
public class ServiceMetadata implements Serializable {

    /**
     * rpc 服务名，服务接口类全名
     */
    private String rpcRegistryName;

    private String serviceName;

    private String host;

    private int port;

}
