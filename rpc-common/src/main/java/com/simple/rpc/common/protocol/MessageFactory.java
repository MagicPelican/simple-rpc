package com.simple.rpc.common.protocol;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
public class MessageFactory {

    public static Message<Request> createByRequest(Header header, Request request) {
        return new Message<>(header, request);
    }

    public static Message<Response> createByResponse(Header header, Response response) {
        return new Message<>(header, response);
    }

    public static Message<Response> createByErrResponse(Header header, String errMsg) {
        return new Message<>(header, new Response(1, errMsg));
    }

}
