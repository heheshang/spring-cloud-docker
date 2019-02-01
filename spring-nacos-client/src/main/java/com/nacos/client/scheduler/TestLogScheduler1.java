package com.nacos.client.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author ssk www.8win.com Inc.All rights reserved
 * @version v1.0
 * @date 2019-01-30-下午 4:01
 */
@Log4j2
@Component
public class TestLogScheduler1 {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Async
    @Scheduled(fixedDelay = 10 * 1000L)
    protected void run() {

        try {
            log.info("未开奖订单回收定时器:开始回收出票中的订单");

        } catch (Exception e) {
            log.error("请求数据异常");
            e.printStackTrace();
        }
    }
}
