package com.simple.rpc.test.provider.facade;

import com.simple.rpc.provider.annotation.SimpleRpcService;
import com.simple.rpc.test.provider.api.facade.RpcTestFacade;
import com.simple.rpc.test.provider.api.request.RpcTestrequest;
import com.simple.rpc.test.provider.api.response.RpcTestResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@Slf4j
@SimpleRpcService
public class RpcTestFacadeImpl implements RpcTestFacade {

    @Override
    public RpcTestResponse rpcTest(RpcTestrequest request) {
        RpcTestResponse response = new RpcTestResponse();
        response.setName(request.getName());
        response.setCardNo("123cooo");
        response.setRegistryTime(LocalDateTime.now());
        log.info("rpcTest, request={}, response={}", request, response);
        return response;
    }
}
