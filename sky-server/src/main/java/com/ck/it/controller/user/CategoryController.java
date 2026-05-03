package com.ck.it.controller.user;

import com.aliyun.core.annotation.Path;
import com.ck.it.entity.Category;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Package: com.ck.it.controller.user
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 23:46
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
@Tag(name = "用户菜品分类")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 *  条件查询
	 *
	 * @param type  1 菜品分类 2 套餐分类
	 * @return {@link Result }<{@link List }<{@link Category }>>
	 */
	@Operation(summary = "分类列表查询")
	@GetMapping("/list")
	@CrossOrigin
	public Result<List<Category>> queryPage(@RequestParam(value = "type",required = false) Integer type){
		log.info("查询分类列表");
		List<Category> categories = categoryService.queryType(type);

		return Result.success(categories);
	}
}















