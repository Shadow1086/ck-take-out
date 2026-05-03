package com.ck.it.controller.admin;

import com.ck.it.dto.DishDTO;
import com.ck.it.dto.DishPageQueryDTO;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 *     菜品管理
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

	@PostMapping
	@Operation(summary = "添加菜品")
	public Result save(@RequestBody DishDTO dto){
		log.info("新增菜品：{}",dto);
		dishService.saveWithFlavor(dto);

		return Result.success();
	}

	@GetMapping("/page")
	@Operation(summary = "分页查询菜品")
	public Result<PageResult> queryDishPage(DishPageQueryDTO dto){
		PageResult pageResult = dishService.queryDishPage(dto);

		return Result.success(pageResult);
	}

	@DeleteMapping
	@Operation(summary = "删除菜品")
	public Result<Integer> deleteDishes(String ids){
		Integer rows = dishService.deleteDishes(ids);
		if(rows>=1){
			return Result.success(rows);
		}
		return Result.error(null);
	}
}
