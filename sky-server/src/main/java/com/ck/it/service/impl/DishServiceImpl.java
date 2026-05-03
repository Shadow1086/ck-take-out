package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.dto.DishDTO;
import com.ck.it.dto.DishPageQueryDTO;
import com.ck.it.entity.Dish;
import com.ck.it.entity.DishFlavor;
import com.ck.it.mapper.DishFlavorMapper;
import com.ck.it.mapper.DishMapper;
import com.ck.it.result.PageResult;
import com.ck.it.service.DishService;
import com.ck.it.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

	/**
	 *  新增菜品，以及菜品的口味
	 *
	 * @param dto
	 */
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
		if (flavors != null && !flavors.isEmpty()) {
			flavors.forEach(flavor -> flavor.setDishId(id));
			flavorMapper.insert(flavors);
		}
	}

	@Override
	public PageResult queryDishPage(DishPageQueryDTO dto) {
		IPage<DishVO> page = new Page<>(dto.getPage(), dto.getPageSize());

		IPage<DishVO> resultPage = dishMapper.queryPage(page, dto);

		PageResult pageResult = new PageResult();
		pageResult.setTotal(resultPage.getTotal());
		pageResult.setRecords(resultPage.getRecords());

		return pageResult;
	}

	/**
	 * 批量删除菜品
	 *
	 * @param ids id1,id2,id3
	 * @return {@link Integer }
	 */
	@Override
	@Transactional
	public Integer deleteDishes(String ids) {
		if (ids != null && !ids.isBlank()) {
			List<Long> idList = Arrays.stream(ids.split(","))
					.map(Long::valueOf).toList();
			flavorMapper.delete(
					new LambdaQueryWrapper<DishFlavor>()
							.in(DishFlavor::getDishId, idList));
			return dishMapper.deleteByIds(idList);
		}
		return 0;
	}
}
