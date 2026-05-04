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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
		addressBook.setIsDefault(0);
		boolean save = addressBookService.save(addressBook);
		return Result.success(save);
	}

	/**
	 * 查询所有地址
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

	/**
	 * 根据id获取地址
	 *
	 * @param id
	 * @return {@link Result }<{@link AddressBook }>
	 */
	@GetMapping("/{id}")
	@Operation(summary = "根据id获取地址")
	public Result<AddressBook> queryById(@PathVariable("id") Long id) {
		log.info("根据id获取地址，id为：{}", id);
		AddressBook result = addressBookService.getById(id);
		if (result!=null && result.getUserId().equals(BaseContext.getCurrentId())) {
			return Result.success(result);
		}
		return Result.error(null);
	}

	/**
	 * 根据id删除地址
	 *
	 * @param id
	 * @return {@link Result }<{@link Boolean }>
	 */
	@DeleteMapping
	@Operation(summary = "根据id删除地址")
	public Result<Object> deleteById(@RequestParam("id") Long id) {
		log.info("根据id删除地址，id:{}", id);
		AddressBook addressBook = addressBookService.getById(id);
		if (addressBook != null && addressBook.getUserId().equals(BaseContext.getCurrentId())) {
			boolean b = addressBookService.removeById(id);
			return Result.success(b);
		}
		return Result.error("无权限");
	}

	/**
	 * 根据id修改地址
	 *
	 * @param newAddressBook
	 * @return {@link Result }<{@link Boolean }>
	 */
	@PutMapping
	@Operation(summary = "根据id修改地址")
	public Result<Boolean> update(@RequestBody AddressBook newAddressBook) {
		log.info("修改地址，新地址：{}", newAddressBook);
		AddressBook addressBook = addressBookService.getById(newAddressBook.getId());
		if (addressBook != null) {
			if (BaseContext.getCurrentId().equals(addressBook.getUserId())) {
				if(addressBook.getIsDefault()==1){
					/// 如果是默认地址，就设置为默认地址
					newAddressBook.setIsDefault(1);
				}
				boolean b = addressBookService.updateById(newAddressBook);
				return Result.success(b);
			}
		}
		return Result.success(false);
	}

	/**
	 * 设置默认地址
	 *
	 * @param id
	 * @return {@link Result }<{@link Boolean }>
	 */
	@PutMapping("/default")
	@Operation(summary = "设置默认地址")
	@Transactional
	public Result<Boolean> setDefault(Long id) {
		log.info("设置默认地址，id：{}", id);
		AddressBook addressBook = addressBookService.getById(id);
		if (addressBook != null) {
			if (addressBook.getIsDefault() == 1) {
				/// 如果已经是默认地址了
				return Result.success(true);
			}
			AddressBook oldDefault = addressBookService.getOne(new LambdaQueryWrapper<AddressBook>()
					.eq(AddressBook::getIsDefault, 1)
					.eq(AddressBook::getUserId, BaseContext.getCurrentId()));
			if (oldDefault != null) {
				/// 如果已经设置过默认地址
				oldDefault.setIsDefault(0);
				addressBook.setIsDefault(1);
				ArrayList<AddressBook> addressBooks = new ArrayList<>();
				addressBooks.add(oldDefault);
				addressBooks.add(addressBook);
				boolean b = addressBookService.updateBatchById(addressBooks);
				return Result.success(b);
			}
			/// 首次设置默认地址
			addressBook.setIsDefault(1);
			boolean b = addressBookService.updateById(addressBook);
			return Result.success(b);
		}
		return Result.success(false);
	}

	/**
	 * 查询默认地址
	 *
	 * @return {@link Result }<{@link AddressBook }>
	 */
	@GetMapping("/default")
	@Operation(summary = "查询默认地址")
	public Result<AddressBook> getDefault() {
		Long id = BaseContext.getCurrentId();
		log.info("查询默认地址，当前用户id:{}", id);
		AddressBook one = addressBookService.getOne(new LambdaQueryWrapper<AddressBook>()
				.eq(AddressBook::getUserId, id)
				.eq(AddressBook::getIsDefault,1));
		if(one!=null){
			return Result.success(one);
		}else{
			return Result.success();
		}

	}
}
