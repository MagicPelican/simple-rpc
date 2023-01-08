package com.simple.rpc.common.serialize;

/**
 * @Author: zh
 * @Date: 2023/1/7
 */
public class SerializeClient {

    private static final RpcSerialize rpcSerialize = SerializeFactory.create();

    public static <T> byte[] serialize(T obj) {
        return rpcSerialize.serialize(obj);
    }

    public static <T> T deserialize(byte[] data, Class<T> clz) {
        return rpcSerialize.deserialize(data, clz);
    }

}
