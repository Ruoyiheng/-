package com.yuyou.zizaiyou.redis.key;


import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 基础 Redis Key 前缀类
 */
@Setter
public class BaseKeyPrefix implements KeyPrefix {

    private String prefix; //key的前缀
    private Long timeout;
    private TimeUnit unit;

    //无过期时间
    public BaseKeyPrefix(String prefix) {
        this(prefix, -1L, null);
    }

    public BaseKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
        this.prefix = prefix;
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public Long getTimeout() {
        return timeout;
    }

    @Override
    public TimeUnit getUnit() {
        return unit;
    }
}
