package com.simple.rpc.common.codec;

import com.simple.rpc.common.protocol.Message;
import com.simple.rpc.common.serialize.SerializeClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
public class RpcNettyEncoder extends MessageToByteEncoder<Message<?>> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Message message, ByteBuf out) {
        byte[] data = SerializeClient.serialize(message);
        out.writeInt(data.length);  // 先将消息长度写入，也就是消息头
        out.writeBytes(data);   // 消息体
    }

}
