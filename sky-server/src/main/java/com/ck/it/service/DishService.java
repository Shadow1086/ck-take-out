package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.DishDTO;
import com.ck.it.dto.DishPageQueryDTO;
import com.ck.it.entity.Dish;
import com.ck.it.result.PageResult;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 22:21
 */
public interface DishService extends IService<Dish> {

	public void saveWithFlavor(DishDTO dto);

	PageResult queryDishPage(DishPageQueryDTO dto);

	Integer deleteDishes(String ids);
}
