package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.StatusConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.SetmealDTO;
import com.ck.it.dto.SetmealPageQueryDTO;
import com.ck.it.entity.Setmeal;
import com.ck.it.entity.SetmealDish;
import com.ck.it.mapper.SetmealDishMapper;
import com.ck.it.mapper.SetmealMapper;
import com.ck.it.result.PageResult;
import com.ck.it.service.SetMealService;
import com.ck.it.vo.DishItemVO;
import com.ck.it.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 00:38
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {
	@Autowired
	private SetmealMapper setmealMapper;
	@Autowired
	private SetmealDishMapper setmealDishMapper;

	@Override
	public List<SetmealVO> queryByCategoryId(Integer categoryId) {
		List<Setmeal> setmeals = setmealMapper.selectList(new LambdaQueryWrapper<Setmeal>()
				.eq(Setmeal::getCategoryId, categoryId)
				.eq(Setmeal::getStatus, StatusConstant.ENABLE));

		if (setmeals == null || setmeals.isEmpty()) {
			return List.of();
		}

		List<Long> ids = setmeals.stream().map(Setmeal::getId).toList();

		List<SetmealDish> setmealDishes = setmealDishMapper.selectList(new LambdaQueryWrapper<SetmealDish>()
				.in(SetmealDish::getSetmealId, ids));

		Map<Long, List<SetmealDish>> dishMap = setmealDishes.stream().collect(Collectors.groupingBy(SetmealDish::getSetmealId));

		return setmeals.stream().map(setmealDish -> {
			SetmealVO setmealVO = new SetmealVO();
			BeanUtils.copyProperties(setmealDish, setmealVO);
			setmealVO.setSetmealDishes(dishMap.getOrDefault(setmealDish.getId(), List.of()));
			return setmealVO;
		}).toList();
	}

	/**
	 * 根据套餐id查询包含的菜品
	 *
	 * @param id
	 * @return {@link List }<{@link DishItemVO }>
	 */
	@Override
	public List<DishItemVO> queryBySetMealId(Integer id) {
		List<DishItemVO> dishItemVOS = setmealMapper.queryBySetMealId(id);

		return dishItemVOS;
	}


	/**
	 * 新增套餐
	 *
	 * @param dto
	 */
	@Override
	@Transactional
	public void addSetmeal(SetmealDTO dto) {
		if (dto == null) {
			return;
		}
		Setmeal setmeal = new Setmeal();
		BeanUtils.copyProperties(dto, setmeal);

//		setmeal.setUpdateTime(LocalDateTime.now());
		setmeal.setCreateUser(BaseContext.getCurrentId());
		setmeal.setUpdateUser(BaseContext.getCurrentId());

		List<SetmealDish> setmealDishes = dto.getSetmealDishes();
		if (setmealDishes == null) {
			return;
		}
		int insert = setmealMapper.insert(setmeal);
		if (insert == 1) {
			for (SetmealDish setmealDish : setmealDishes) {
				setmealDish.setSetmealId(setmeal.getId());
			}
			setmealDishMapper.insert(setmealDishes);
		}
	}

	/**
	 * 分页查询套餐
	 *
	 * @param dto
	 * @return {@link PageResult }
	 */
	@Override
	public PageResult queryPage(SetmealPageQueryDTO dto) {
		IPage<Setmeal> queryPage = new Page<>(dto.getPage(), dto.getPageSize());

		LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
		if (dto.getName() != null) {
			wrapper.like(Setmeal::getName, dto.getName());
		}
		if (dto.getCategoryId() != null) {
			wrapper.eq(Setmeal::getCategoryId, dto.getCategoryId());
		}
		if (dto.getStatus() != null) {
			wrapper.eq(Setmeal::getStatus, dto.getStatus());
		}

		IPage<Setmeal> resultPage = setmealMapper.selectPage(queryPage, wrapper);

		PageResult pageResult = new PageResult();
		pageResult.setTotal(resultPage.getTotal());

		List<Setmeal> records = resultPage.getRecords();
		List<SetmealVO> list = records.stream().map(record -> {
			SetmealVO setmealVO = new SetmealVO();
			BeanUtils.copyProperties(record, setmealVO);
			return setmealVO;
		}).toList();

		pageResult.setRecords(list);
		return pageResult;
	}
}







