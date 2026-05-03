package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.StatusConstant;
import com.ck.it.entity.Setmeal;
import com.ck.it.entity.SetmealDish;
import com.ck.it.mapper.SetmealDishMapper;
import com.ck.it.mapper.SetmealMapper;
import com.ck.it.service.SetMealService;
import com.ck.it.vo.DishItemVO;
import com.ck.it.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
