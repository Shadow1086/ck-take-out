package com.ck.it.controller.admin;

import com.ck.it.dto.OrdersCancelDTO;
import com.ck.it.dto.OrdersConfirmDTO;
import com.ck.it.dto.OrdersPageQueryDTO;
import com.ck.it.dto.OrdersRejectionDTO;
import com.ck.it.entity.Orders;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.OrderService;
import com.ck.it.vo.OrderStatisticsVO;
import com.ck.it.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
		log.info("查询订单详情，订单id：{}", id);
		OrderVO orderVO = orderService.queryDetail(id);

		return Result.success(orderVO);
	}


	/**
	 * 根据条件查询订单
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

	/**
	 * 商家拒单
	 *
	 * @param dto
	 * @return {@link Result }<{@link Boolean }>
	 */
	@Operation(summary = "拒单")
	@PutMapping("/rejection")
	public Result<Boolean> rejection(@RequestBody OrdersRejectionDTO dto) {
		log.info("商家拒单:{}", dto);
		boolean rejection = orderService.rejection(dto);

		return Result.success(rejection);
	}

	/**
	 * 各个状态的订单数量统计
	 *
	 * @return {@link Result }<{@link OrderStatisticsVO }>
	 */
	@Operation(summary = "各个状态的订单数量统计")
	@GetMapping("/statistics")
	public Result<OrderStatisticsVO> statistics() {
		OrderStatisticsVO statistics = orderService.statistics();

		return Result.success(statistics);
	}

	/**
	 * 接单
	 *
	 * @param dto
	 * @return {@link Result }
	 */
	@PutMapping("/confirm")
	@Operation(summary = "接单")
	public Result<Boolean> confirm(@RequestBody OrdersConfirmDTO dto) {
		log.info("商家开始接单:{}", dto);
		boolean confirm = orderService.changeOrderStatus(dto.getId(), Orders.CONFIRMED);

		return Result.success(confirm);
	}

	/**
	 * 商家取消订单
	 *
	 * @param dto
	 * @return {@link Result }<{@link Boolean }>
	 */
	@PutMapping("/cancel")
	@Operation(summary = "取消订单")
	public Result<Boolean> cancelOrder(@RequestBody OrdersCancelDTO dto) {
		log.info("商家取消了订单：{}", dto);
		boolean cancel = orderService.changeOrderStatus(dto.getId(), Orders.CANCELLED);
		return Result.success(cancel);
	}

	@PutMapping("/delivery/{id}")
	@Operation(summary = "派送订单")
	public Result<Boolean> delivery(@PathVariable("id") Long id) {
		log.info("商家派送订单，id：{}", id);
		boolean cancel = orderService.changeOrderStatus(id, Orders.DELIVERY_IN_PROGRESS);
		return Result.success(cancel);
	}

	@PutMapping("/complete/{id}")
	@Operation(summary = "完成订单")
	public Result<Boolean> complete(@PathVariable("id") Long id) {
		log.info("商家派送订单，id：{}", id);
		boolean completed = orderService.changeOrderStatus(id, Orders.COMPLETED);
		return Result.success(completed);
	}

}
