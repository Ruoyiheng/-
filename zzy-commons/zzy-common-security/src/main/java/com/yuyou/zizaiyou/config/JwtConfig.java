package com.yuyou.zizaiyou.config;

/**
 * @Description:注册过滤器bean
 * @Author: xa5fun
 * @Version: 1.0
 */
import com.yuyou.zizaiyou.jwt.JwtAuthenticationFilter;
import com.yuyou.zizaiyou.jwt.JwtUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig  {

	private final JwtUtils jwtUtils;

	public JwtConfig(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterbean() {//把jwtAuthenticationFilter注册为过滤器
		FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtAuthenticationFilter(jwtUtils));
		registrationBean.addUrlPatterns("/*");  // 配置需要拦截的 URL
		registrationBean.setOrder(-1);
		return registrationBean;
	}
}