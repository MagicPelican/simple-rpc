package com.simple.rpc.test.provider.api.facade;

import com.simple.rpc.test.provider.api.request.RpcTestrequest;
import com.simple.rpc.test.provider.api.response.RpcTestResponse;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
public interface RpcTestFacade {

    RpcTestResponse rpcTest(RpcTestrequest request);

}
