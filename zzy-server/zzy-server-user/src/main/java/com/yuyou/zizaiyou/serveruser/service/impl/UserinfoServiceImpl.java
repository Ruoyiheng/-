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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
		//生成token
		Map<String , Object> payload = new HashMap<>();
		payload.put("id",user.getId());
		payload.put("phone",user.getPhone());
		payload.put("nickname",user.getNickname());
		String token = JwtUtils.generateToken(payload);
		//user信息脱敏
		user.setPassword("");
		//按前端要求格式封装返回值
		HashMap<String, Object> map = new HashMap<>();
		map.put("token",token);
		map.put("user",user);
		return ResultUtils.success(map);
	}
}




