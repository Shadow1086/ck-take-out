package com.ck.it.controller.user;

import com.ck.it.dto.ShoppingCartDTO;
import com.ck.it.result.Result;
import com.ck.it.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Result add(@RequestBody ShoppingCartDTO dto){
		log.info("添加购物车，商品信息：{}",dto);
		shoppingCartService.add(dto);

		return Result.success();
	}

}
