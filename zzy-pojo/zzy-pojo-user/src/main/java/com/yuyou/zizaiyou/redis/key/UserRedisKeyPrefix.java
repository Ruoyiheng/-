package com.yuyou.zizaiyou.redis.key;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: xa5fun
 * @Version: 1.0
 */
public class UserRedisKeyPrefix extends BaseKeyPrefix {

	//用户注册key
	public static final UserRedisKeyPrefix USERS_REGISTER_VERIFYCODE =
			new UserRedisKeyPrefix("users:register",300L, TimeUnit.SECONDS);
	public UserRedisKeyPrefix(String prefix) {
		super(prefix);
	}

	public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
		super(prefix, timeout, unit);
	}
}
