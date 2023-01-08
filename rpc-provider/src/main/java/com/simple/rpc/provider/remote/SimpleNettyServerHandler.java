package com.simple.rpc.provider.remote;

import com.simple.rpc.common.bean.BeanManager;
import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.common.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Slf4j
public class SimpleNettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 本方法用于读取客户端发送的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message<Request> message = (Message) msg;

        // 客户端请求处理 todo 异步
        Request request = message.getContent();
        Object bean = BeanManager.getBean(request.getRpcRegistryName());
        if (bean == null) {
            throw SimpleRpcResponseEnum.providerClassNotFind.getException();
        }
        Method method = bean.getClass().getMethod(request.getMethodName(), request.getArgTypes());

        Object result = method.invoke(bean, request.getArgs());
        Response response = new Response(SimpleRpcResponseEnum.success);
        response.setResult(result);
        Message<Response> resultMsg = MessageFactory.createByResponse(message.getHeader(), response);

        // 返回结果
        ctx.writeAndFlush(resultMsg);
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("Netty case exception", cause);
        Header header = HeaderFactory.create();
        Response response = new Response(SimpleRpcResponseEnum.fail.getCode(), cause.toString());
        Message<Response> resultMsg = MessageFactory.createByResponse(header, response);

        // 返回结果
        ctx.writeAndFlush(resultMsg);
    }

    /**
     * 信息获取完毕后操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}
