package com.ck.it.controller.admin;

import com.aliyun.core.annotation.Path;
import com.ck.it.dto.DishDTO;
import com.ck.it.dto.DishPageQueryDTO;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.DishService;
import com.ck.it.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * 菜品管理
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 22:18
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
@Tag(name = "菜品管理")
public class DishController {
	@Autowired
	private DishService dishService;

	@Autowired
	private RedisTemplate<String ,Object> redisTemplate;

	/**
	 * 添加菜品信息
	 *
	 * @param dto
	 * @return {@link Result }
	 */
	@PostMapping
	@Operation(summary = "添加菜品")
	public Result save(@RequestBody DishDTO dto) {
		log.info("新增菜品：{}", dto);
		dishService.saveWithFlavor(dto);
		/// 清理缓存数据
		String key = "dish_"+dto.getCategoryId();
		redisTemplate.delete(key);

		return Result.success();
	}

	/**
	 * 分页查询菜品信息
	 *
	 * @param dto
	 * @return {@link Result }<{@link PageResult }>
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询菜品")
	public Result<PageResult> queryDishPage(DishPageQueryDTO dto) {
		log.info("分页查询菜品：{}", dto);
		PageResult pageResult = dishService.queryDishPage(dto);

		return Result.success(pageResult);
	}

	/**
	 * 根据id删除菜品，可以批量，使用,隔开id
	 *
	 * @param ids
	 * @return {@link Result }<{@link Integer }>
	 */
	@DeleteMapping
	@Operation(summary = "删除菜品")
	public Result<Integer> deleteDishes(@RequestParam("ids") String ids) {
		log.info("删除菜品：{}", ids);
		Integer rows = dishService.deleteDishes(ids);
		if (rows >= 1) {
			/// 清理所有菜品缓存数据，所有以dish_开头的key
			Set<String> keys = redisTemplate.keys("dish_*");
			redisTemplate.delete(keys);
			return Result.success(rows);
		}
		return Result.error(null);
	}

	/**
	 * 更新菜品信息
	 *
	 * @param dto
	 * @return {@link Result }
	 */
	@PutMapping
	@Operation(summary = "修改菜品信息")
	public Result updateDish(@RequestBody DishDTO dto) {
		log.info("修改菜品信息：{}", dto);
		dishService.updateDish(dto);
		/// 清理所有缓存数据
		Set<String> keys = redisTemplate.keys("dish_*");
		redisTemplate.delete(keys);

		return Result.success();
	}

	/**
	 *  根据菜品id查询菜品
	 *
	 * @param id
	 * @return {@link Result }<{@link DishVO }>
	 */
	@GetMapping("{id}")
	@Operation(summary = "根据菜品id查询菜品")
	public Result<DishVO> queryById(@PathVariable("id") Long id) {
		log.info("根据id查询菜品，id：{}", id);
		DishVO dishVO = dishService.queryById(id);
		return Result.success(dishVO);
	}

	/**
	 * 菜品停售/起售
	 *
	 * @param status
	 * @param id
	 * @return {@link Result }
	 */
	@PostMapping("/status/{status}")
	@Operation(summary = "菜品停售/起售")
	public Result<Integer> updateStatus(@PathVariable("status") Integer status,@RequestParam("id") Long id){
		log.info("菜品状态改为：{},id为：{}",status==1?"起售":"停售",id);
		Integer i = dishService.updateStatus(status, id);
		/// 清理缓存
		Set<String> keys = redisTemplate.keys("dish_*");
		redisTemplate.delete(keys);

		return Result.success(i);
	}
}
