package com.ck.it.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.ShoppingCartDTO;
import com.ck.it.entity.ShoppingCart;
import com.ck.it.result.Result;
import com.ck.it.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Package: com.ck.it.controller.user
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 16:53
 */
@RestController
@Tag(name = "购物车接口")
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService shoppingCartService;

	@PostMapping("/add")
	@Operation(summary = "添加购物车")
	public Result add(@RequestBody ShoppingCartDTO dto) {
		log.info("添加购物车，商品信息：{}", dto);
		shoppingCartService.add(dto);

		return Result.success();
	}

	@GetMapping("list")
	@Operation(summary = "查看购物车")
	public Result<List<ShoppingCart>> list() {
		List<ShoppingCart> list = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));

		return Result.success(list);
	}

	@DeleteMapping("clean")
	@Operation(summary = "清空购物车")
	public Result<Object> clean() {
		boolean remove = shoppingCartService.remove(new LambdaUpdateWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
		return Result.success(remove);
	}

	@PostMapping("sub")
	@Operation(summary = "删除购物车中某一商品")
	public Result sub(@RequestBody ShoppingCartDTO dto) {
		log.info("删除购物车中商品：{}", dto);
		return Result.success(shoppingCartService.removeItem(dto));
	}
}
