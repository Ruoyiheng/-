package com.yuyou.zizaiyou.jwt;
import com.yuyou.zizaiyou.annotation.LoginRequired;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Description:
 * @Author: xa5fun
 * @Version: 1.0
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

	private JwtUtils jwtUtils;

	public LoginInterceptor(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}
	// 请求处理前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		// 检查controller控制器类是否有 @LoginRequired 注解
		Class<?> controllerClass = handlerMethod.getBeanType();
		LoginRequired classAnnotation = controllerClass.getAnnotation(LoginRequired.class);

		// 检查请求方法是否有 @LoginRequired 注解
		LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);

		// 如果方法或类上有 @LoginRequired 注解，则需要登录
		if (classAnnotation != null || methodAnnotation != null) {
			// 执行需要登录的逻辑，例如检查用户是否已登录

			String token = request.getHeader("token");//从请求头key为token的字段中拿到token
			if(token==null){
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Authorization token is missing");
				return false;
			}
			try {
				/*// 获取实际的 JWT 部分，去掉 "Bearer " 前缀
				token = token.substring(7);*/

				//判断签名是否正确
				if(!jwtUtils.validateSignature(token)){
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong signature");
					return false;
				}
				//从redis查询用户登录信息,判断token是否过期（true表示过期）,并判断token是否需要续期
				if (jwtUtils.isTokenExpiredByRedis(token)){
					// Token 过期，返回 401 错误
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired token");
					return false;
				}

			} catch (ExpiredJwtException e) {
				// 处理token异常
				log.info("{}",e);
			}
			//token有效放行
			return  true;
		}

		return true;  // 没有@LoginRequired注解 放行
	}

}