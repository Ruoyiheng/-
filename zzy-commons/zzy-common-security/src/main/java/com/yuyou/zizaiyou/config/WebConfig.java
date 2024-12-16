package com.yuyou.zizaiyou.config;
import com.yuyou.zizaiyou.jwt.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @Description:
 * @Author: xa5fun
 * @Version: 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	LoginInterceptor loginInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册拦截器并设置拦截路径
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**") // 设置拦截的路径
				.excludePathPatterns("/users/register,/users/login"); // 排除不需要拦截的路径
	}
}