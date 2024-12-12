package com.yuyou.zizaiyou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ZyyGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZyyGateWayApplication.class,args);
    }
}