package com.simple.rpc.common.serialize;

/**
 * @Author: zh
 * @Date: 2023/1/8
 */
public interface RpcSerialize {

    /**
     * 序列化
     */
    <T> byte[] serialize(T data);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clz);

}
