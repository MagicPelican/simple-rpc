package com.simple.rpc.test.consumer.controller;

import com.simple.rpc.consumer.annotation.SimpleRpcResource;
import com.simple.rpc.test.provider.api.facade.RpcTestFacade;
import com.simple.rpc.test.provider.api.request.RpcTestrequest;
import com.simple.rpc.test.provider.api.response.RpcTestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhenghao
 * @Date: 2023/1/7
 */
@Slf4j
@RestController
@RequestMapping({"/test"})
public class TestController {

    @SimpleRpcResource
    private RpcTestFacade rpcTestFacade;

    @GetMapping("/rpc/{content}")
    public String rpcTest(@PathVariable String content) {
        log.info("rpcTest, content={}", content);
        RpcTestResponse response = null;
        try {
            RpcTestrequest testrequest = new RpcTestrequest();
            testrequest.setName(content);
            testrequest.setAge(31);

            response = rpcTestFacade.rpcTest(testrequest);
            log.info("rpcTest, content={}, response={}", content, response);
        } catch (Exception e) {
            log.error("rpcTest error", e);
        }
        return "rpc:" + response;
    }

}
