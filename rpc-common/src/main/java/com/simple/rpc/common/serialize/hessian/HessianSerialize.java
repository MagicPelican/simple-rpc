package com.simple.rpc.common.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class HessianSerialize {

    /**
     * 对部分类会出现堆栈溢出的情况，如 LocalDateTime，待解决
     */
    public static <T> byte[] serialize(T data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.writeObject(data);
        out.close();
        byte[] bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }

    public static <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(data));
        return (T) input.readObject(clz);
    }
}