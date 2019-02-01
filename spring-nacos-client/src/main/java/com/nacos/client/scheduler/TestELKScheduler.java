package com.nacos.client.scheduler;

import com.alibaba.nacos.common.util.UuidUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author ssk www.8win.com Inc.All rights reserved
 * @version v1.0
 * @date 2019-01-31-上午 9:55
 */
@Log4j2
@Component
public class TestELKScheduler {


    @Autowired
    LoadBalancerClient loadBalancerClient;


    @Async
    @Scheduled(fixedDelay = 5 * 1000L)
    protected void run() {

        try {
            log.info("未开奖订单回收定时器:开始回收出票中的订单");
            // 通过spring cloud common中的负载均衡接口选取服务提供节点实现接口调用
            ServiceInstance serviceInstance = loadBalancerClient.choose("spring-nacos-server");
            String url = serviceInstance.getUri() + "/insertELK";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递


            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("id", UuidUtil.generateUuid());
            params.add("firstName", "tom");
            params.add("lastName", "alex");
            params.add("age", "25");
            params.add("about", "I'm in peking");
            log.info("发送消息为：" + params.toString());

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
            //  执行HTTP请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            log.info("提交返回结果为【】" + response.getBody());
        } catch (Exception e) {
            log.error("请求数据异常");
            e.printStackTrace();
        }

    }
}
