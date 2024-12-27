package com.yuyou.zizaiyou.serverarticle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    @Bean
    public ThreadPoolExecutor BusinessThreadPoolExecutor(){
         return new ThreadPoolExecutor(10,50,10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
    }
}
