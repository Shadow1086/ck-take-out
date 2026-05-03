package com.ck.it.controller.admin;

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
import org.springframework.web.bind.annotation.*;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * 菜品管理
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 22:18
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Tag(name = "菜品管理")
public class DishController {
	@Autowired
	private DishService dishService;

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

		return Result.success();
	}

	@GetMapping("{id}")
	@Operation(summary = "根据菜品id查询菜品")
	public Result<DishVO> queryById(@PathVariable("id") Long id) {
		log.info("根据id查询菜品，id：{}", id);
		DishVO dishVO = dishService.queryById(id);
		return Result.success(dishVO);
	}
}
