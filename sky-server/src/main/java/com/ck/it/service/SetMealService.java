package com.ck.it.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.entity.Setmeal;
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
	List<SetmealVO> queryByCategoryId(Integer categoryId);
}
