package com.yuyou.zizaiyou.serveruser.service.impl;

import com.yuyou.zizaiyou.commoncore.exception.BaseResponse;
import com.yuyou.zizaiyou.commoncore.exception.BusinessException;
import com.yuyou.zizaiyou.commoncore.exception.ErrorCode;
import com.yuyou.zizaiyou.redis.key.UserRedisKeyPrefix;
import com.yuyou.zizaiyou.redis.utils.RedisCache;
import com.yuyou.zizaiyou.serveruser.service.SmsService;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Value("${aliyun.dysms.templateCode}")
    private String templateCode;
    @Value("${aliyun.dysms.sign}")
    private String sign;


    private final RedisCache redisCache;
    private final Client smsClient;

    public SmsServiceImpl(RedisCache redisCache, Client smsClient) {
        this.redisCache = redisCache;
        this.smsClient = smsClient;
    }

    /**
     * @Author xa5fun
     * @Description 验证码功能整体流程
     * @Param [phone]
     * @return void
     **/
    @Override
    public void registerSmsSend(String phone) {
        // 1.验证手机号是否为有效的中国大陆手机号
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("请输入有效的中国大陆手机号");
        }
        // 2.限制每60s只能发送一次
        String key = "users:register:" + phone;
        if (redisCache.getExpire(key) >240) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"发送验证码过于频繁");
        }
        // 3. 生成验证码(纯数字, 字母+数字)
        String code = this.generateVerifyCode("MATH", 6);
        // 4. 调用第三方接口, 发送验证码
        try {
            this.send(phone, code);
        } catch (BusinessException re) {
            throw re;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 5. 将验证保存起来, 五分钟内有效
        redisCache.setCacheObject(UserRedisKeyPrefix.USERS_REGISTER_VERIFYCODE,code,phone);
    }


    /**
     * @Author xa5fun
     * @Description 设定要发送的手机号，短信模板，模板code变量（code即验证码）
     * @Param [phone, code]
     * @return void
     **/
    private void send(String phone, String code) throws Exception {
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest =
                new com.aliyun.dysmsapi20170525.models.SendSmsRequest().setTemplateCode(templateCode)
                        .setTemplateParam("{\"code\":\"" + code + "\"}")
                        .setPhoneNumbers(phone)
                        .setSignName(sign);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = smsClient.sendSmsWithOptions(sendSmsRequest, runtime);
        ObjectMapper mapper = new ObjectMapper();
        SendSmsResponseBody body = response.getBody();//拿到响应值
        String respJson = mapper.writeValueAsString(body);//转成json格式
        log.info("[短信服务] 阿里云发送短信响应结果: {}", respJson);

        if (!"ok".equalsIgnoreCase(body.code)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, body.message);
        }
    }

    /**
     * @Author xa5fun
     * @Description 生成验证码 支持math模式和uuid模式
     * @Param [type, len]
     * @return java.lang.String
     **/
    private String generateVerifyCode(String type, int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("验证码长度必须大于0");
        }
        StringBuilder code = new StringBuilder();
        if ("MATH".equalsIgnoreCase(type)) {
            Random random = new Random();
            for (int i = 0; i < len; i++) {
                code.append(random.nextInt(10));//生成0-9的随机数字追加到code
            }
        } else {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");//生成uuid替换“-”
            if (len > uuid.length()) {
                throw new IllegalArgumentException("验证码长度大于生成的uuid长度");//生成的uuid长度小于len抛出异常
            }
            code = new StringBuilder(uuid.substring(0, len));
        }
        log.info("[短信服务] 生成验证码 ====> type={}, len={}, code={}", type, len, code);
        return code.toString();
    }

    /**
     * @Author xa5fun
     * @Description 判断手机号格式是否正确
     * @Param [phone]
     * @return boolean
     **/
    private boolean isValidPhone(String phone) {
        // 中国大陆手机号格式：以1开头，第二位是3-9，后续9位数字
        String regex = "^1[3-9]\\d{9}$";
        return Pattern.matches(regex, phone);
    }

}
