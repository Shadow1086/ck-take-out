package com.ck.it.controller.user;

import com.ck.it.entity.Setmeal;
import com.ck.it.result.Result;
import com.ck.it.service.SetMealService;
import com.ck.it.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * {@code @Create} 2026-2026/5/4 00:38
 */
@RestController
@RequestMapping("/user/setmeal")
@Slf4j
@Tag(name = "套餐浏览接口")
public class SetMealController {
	@Autowired
	private SetMealService setMealService;

	@GetMapping("/list")
	@Operation(summary = "根据分类id查询套餐")
	public Result<List<SetmealVO>> queryByCategoryId(@RequestParam("categoryId") Integer categoryId) {
		List<SetmealVO> setmealVOS = setMealService.queryByCategoryId(categoryId);

		return Result.success(setmealVOS);
	}
}
