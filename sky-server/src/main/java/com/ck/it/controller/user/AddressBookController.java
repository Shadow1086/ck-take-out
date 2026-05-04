package com.ck.it.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ck.it.context.BaseContext;
import com.ck.it.entity.AddressBook;
import com.ck.it.result.Result;
import com.ck.it.service.AddressBookService;
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
 * {@code @Create} 2026-2026/5/4 19:38
 */
@RestController
@Slf4j
@RequestMapping("/user/addressBook")
@Tag(name = "地址相关接口")
public class AddressBookController {
	@Autowired
	private AddressBookService addressBookService;

	/**
	 * 新增地址
	 *
	 * @param addressBook
	 * @return {@link Result }<{@link Boolean }>    成功返回true,反之返回false
	 */
	@PostMapping
	@Operation(summary = "新增地址")
	public Result<Boolean> add(@RequestBody AddressBook addressBook) {
		log.info("新增地址：{}", addressBook);
		addressBook.setUserId(BaseContext.getCurrentId());
		boolean save = addressBookService.save(addressBook);
		return Result.success(save);
	}

	/**
	 *  查询所有地址
	 *
	 * @return {@link Result }<{@link List }<{@link AddressBook }>>
	 */
	@GetMapping("list")
	@Operation(summary = "查询所有地址")
	public Result<List<AddressBook>> list() {
		Long userId = BaseContext.getCurrentId();
		log.info("查询所有地址,用户id:{}", userId);
		List<AddressBook> list = addressBookService.list(new LambdaQueryWrapper<AddressBook>()
				.eq(AddressBook::getUserId, userId));
		return Result.success(list);
	}

}
