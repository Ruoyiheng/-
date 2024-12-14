package com.yuyou.zizaiyou.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @Description: jwt工具类
 * @Author: xa5fun
 * @Version: 1.0
 */
@Component
public class JwtUtils {

	private static final String SECRET_KEY = "123456"; // 替换成实际密钥
	private static final long EXPIRATION_TIME = 1000*60*60*2; // JWT 有效期，单位为毫秒（2小时）

	// 生成 JWT
	public static String generateToken(Map payload) {
		return Jwts.builder()
				.setClaims(payload)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				.compact();
	}
	// 验证签名是否有效
	public boolean validateSignature(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token); // 如果签名无效会抛出异常
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	// 从 JWT 中提取用户名
	public static String getUsernameFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.getSubject();
	}

	// 获取 JWT 的有效期
	public static Date getExpirationDateFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.getExpiration();
	}

	// 从 token 中提取 claims
	private static Claims getClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

	// 验证 token 是否过期
	public static boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// 验证 token
	public static boolean validateToken(String token, String username) {
		return username.equals(getUsernameFromToken(token)) && !isTokenExpired(token);
	}
}