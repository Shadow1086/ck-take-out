package com.ck.it.controller.user;

import com.ck.it.entity.Dish;
import com.ck.it.result.Result;
import com.ck.it.service.DishService;
import com.ck.it.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

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

	@GetMapping("/list")
	@Operation(summary = "根据分类id查询菜品")
	public Result<List<DishVO>> queryByCategory(@RequestParam("categoryId") Integer categoryId){
		List<DishVO> dishVOS = dishService.queryByCategoryId(categoryId);

		return Result.success(dishVOS);
	}
}
