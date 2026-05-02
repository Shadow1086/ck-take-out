package com.ck.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.dto.DishDTO;
import com.ck.it.entity.Dish;
import com.ck.it.entity.DishFlavor;
import com.ck.it.mapper.DishFlavorMapper;
import com.ck.it.mapper.DishMapper;
import com.ck.it.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 22:21
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
	@Autowired
	private DishMapper dishMapper;

	@Autowired
	private DishFlavorMapper flavorMapper;

//	private

	@Override
	@Transactional
	public void saveWithFlavor(DishDTO dto) {
		/// 向菜品表插入一条数据
		Dish dish = new Dish();
		dish = Dish.builder().name(dto.getName())
				.categoryId(dto.getCategoryId())
				.price(dto.getPrice())
				.image(dto.getImage())
				.description(dto.getDescription())
				.status(dto.getStatus()).build();

		dishMapper.insert(dish);
		Long id = dish.getId();
		/// 向口味表插入n条数据
		List<DishFlavor> flavors = dto.getFlavors();
		if(flavors!=null && !flavors.isEmpty()){
			flavors.forEach(flavor -> flavor.setDishId(id));
			flavorMapper.insert(flavors);
		}
	}
}
