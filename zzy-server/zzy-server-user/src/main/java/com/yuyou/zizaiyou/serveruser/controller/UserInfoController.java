package com.yuyou.zizaiyou.serveruser.controller;

import com.yuyou.zizaiyou.domin.Userinfo;
import com.yuyou.zizaiyou.serveruser.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description
 * 用户信息查询
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {
	private final UserinfoService userinfoService; //采用构造器注入Service，这种注入方法可以添加final使得后续不被更改

	@Autowired
	public UserInfoController(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}
	@GetMapping("/list")
	public List<Userinfo> list(){
		return userinfoService.list();
	}
}
