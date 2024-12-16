package com.yuyou.zizaiyou.serveruser.controller;

import com.yuyou.zizaiyou.annotation.LoginRequired;
import com.yuyou.zizaiyou.commoncore.exception.BaseResponse;
import com.yuyou.zizaiyou.commoncore.exception.ResultUtils;
import com.yuyou.zizaiyou.dto.RegInfo;
import com.yuyou.zizaiyou.po.Userinfo;
import com.yuyou.zizaiyou.serveruser.service.UserinfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description
 * 用户信息查询
 */
@RestController
@RequestMapping("/users")
public class UserInfoController {
	private final UserinfoService userinfoService; //采用构造器注入Service，这种注入方法可以添加final使得后续不被更改

	public UserInfoController(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}

	@LoginRequired
	@GetMapping("/list")
	public BaseResponse<List<Userinfo>> list() {
		return ResultUtils.success(userinfoService.list());
	}

	/**
	 * 判断手机号是否已注册
	 * @param phone
	 * @return
	 */
	@GetMapping("phone/exists")
	@LoginRequired
	public BaseResponse<Boolean> getbyphone(@RequestParam String phone) {
		//存在用户返回true 不存在返回false
		return ResultUtils.success(userinfoService.getbyphone(phone)!=null);
	}

	/**
	 * 注册接口
	 * @param regInfo 前端注册参数
	 * @return
	 */
	@PostMapping("/register")
	public BaseResponse<?> register(@RequestBody RegInfo regInfo) {
		userinfoService.register(regInfo);
		return ResultUtils.success();
	}
	/**
	 * 登录接口
	 * @param phone
	 * @param password
	 * @return
	 */
	@PostMapping("/login")
	public BaseResponse<Map<String,Object>> login(@RequestParam(name = "username")String phone,@RequestParam String password){
		BaseResponse<Map<String,Object>> loginmap = userinfoService.login(phone,password);
		return  loginmap;
	}
}
