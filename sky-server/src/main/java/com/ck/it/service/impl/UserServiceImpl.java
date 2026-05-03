package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.MessageConstant;
import com.ck.it.dto.UserLoginDTO;
import com.ck.it.entity.User;
import com.ck.it.exception.LoginFailedException;
import com.ck.it.mapper.UserMapper;
import com.ck.it.properties.WeChatProperties;
import com.ck.it.service.UserService;
import com.ck.it.utils.HttpClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 22:42
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	/// 微信服务接口地址
	public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private WeChatProperties weChatProperties;

	@Override
	public User wxLogin(UserLoginDTO dto) {
		String openid = getOpenId(dto.getCode());
		/// 判断openid是否为空，如果为空表示登录失败，抛出业务异常
		if (openid == null) {
			throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
		}
		/// 判断当前用户是否为新用户
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(User::getOpenid, openid);
		User user = userMapper.selectOne(wrapper);
		/// 如果是新用户，自动完成注册
		if (user == null) {
			user = User.builder()
					.openid(openid)
					.build();
			userMapper.insert(user);
		}
		/// 返回这个用户对象
		return user;
	}

	/**
	 * 通过调用微信的接口获取用户的openid唯一标识
	 *
	 * @param code 授权码
	 * @return {@link String }
	 */
	private String getOpenId(String code) {
		/// 调用微信服务接口，获取当前为新用户的openid
		Map<String, String> map = new HashMap<>();
		map.put("appid", weChatProperties.getAppid());
		map.put("secret", weChatProperties.getSecret());
		map.put("js_code", code);
		map.put("grant_type", "authorization_code");

		String json = HttpClientUtil.doGet(WX_LOGIN, map);
		String openid;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(json);
			openid = jsonNode.path("openid").asText(null);
		} catch (JsonProcessingException e) {
			throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
		}
		return openid;
	}
}
