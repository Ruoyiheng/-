package com.yuyou.zizaiyou.serveruser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuyou.zizaiyou.commoncore.exception.*;
import com.yuyou.zizaiyou.dto.RegInfo;
import com.yuyou.zizaiyou.jwt.JwtUtils;
import com.yuyou.zizaiyou.po.Userinfo;
import com.yuyou.zizaiyou.redis.key.UserRedisKeyPrefix;
import com.yuyou.zizaiyou.redis.utils.RedisCache;
import com.yuyou.zizaiyou.serveruser.mapper.UserinfoMapper;
import com.yuyou.zizaiyou.serveruser.service.UserinfoService;
import com.yuyou.zizaiyou.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
* @author xa5fun
* @description 针对表【userinfo】的数据库操作Service实现
* @createDate 2024-12-05 16:51:48
*/
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo> implements UserinfoService {


	public UserinfoServiceImpl(RedisCache redisCache) {
		this.redisCache = redisCache;
	}

	RedisCache redisCache;

	@Value("${jwt.expireTime}")
	private Long jwtExpireTime;

	@Autowired
	JwtUtils jwtUtils;


	@Override
	public Userinfo getbyphone(String phone) {
		LambdaQueryWrapper<Userinfo> userinfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
		userinfoLambdaQueryWrapper.eq(Userinfo::getPhone, phone);
		return getOne(userinfoLambdaQueryWrapper);
	}

	@Override
	public void register(RegInfo regInfo) {
		//判断手机号是否已注册
		if(getbyphone(regInfo.getPhone())!=null){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"手机号已注册");
		};
		//判断验证码是否正确
		String key = UserRedisKeyPrefix.USERS_REGISTER_VERIFYCODE.fullKey(regInfo.getPhone());//拼接key
		if(!(redisCache.getCacheObject(key).equals(regInfo.getVerifycode()))){
			throw  new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
		};
		//删除redis中验证码
		redisCache.deleteObject(key);
		//使用MD5加密算法对密码加密
		String md5 = Md5Utils.getMD5(regInfo.getPassword()+regInfo.getPhone());//使用getphone简单加盐
		//封装用户信息保存用户到数据库
		Userinfo newuser = new Userinfo();
		newuser.setPhone(regInfo.getPhone());
		newuser.setNickname(regInfo.getNickname());
		newuser.setPassword(md5);
		this.save(newuser);
	}

	@Override
	public BaseResponse<Map<String, Object>> login(String phone, String password) {
		//查询用户，如为空则抛出异常
		Userinfo user = getbyphone(phone);
		if(user==null){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
		}
		//校验密码
		if(!user.getPassword().equals(Md5Utils.getMD5(password+phone))){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"登录密码错误");
		};
		//复制查询到的用户到登录用户
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(user,loginUser);
		//填充登录时间和过期时间
		long currentTimeMillis = System.currentTimeMillis();
		loginUser.setLoginTime(currentTimeMillis);
		loginUser.setExpireTime(currentTimeMillis+jwtExpireTime);//24小时过期
		//保存用户信息到redis
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fullKey = UserRedisKeyPrefix.USERS_LOGIN_INFO.fullKey(uuid);
		redisCache.setCacheObject(fullKey,loginUser,loginUser.getExpireTime(), TimeUnit.MILLISECONDS);//uuid为唯一标识
		//生成token,把用户进本信息填入payload中
		Map<String , Object> payload = new HashMap<>();
		payload.put("uuid",uuid);//uuid为唯一标识,用于从redis取得用户登录信息
		String token = jwtUtils.generateToken(payload);

		//按前端要求格式封装返回值
		HashMap<String, Object> map = new HashMap<>();
		map.put("token",token);
		map.put("user",loginUser);
		return ResultUtils.success(map);
	}
}




