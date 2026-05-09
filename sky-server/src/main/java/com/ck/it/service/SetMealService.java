package com.ck.it.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.SetmealDTO;
import com.ck.it.dto.SetmealPageQueryDTO;
import com.ck.it.entity.Setmeal;
import com.ck.it.entity.SetmealDish;
import com.ck.it.result.PageResult;
import com.ck.it.vo.DishItemVO;
import com.ck.it.vo.SetmealVO;

import java.util.List;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 00:36
 */
public interface SetMealService extends IService<Setmeal> {
	/**
	 *  根据分类id查询套餐
	 *
	 * @param categoryId
	 * @return {@link List }<{@link SetmealVO }>
	 */
	List<SetmealVO> queryByCategoryId(Integer categoryId);

	/**
	 *  根据套餐id查询包含的菜品
	 *
	 * @param id
	 * @return {@link List }<{@link DishItemVO }>
	 */
	List<DishItemVO> queryBySetMealId(Integer id);

	/**
	 *  新增套餐
	 *
	 * @param dto
	 */
	void addSetmeal(SetmealDTO dto);

	/**
	 *  分页查询套餐
	 *
	 * @param dto
	 * @return {@link PageResult }
	 */
	PageResult queryPage(SetmealPageQueryDTO dto);
}
