package com.yuyou.zizaiyou.serveruser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuyou.zizaiyou.commoncore.exception.BusinessException;
import com.yuyou.zizaiyou.commoncore.exception.ErrorCode;
import com.yuyou.zizaiyou.commoncore.exception.Md5Utils;
import com.yuyou.zizaiyou.dto.RegInfo;
import com.yuyou.zizaiyou.po.Userinfo;
import com.yuyou.zizaiyou.redis.utils.RedisCache;
import com.yuyou.zizaiyou.serveruser.mapper.UserinfoMapper;
import com.yuyou.zizaiyou.serveruser.service.UserinfoService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
		String key = "users:register:" + regInfo.getPhone();
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
}




