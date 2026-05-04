package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.ShoppingCartDTO;
import com.ck.it.entity.ShoppingCart;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 16:56
 */

public interface ShoppingCartService extends IService<ShoppingCart> {

	/**
	 *  添加购物车
	 *
	 * @param dto
	 */
	void add(ShoppingCartDTO dto);
}
