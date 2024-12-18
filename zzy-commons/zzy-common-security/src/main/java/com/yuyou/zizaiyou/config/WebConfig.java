package com.yuyou.zizaiyou.config;
import com.yuyou.zizaiyou.jwt.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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

	/**
	 * 注册登录授权拦截器，拦截无有效token的请求
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册拦截器并设置拦截路径
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**") // 设置拦截的路径
				.excludePathPatterns("/users/register,/users/login"); // 排除不需要拦截的路径
	}


	/**
	 * 启用全局 CORS,允许跨域请求
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 匹配所有路径
		registry.addMapping("/**") // 匹配所有路径
				.allowedOrigins("*") // 允许的来源
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
				.allowedHeaders("Content-Type", "Authorization","token") // 允许的请求头
				.allowCredentials(true) // 是否允许携带凭证
				.maxAge(3600); // 预检请求的缓存时间
	}

}