package com.yuyou.zizaiyou.jwt;

import com.yuyou.zizaiyou.redis.key.UserRedisKeyPrefix;
import com.yuyou.zizaiyou.redis.utils.RedisCache;
import com.yuyou.zizaiyou.vo.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: jwt工具类
 * @Author: xa5fun
 * @Version: 1.0
 */
@Component
public class JwtUtils {

	@Value("${jwt.secretkey}")
	private String SECRET_KEY ; //替换成实际密钥

	@Value("${jwt.expireTime}")
	private long EXPIRATION_TIME ;//令牌默认过期时间

	@Autowired
	RedisCache redisCache;

	// 生成 JWT
	public  String generateToken(Map payload) {
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
	public  String getUsernameFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.getSubject();
	}

	// 获取 JWT 的有效期
	public  Date getExpirationDateFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return claims.getExpiration();
	}

	// 从 token 中提取 claims
	private  Claims getClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
	}

	// 验证 token 是否过期
	public  boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//根据redis存放的用户登录信息判断token是否过期
	public  boolean isTokenExpiredByRedis(String token) {
		//从token中取到uuid
		Claims claims = getClaimsFromToken(token);
		String uuid = (String) claims.get("uuid");
		//根据{登录信息前缀+uuid}查询数据库
		String fullKey = UserRedisKeyPrefix.USERS_LOGIN_INFO.fullKey(uuid);
		LoginUser loginUser = redisCache.getCacheObject(fullKey);
		//不能查到用户则token已过期 能查到则未过期
		if(loginUser == null){
			return true;
		}else{
			 Long logintime;//登录时间
			//判断距离过期时间是否小于6小时，如果小于则续期
			if (loginUser.getExpireTime() - (logintime = System.currentTimeMillis()) < 6 * 60 * 60 * 1000) {
				loginUser.setLoginTime(logintime);
				loginUser.setExpireTime(logintime + EXPIRATION_TIME);//过期时间
				redisCache.setCacheObject(fullKey,loginUser,loginUser.getExpireTime(), TimeUnit.MILLISECONDS);//更新redis的登录信息
			}
		}
		return  false;
	}

	// 验证 token
	public  boolean validateToken(String token, String username) {
		return username.equals(getUsernameFromToken(token)) && !isTokenExpired(token);
	}
}