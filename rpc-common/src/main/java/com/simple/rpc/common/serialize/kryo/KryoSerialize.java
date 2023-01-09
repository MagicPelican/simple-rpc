package com.simple.rpc.common.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import com.simple.rpc.common.protocol.Message;
import com.simple.rpc.common.serialize.RpcSerialize;

import java.io.ByteArrayInputStream;

/**
 * @Author: zh
 * @Date: 2023/1/8
 */
public class KryoSerialize implements RpcSerialize {

    /**
     * Kryo 使用的三个池
     */
    private final Pool<Kryo> kryoPool;
    private final Pool<Input> inputPool;
    private final Pool<Output> outputPool;

    public KryoSerialize() {

        kryoPool = new Pool<Kryo>(true, false, 32) {
            protected Kryo create() {
                Kryo kryo = new Kryo();
                // 支持对象循环引用（否则会栈溢出）
                kryo.setReferences(true);
                // 不强制要求注册类，注册就是用时间换空间（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
                kryo.setRegistrationRequired(false);
                return kryo;
            }
        };

        inputPool = new Pool<Input>(true, false, 16) {
            protected Input create() {
                return new Input(4056);
            }
        };

        outputPool = new Pool<Output>(true, false, 16) {
            protected Output create() {
                return new Output(2048, -1);
            }
        };

    }

    /**
     * 序列化
     */
    @Override
    public <T> byte[] serialize(T data) {
        Kryo kryo = kryoPool.obtain();
        Output output = outputPool.obtain();

        try {
            kryo.writeObject(output, data);
            output.flush();// 必须刷新或者关闭，才会写入OutputStream
            return output.getBuffer();
        } finally {
            kryoPool.free(kryo);
            outputPool.free(output);
        }
    }

    /**
     * 反序列化
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) {
        Kryo kryo = kryoPool.obtain();
        Input input = inputPool.obtain();

        try {
            input.setBuffer(data);
            return kryo.readObject(input, clz);
        } finally {
            kryoPool.free(kryo);
            inputPool.free(input);
        }
    }


}
