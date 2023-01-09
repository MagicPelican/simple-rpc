package com.simple.rpc.consumer.proxy;

import com.simple.rpc.common.bean.BeanManager;
import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.common.protocol.*;
import com.simple.rpc.consumer.remote.RpcNetworkConnect;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: zhenghao
 * @Date: 2022/12/31
 */
@Slf4j
public class ProviderRpcProxy implements MethodInterceptor {


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) {
        // 组装参数
        Header header = HeaderFactory.create();
        String rpcRegistryName = BeanManager.getName(obj.getClass());
        Request request = new Request(rpcRegistryName, method.getName(), args);
        Message<Request> requestMessage = MessageFactory.createByRequest(header, request);

        // rpc 调用
        Message<Response> responseMessage = RpcNetworkConnect.sendRequest(requestMessage);

        // 结果处理
        Response response = responseMessage.getContent();
        if (response == null) {
            throw SimpleRpcResponseEnum.requestProviderNoneErr.getException();
        }
        if (response.getCode() != 0) {
            throw SimpleRpcResponseEnum.requestProviderErr.getException4Msg("errMsg: " + response.getErrMsg());
        }
        return response.getResult();
    }
}
