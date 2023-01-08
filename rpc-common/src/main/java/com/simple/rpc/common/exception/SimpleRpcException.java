package com.simple.rpc.common.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Getter
@ToString
public class SimpleRpcException extends RuntimeException {

    private final SimpleRpcResponseEnum exceptionEnum;

    private final String message;

    public SimpleRpcException(SimpleRpcResponseEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
        this.message = exceptionEnum.getMsg();
    }

    public SimpleRpcException(SimpleRpcResponseEnum exceptionEnum, String message) {
        this.exceptionEnum = exceptionEnum;
        this.message = message;
    }

    public SimpleRpcException(SimpleRpcResponseEnum exceptionEnum, Throwable cause) {
        super(cause);
        this.exceptionEnum = exceptionEnum;
        this.message = exceptionEnum.getMsg();
    }

    public SimpleRpcException(SimpleRpcResponseEnum exceptionEnum, String message, Throwable cause) {
        super(cause);
        this.exceptionEnum = exceptionEnum;
        this.message = message;
    }

    public int getCode() {
        return exceptionEnum.getCode();
    }

}
