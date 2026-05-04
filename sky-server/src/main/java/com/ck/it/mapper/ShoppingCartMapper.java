package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.entity.ShoppingCart;

import java.util.List;

/**
 * Package: com.ck.it.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 16:55
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
	List<ShoppingCart> list (ShoppingCart shoppingCart);
}
