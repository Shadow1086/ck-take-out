package com.ck.it.controller.user;

import com.ck.it.result.Result;
import com.ck.it.service.DishService;
import com.ck.it.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Package: com.ck.it.controller.user
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 00:18
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Tag(name = "菜品浏览")
public class DishController {
	@Autowired
	private DishService dishService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/list")
	@Operation(summary = "根据分类id查询菜品")
	public Result<List<DishVO>> queryByCategory(@RequestParam("categoryId") Integer categoryId) {
		log.info("根据分类id查询菜品,id：{}",categoryId);
		/// 构造redis中的key,规则：dish_分类id
		String key = "dish_" + categoryId;
		/// 查询redis中是否存在菜品数据
		List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
		if (list != null && !list.isEmpty()) {
			/// 如果存在，直接返回，不需要查询数据库
			log.info("redis缓存取出数据");
			return Result.success(list);
		} else {
			/// 如果不存在，查询数据库，将数据放入redis中
		log.info("数据库中取出数据，放入redis中");
			List<DishVO> dishVOS = dishService.queryByCategoryId(categoryId);
			redisTemplate.opsForValue().set(key, dishVOS);
			return Result.success(dishVOS);
		}
	}
}
