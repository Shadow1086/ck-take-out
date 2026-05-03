package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.entity.Setmeal;
import com.ck.it.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {


	Integer countByCategoryId(Long id);

	List<DishItemVO> queryBySetMealId(@Param("id") Integer id);
}
