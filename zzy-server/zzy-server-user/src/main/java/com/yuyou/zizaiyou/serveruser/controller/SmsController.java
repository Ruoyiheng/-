package com.yuyou.zizaiyou.serveruser.controller;


import com.yuyou.zizaiyou.commoncore.exception.BaseResponse;
import com.yuyou.zizaiyou.commoncore.exception.ResultUtils;
import com.yuyou.zizaiyou.serveruser.service.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author xa5fun
 * @Description 阿里云短信服务
 * @Param
 * @return
 **/
@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }
    /**
     * @Author xa5fun
     * @Description 用户注册发送短信验证码
     * @Param [phone]
     * @return com.yuyou.zizaiyou.commoncore.exception.BaseResponse<?>
     **/
    @PostMapping("/register")
    public BaseResponse<?> registerVerifyCode(@RequestParam String phone) {
        smsService.registerSmsSend(phone);
        return ResultUtils.success();
    }
}
