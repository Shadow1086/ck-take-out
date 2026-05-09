package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.ddl.IDdl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.StatusConstant;
import com.ck.it.dto.DishDTO;
import com.ck.it.dto.DishPageQueryDTO;
import com.ck.it.entity.Category;
import com.ck.it.entity.Dish;
import com.ck.it.entity.DishFlavor;
import com.ck.it.mapper.CategoryMapper;
import com.ck.it.mapper.DishFlavorMapper;
import com.ck.it.mapper.DishMapper;
import com.ck.it.result.PageResult;
import com.ck.it.service.DishService;
import com.ck.it.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Autowired
	private CategoryMapper categoryMapper;

	/**
	 * 新增菜品，以及菜品的口味
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

	/**
	 * 更新菜品信息
	 *
	 * @param dto
	 */
	@Override
	@Transactional
	public void updateDish(DishDTO dto) {
		if (dto != null && dto.getId() != null) {
			Dish dish = new Dish();
			BeanUtils.copyProperties(dto, dish);

			/// 修改菜品表的基本信息
			if (dishMapper.updateById(dish) != 0) {
				/// 删除口味再重新添加口味信息
				flavorMapper.delete(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dish.getId()));
				List<DishFlavor> flavors = dto.getFlavors();
				if (flavors != null && !flavors.isEmpty()) {
					flavors.forEach(flavor -> flavor.setDishId(dish.getId()));
					flavorMapper.insert(flavors);
				}
			}
		}
	}

	/**
	 * 根据id查询菜品信息
	 *
	 * @param id
	 * @return {@link DishVO }
	 */
	@Override
	public DishVO queryById(Long id) {
		if (id != null) {
			Dish dish = dishMapper.selectById(id);

			if (dish != null) {
				DishVO resultVo = new DishVO();
				Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
						.select(Category::getName).eq(Category::getId, dish.getCategoryId()));

				resultVo.setCategoryName(category.getName());
				List<DishFlavor> dishFlavors = flavorMapper.selectList(
						new LambdaQueryWrapper<DishFlavor>()
								.eq(DishFlavor::getDishId, dish.getId()));

				BeanUtils.copyProperties(dish, resultVo);
				resultVo.setFlavors(dishFlavors);

				return resultVo;
			}
			return null;
		}
		return null;
	}

	@Override
	public List<DishVO> queryByCategoryId(Integer categoryId) {
		List<Dish> dishes = dishMapper.selectList(
				new LambdaQueryWrapper<Dish>()
						.eq(Dish::getCategoryId, categoryId)
						.eq(Dish::getStatus, StatusConstant.ENABLE));
		if (dishes == null || dishes.isEmpty()) {
			return List.of();
		}
		/// 将查询到的dish中的id提取出来
		List<Long> dishids = dishes.stream().map(Dish::getId).toList();
		/// 查询所有dish.id对应的口味
		List<DishFlavor> flavors = flavorMapper.selectList(new LambdaQueryWrapper<DishFlavor>()
				.in(DishFlavor::getDishId, dishids));
		/// 将对应的口味和菜品对应存储到map中
		Map<Long, List<DishFlavor>> flavorMap = flavors.stream()
				.collect(Collectors.groupingBy(DishFlavor::getDishId));

		return dishes.stream().map(dish -> {
			/// 循环处理，将dish和口味封装为一个个vo对像
			DishVO dishVo = new DishVO();
			BeanUtils.copyProperties(dish, dishVo);
			dishVo.setFlavors(flavorMap.getOrDefault(dish.getId(), List.of()));
			return dishVo;
		}).toList();
	}

	/**
	 * 管理端根据套餐id查询菜品
	 *
	 * @param categoryId
	 * @return {@link List }<{@link Dish }>
	 */
	@Override
	public List<Dish> getByCategoryId(Long categoryId) {
		return dishMapper.selectList(new LambdaQueryWrapper<Dish>()
				.eq(Dish::getCategoryId, categoryId)
				.eq(Dish::getStatus, 1));
	}

	/**
	 * 菜品的起售与停售
	 *
	 * @param status
	 * @param id
	 * @return {@link Integer }
	 */
	@Override
	public Integer updateStatus(Integer status, Long id) {
		if(id!=null){
			if(dishMapper.selectById(id)!= null){
				Dish dish = new Dish();
				dish.setStatus(status);
				dish.setId(id);
				return dishMapper.updateById(dish);
			}
		}
		return 0;
	}
}
