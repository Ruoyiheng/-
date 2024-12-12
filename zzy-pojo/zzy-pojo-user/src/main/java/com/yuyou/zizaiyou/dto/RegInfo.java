package com.yuyou.zizaiyou.dto;

import lombok.Data;

/**
 * @Description:接受和返回前端注册信息参数
 * @Author: xa5fun
 * @Version: 1.0
 */
@Data
public class RegInfo {

	private String phone;

	private String nickname;

	private String password;

	private String verifycode; //验证码
}
