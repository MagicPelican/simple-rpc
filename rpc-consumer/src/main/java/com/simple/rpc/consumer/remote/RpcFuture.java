package com.simple.rpc.consumer.remote;


import com.simple.rpc.common.dto.TwoTuple;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 获取rpc结果
 *
 * @Author: zhenghao
 * @Date: 2023/1/2
 */
public class RpcFuture<T> {

    private T result;

    private final CountDownLatch latch = new CountDownLatch(1);

    private final long createMillis = System.currentTimeMillis();

    public TwoTuple<Boolean,T> getBySync(long timeoutMillis) throws InterruptedException {
        return getBySync(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    public TwoTuple<Boolean,T> getBySync(long timeout, TimeUnit unit) throws InterruptedException {
        if (latch.await(timeout, unit)) {
            return TwoTuple.of(true, result);
        }
        return TwoTuple.of(false, null);
    }

    public void setResult(T result) {
        this.result = result;
        latch.countDown();
    }

    public long getCreateMillis() {
        return createMillis;
    }


}
