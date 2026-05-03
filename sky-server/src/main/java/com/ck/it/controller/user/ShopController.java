package com.ck.it.controller.user;

import com.ck.it.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.ck.it.controller.user
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 19:14
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Tag(name = "用户端店铺相关接口")
public class ShopController {
	public static final String KEY = "SHOP_STATUS";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/status")
	@Operation(summary = "查询店铺状态")
	public Result<Integer> getStatus() {
		Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
		log.info("查询店铺状态：{}", shopStatus == 1 ? "营业中" : "打烊中");

		return Result.success(shopStatus);
	}
}
