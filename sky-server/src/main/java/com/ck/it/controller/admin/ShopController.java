package com.ck.it.controller.admin;

import com.ck.it.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 19:08
 */
@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
@Tag(name = "店铺相关接口")
public class ShopController {

	public static final String KEY = "SHOP_STATUS";

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 设置营业状态
	 *
	 * @param status
	 * @return {@link Result }
	 */
	@PutMapping("/{status}")
	@Operation(summary = "设置店铺的营业状态")
	public Result<Object> setStatus(@PathVariable Integer status) {
		log.info("设置店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(KEY, status);

		return Result.success();
	}

	@GetMapping("/status")
	@Operation(summary = "查询店铺状态")
	public Result<Integer> getStatus() {
		Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
		log.info("查询店铺状态：{}", shopStatus == 1 ? "营业中" : "打烊中");

		return Result.success(shopStatus);
	}
}
