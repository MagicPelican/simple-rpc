package com.simple.rpc.provider.remote;

import com.simple.rpc.common.bean.BeanManager;
import com.simple.rpc.common.exception.SimpleRpcException;
import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.common.protocol.Message;
import com.simple.rpc.common.protocol.MessageFactory;
import com.simple.rpc.common.protocol.Request;
import com.simple.rpc.common.protocol.Response;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message<Request> message = (Message) msg;

        Message<Response> resultMsg;
        Response response;
        try {
            // 客户端请求处理 todo 使用工作线程池处理
            Request request = message.getContent();
            Object bean = BeanManager.getBean(request.getRpcRegistryName());
            if (bean == null) {
                throw SimpleRpcResponseEnum.providerClassNotFind.getException4Msg("provider: " + request.getRpcRegistryName());
            }
            Method method = bean.getClass().getMethod(request.getMethodName(), request.getArgTypes());

            // 调用业务逻辑
            Object result = method.invoke(bean, request.getArgs());
            response = new Response(SimpleRpcResponseEnum.success);
            response.setResult(result);
        } catch (SimpleRpcException e) {
            log.error("Provider handling exception, message={}", message, e);
            response = new Response(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("Provider handling exception, message={}", message, e);
            response = new Response(SimpleRpcResponseEnum.fail);
            response.setErrMsg(e.getMessage());
        }

        // 返回结果
        resultMsg = MessageFactory.createByResponse(message.getHeader(), response);
        ctx.writeAndFlush(resultMsg);
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("Netty case exception", cause);
    }

    /**
     * 信息获取完毕后操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}
