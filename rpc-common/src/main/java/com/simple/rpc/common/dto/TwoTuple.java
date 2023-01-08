package com.simple.rpc.common.dto;

import lombok.Getter;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@Getter
public class TwoTuple<T1, T2> {

    private final T1 t1;
    private final T2 t2;

    public TwoTuple(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public static <T1, T2> TwoTuple<T1, T2> of(T1 t1, T2 t2) {
        return new TwoTuple<>(t1, t2);
    }
}
