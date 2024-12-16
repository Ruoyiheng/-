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
			new UserRedisKeyPrefix("users:register:verifycode",300L, TimeUnit.SECONDS);

	//存入redis的用户token
	public static final UserRedisKeyPrefix USERS_LOGIN_INFO =
			new UserRedisKeyPrefix("users:login:info");
	public UserRedisKeyPrefix(String prefix) {
		super(prefix);
	}

	public UserRedisKeyPrefix(String prefix, Long timeout, TimeUnit unit) {
		super(prefix, timeout, unit);
	}
}
