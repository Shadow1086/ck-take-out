package com.ck.it.controller.user;

import com.ck.it.constant.JwtClaimsConstant;
import com.ck.it.dto.UserLoginDTO;
import com.ck.it.entity.User;
import com.ck.it.properties.JwtProperties;
import com.ck.it.result.Result;
import com.ck.it.service.UserService;
import com.ck.it.utils.JwtUtil;
import com.ck.it.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.ck.it.controller.user
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 22:34
 */
@RestController
@RequestMapping("/user/user")
@Slf4j
@Tag(name = "用户登录注册接口")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtProperties jwtProperties;

	@Operation(summary = "微信用户登录")
	@PostMapping("/login")
	public Result<UserLoginVO> login(@RequestBody UserLoginDTO dto) {
		log.info("微信用户登录：{}", dto);
		User user = userService.wxLogin(dto);

		/// 为为新用户生成jwt令牌
		Map<String, Object> claims = new HashMap<>();
		claims.put(JwtClaimsConstant.USER_ID, user.getId());
		String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),
				jwtProperties.getUserTtl(),
				claims);

		UserLoginVO userLoginVO = UserLoginVO.builder()
				.id(user.getId())
				.openid(user.getOpenid())
				.token(token)
				.build();

		return Result.success(userLoginVO);
	}
}
