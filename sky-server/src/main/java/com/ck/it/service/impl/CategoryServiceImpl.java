package com.ck.it.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.MessageConstant;
import com.ck.it.constant.StatusConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.CategoryDTO;
import com.ck.it.dto.CategoryPageQueryDTO;
import com.ck.it.entity.Category;
import com.ck.it.exception.DeletionNotAllowedException;
import com.ck.it.mapper.CategoryMapper;
import com.ck.it.mapper.DishMapper;
import com.ck.it.mapper.SetmealMapper;
import com.ck.it.result.PageResult;
import com.ck.it.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private SetmealMapper setmealMapper;

	/**
	 * 新增分类
	 *
	 * @param categoryDTO
	 */
	public void save(CategoryDTO categoryDTO) {
		Category category = new Category();
		//属性拷贝
		BeanUtils.copyProperties(categoryDTO, category);

		//分类状态默认为禁用状态0
		category.setStatus(StatusConstant.DISABLE);

		//设置创建时间、修改时间、创建人、修改人
		category.setCreateTime(LocalDateTime.now());
		category.setUpdateTime(LocalDateTime.now());
		category.setCreateUser(BaseContext.getCurrentId());
		category.setUpdateUser(BaseContext.getCurrentId());

		categoryMapper.insert(category);
	}

	/**
	 * 分页查询
	 *
	 * @param dto
	 * @return
	 */
	public PageResult pageQuery(CategoryPageQueryDTO dto) {
		IPage<Category> page = new Page<>(dto.getPage(), dto.getPageSize());

		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(dto.getType()!=null , Category::getType, dto.getType())
				.like(dto.getName() != null && !dto.getName().isBlank(), Category::getName, dto.getName());

		IPage<Category> resultPage =  categoryMapper.selectPage(page,wrapper);

		PageResult pageResult = new PageResult();
		pageResult.setTotal(resultPage.getTotal());
		pageResult.setRecords(resultPage.getRecords());

		return pageResult;
	}

	/**
	 * 根据id删除分类
	 *
	 * @param id
	 */
	public void deleteById(Long id) {
		//查询当前分类是否关联了菜品，如果关联了就抛出业务异常
		Integer count = dishMapper.countByCategoryId(id);
		if (count > 0) {
			//当前分类下有菜品，不能删除
			throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
		}

		//查询当前分类是否关联了套餐，如果关联了就抛出业务异常
		count = setmealMapper.countByCategoryId(id);
		if (count > 0) {
			//当前分类下有菜品，不能删除
			throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
		}

		//删除分类数据
		categoryMapper.deleteById(id);
	}

	/**
	 * 修改分类
	 *
	 * @param categoryDTO
	 */
	public void update(CategoryDTO categoryDTO) {
		Category category = new Category();
		BeanUtils.copyProperties(categoryDTO, category);

		//设置修改时间、修改人
		category.setUpdateTime(LocalDateTime.now());
		category.setUpdateUser(BaseContext.getCurrentId());

		categoryMapper.updateById(category);
	}

	/**
	 * 启用、禁用分类
	 *
	 * @param status
	 * @param id
	 */
	public void startOrStop(Integer status, Long id) {
		Category category = Category.builder()
				.id(id)
				.status(status)
				.updateTime(LocalDateTime.now())
				.updateUser(BaseContext.getCurrentId())
				.build();
		categoryMapper.updateById(category);
	}

	/**
	 * 根据类型查询分类
	 *
	 * @param type
	 * @return
	 */
	public List<Category> list(Integer type) {
		LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Category::getType, type);

		return categoryMapper.selectList(wrapper);
	}
}
