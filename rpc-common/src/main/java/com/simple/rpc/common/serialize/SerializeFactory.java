package com.simple.rpc.common.serialize;

import com.simple.rpc.common.serialize.kryo.KryoSerialize;

/**
 * @Author: zh
 * @Date: 2023/1/8
 */
public class SerializeFactory {

    public static RpcSerialize create() {
        // 目前只提供 Kryo 实现方式
        return new KryoSerialize();
    }


}
