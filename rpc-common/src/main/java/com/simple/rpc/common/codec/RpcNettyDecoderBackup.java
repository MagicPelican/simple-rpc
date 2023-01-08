package com.simple.rpc.common.codec;

import com.simple.rpc.common.constants.Constants;
import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.common.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Slf4j
public abstract class RpcNettyDecoderBackup extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < Constants.HEADER_SIZE) {
            return; // 不到16字节的话无法解析消息头，暂不读取
        }

        // 标记一下当前的readIndex指针的位置，方便重置
        in.markReaderIndex();
        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        int dataLength = in.readInt();
        // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        // 转为Message对象
        Message<Object> msg;
        try {
            msg = parseObject(data);
        } catch (IOException e) {
            String dataMsg = new String(data);
            log.error("netty decoder parse Message error, data={}", dataMsg, e);
            throw SimpleRpcResponseEnum.noServiceProviderFound.getException4Msg("message: " + dataMsg);
        }

        // 尝试读取消息头的魔数部分
        short magic = msg.getHeader().getMagic();
        if (magic != Constants.MAGIC) { // 魔数不匹配会抛出异常
            throw new RuntimeException("magic number error:" + magic);
        }

        out.add(msg);
    }


    protected abstract Message<Object> parseObject(byte[] data) throws IOException;

}
