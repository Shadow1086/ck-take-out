package com.ck.it.controller.admin;

import com.ck.it.dto.SetmealDTO;
import com.ck.it.dto.SetmealPageQueryDTO;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.SetMealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 16:01
 */
@RestController("adminSetMealController")
@Slf4j
@Tag(name = "套餐相关接口")
@RequiredArgsConstructor
@RequestMapping("/admin/setmeal")
public class SetMealController {
	private final SetMealService setMealService;
	// TODO 完成套餐相关接口

	/**
	 * 新增套餐
	 *
	 * @param dto
	 * @return {@link Result }
	 */
	@PostMapping
	@Operation(summary = "新增套餐")
	public Result addSetmeal(@RequestBody SetmealDTO dto) {
		log.info("新增套餐：{}", dto);
		setMealService.addSetmeal(dto);
		return Result.success();
	}

	/**
	 * 分页查询套餐
	 *
	 * @param dto
	 * @return {@link Result }<{@link PageResult }>
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询套餐")
	public Result<PageResult> page(SetmealPageQueryDTO dto) {
		log.info("分页查询套餐：{}", dto);
		return Result.success(setMealService.queryPage(dto));
	}

	/**
	 * 套餐停售/起售
	 *
	 * @param status
	 * @return {@link Result }
	 */
	@PostMapping("/status/{status}")
	@Operation(summary = "套餐停售/起售")
	public Result changeStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
		log.info("套餐id为：{}   更改为：{}", id, status == 1 ? "起售" : "停售");
		setMealService.changeStatus(id, status);
		return Result.success();
	}
}
