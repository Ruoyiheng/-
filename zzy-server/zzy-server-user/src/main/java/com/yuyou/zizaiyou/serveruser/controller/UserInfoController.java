package com.yuyou.zizaiyou.serveruser.controller;

import com.yuyou.zizaiyou.serveruser.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * 用户信息查询
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {
	@Autowired
	UserinfoService userinfoService;
}
