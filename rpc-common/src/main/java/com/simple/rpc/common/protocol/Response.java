package com.simple.rpc.common.protocol;


import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Response implements Serializable {

    /**
     * 响应码，正常响应为0，非0表示异常响应
     */
    private int code = 0;

    /**
     * 异常信息
     */
    private String errMsg;

    /**
     * 响应结果
     */
    private Object result;

    public Response() {
    }

    public Response(SimpleRpcResponseEnum responseEnum) {
        new Response(responseEnum.getCode(), responseEnum.getMsg());
    }

    public Response(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

}
