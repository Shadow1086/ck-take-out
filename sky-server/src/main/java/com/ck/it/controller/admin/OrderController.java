package com.ck.it.controller.admin;

import com.ck.it.dto.OrdersPageQueryDTO;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.OrderService;
import com.ck.it.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/5 17:55
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Tag(name = "管理端订单相关接口")
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 管理端查询订单详情
	 *
	 * @param id
	 * @return {@link Result }<{@link OrderVO }>
	 */
	@Operation(summary = "查询订单详情")
	@GetMapping("/details/{id}")
	public Result<OrderVO> queryDetail(@PathVariable("id") Long id) {
		OrderVO orderVO = orderService.queryDetail(id);

		return Result.success(orderVO);
	}


	/**
	 *  根据条件查询订单
	 *
	 * @param dto
	 * @return {@link Result }<{@link PageResult }>
	 */
	@Operation(summary = "根据条件查询")
	@GetMapping("/conditionSearch")
	public Result<PageResult> conditionSearch(OrdersPageQueryDTO dto) {
		PageResult pageResult = orderService.conditionSearch(dto);

		return Result.success(pageResult);
	}
}
