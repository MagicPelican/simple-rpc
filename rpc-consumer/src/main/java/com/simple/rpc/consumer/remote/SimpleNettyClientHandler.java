package com.simple.rpc.consumer.remote;

import com.simple.rpc.common.protocol.Message;
import com.simple.rpc.common.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Slf4j
public class SimpleNettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 本方法用于接收服务端发送过来的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof Message)) {
            log.warn("非法的请求，msg={}", msg);
            return;
        }
        Message<Response> result = (Message) msg;

        // 返回结果
        RpcFuture<Message<Response>> rpcFuture = RpcNetworkConnect.getRpcFuture(result.getHeader().getMessageId());
        if (rpcFuture != null) {
            rpcFuture.setResult(result);
        }
    }

    /**
     * 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("SimpleNettyClientHandler 异常", cause);
    }

    /**
     * 连接成功时调用本方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("SimpleNettyClientHandler 连接成功");
    }

}
