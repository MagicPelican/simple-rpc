package com.simple.rpc.provider.framewrok;

import com.simple.rpc.provider.ProviderStart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Author: zhenghao
 * @Date: 2023/1/2
 */
@Slf4j
public class StratBySpringInitEnd implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 启动生产者
        try {
            ProviderStart.start();
        } catch (Exception e) {
            log.error("SimpleRpc provider start fail", e);
        }
    }

}
