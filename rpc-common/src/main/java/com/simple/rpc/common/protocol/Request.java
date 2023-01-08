package com.simple.rpc.common.protocol;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Request implements Serializable {

    /**
     * 请求rpc服务名
     */
    private String rpcRegistryName;

    /**
     * 请求的方法名称
     */
    private String methodName;

    /**
     * 请求方法的参数类型
     */
    private Class<?>[] argTypes;

    /**
     * 请求方法的参数
     */
    private Object[] args;

    public Request() {
    }

    public Request(String rpcRegistryName, String methodName, Object[] args) {
        this.rpcRegistryName = rpcRegistryName;
        this.methodName = methodName;
        this.args = args;
        this.argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
    }

}
