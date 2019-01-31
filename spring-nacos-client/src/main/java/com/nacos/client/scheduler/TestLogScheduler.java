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
public class TestLogScheduler {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Async
    @Scheduled(fixedDelay  =  10 *1000L)
    protected void run() {

        log.info("未开奖订单回收定时器:开始回收出票中的订单");
        // 通过spring cloud common中的负载均衡接口选取服务提供节点实现接口调用
        ServiceInstance serviceInstance = loadBalancerClient.choose("spring-nacos-server");
        String url = serviceInstance.getUri() + "/hello?name=" + "didi";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        log.info("Invoke : " + url + ", return : " + result);
    }
}
