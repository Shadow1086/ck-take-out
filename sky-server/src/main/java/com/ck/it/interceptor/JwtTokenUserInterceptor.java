package com.ck.it.interceptor;

import com.ck.it.constant.JwtClaimsConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.properties.JwtProperties;
import com.ck.it.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.interceptor
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 23:13
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

	@Autowired
	private JwtProperties jwtProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/// 获取当前线程id
		log.info("用户端请求拦截...当前线程:{}", Thread.currentThread().threadId());
		/// 判断当前拦截到的是Controller的方法还是其他资源
		if(!(handler instanceof HandlerMethod)){
			///当前拦截到的不是动态方法，直接放行
			return true;
		}
		/// 从请求头中获取令牌
		String token = request.getHeader(jwtProperties.getUserTokenName());
		/// 校验令牌
		try{
			log.info("校验令牌：{}",token);
			Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
			Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
			BaseContext.setCurrentId(userId);
			/// 通过放行
			return true;
		}catch (Exception e ){
			/// 不通过响应401状态码
			response.setStatus(401);
			return false;
		}
	}
}
