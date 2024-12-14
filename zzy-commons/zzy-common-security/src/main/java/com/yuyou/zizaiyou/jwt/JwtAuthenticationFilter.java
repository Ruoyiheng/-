package com.yuyou.zizaiyou.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Description: 配置JWT过滤器
 * @Author: xa5fun
 * @Version: 1.0
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtUtils jwtUtils;

	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}


	@Override
	protected void doFilterInternal(HttpServletRequest request, javax.servlet.http.HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		 //放行登录接口
		String requestURI = request.getRequestURI();
		log.info("requestURI:{}",requestURI);
		if ("/users/login".equals(requestURI)) {
			chain.doFilter(request, response); // 放行
			return;
		}

	/*	String token = request.getHeader("Authorization");
		// 如果没有Authorization头或者Authorization头不以"Bearer "开头，返回401 Unauthorized
		if (token == null || !token.startsWith("Bearer ")) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Authorization token is missing or invalid.");
			return;  // 直接返回，不继续调用后续过滤器
		}*/

		String token = request.getHeader("token");//从请求头key为token的字段中拿到token
		if(token==null){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Authorization token is missing");
			return;
		}
			try {
				/*// 获取实际的 JWT 部分，去掉 "Bearer " 前缀
				token = token.substring(7);*/

				//判断签名是否正确
				if(!jwtUtils.validateSignature(token)){
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong signature ");
					return;
				}
				//判断token是否过期（true表示过期）
				if (jwtUtils.isTokenExpired(token)){
					// Token 过期，返回 401 错误
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Expired token");
					return;
				}

			} catch (ExpiredJwtException e) {
				// 处理token异常
				log.info("{}",e);
			}


		chain.doFilter(request, response);
	}
}