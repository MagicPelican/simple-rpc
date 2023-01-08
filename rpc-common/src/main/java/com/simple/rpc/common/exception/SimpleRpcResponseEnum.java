package com.simple.rpc.common.exception;

import lombok.Getter;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Getter
public enum SimpleRpcResponseEnum {
    success(0, "success"),
    fail(1, "System exception"),

    // 通用异常
    classNotImplemented(101, "No interface is implemented"),
    decodeParseMessageErr(102, "Netty decoder parse message error"),

    // 服务方异常
    providerClassNotFind(201, "Provider class not find"),
    providerMethodNotFind(202, "Provider method not find"),
    providerRegisterBeanFail(203, "Provider register bean fail"),

    // 客户端异常
    noServiceProviderFound(301, "No service provider found"),
    noServiceProviderEnabled(302, "No service provider enabled"),
    providerUnableToConnect(303, "Service provider unable to connect"),
    requestProviderErr(304, "Provider call exception"),

    ;

    private final int code;

    private final String msg;

    SimpleRpcResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SimpleRpcException getException() {
        return new SimpleRpcException(this);
    }

    public SimpleRpcException getException(String msg) {
        return new SimpleRpcException(this, msg);
    }

    public SimpleRpcException getException4Msg(String msg) {
        return new SimpleRpcException(this, this.getMsg() + ". " + msg);
    }

}
