package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ck.it.dto.DishPageQueryDTO;
import com.ck.it.entity.Dish;
import com.ck.it.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    Integer countByCategoryId(Long categoryId);

	IPage<DishVO> queryPage(@Param("page") IPage<DishVO> page,@Param("dto") DishPageQueryDTO dto);
}
