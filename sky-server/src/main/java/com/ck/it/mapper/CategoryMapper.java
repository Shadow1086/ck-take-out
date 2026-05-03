package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.annotation.AutoFill;
import com.ck.it.entity.Category;
import com.ck.it.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
//	@AutoFill(OperationType.INSERT)
	int insert(Category category);
}
