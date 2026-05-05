package com.ck.it.controller.user;

import com.ck.it.dto.OrdersPaymentDTO;
import com.ck.it.dto.OrdersSubmitDTO;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.OrderService;
import com.ck.it.vo.OrderPaymentVO;
import com.ck.it.vo.OrderSubmitVO;
import com.ck.it.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Package: com.ck.it.controller.user
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 22:14
 */
@RestController("userOrderController")
@Slf4j
@Tag(name = "用户端订单相关接口")
@RequestMapping("/user/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	/**
	 * 用户下单接口
	 *
	 * @param dto
	 * @return {@link Result }<{@link OrderSubmitVO }>
	 */
	@PostMapping("/submit")
	@Operation(summary = "用户下单接口")
	public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO dto) {
		log.info("用户下单：{}", dto);
		OrderSubmitVO submit = orderService.submit(dto);

		return Result.success(submit);
	}


	@PutMapping("/payment")
	@Operation(summary = "订单支付")
	public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO dto) {
		log.info("订单支付：{}", dto);
		OrderPaymentVO orderPaymentVO = orderService.payment(dto);
		return Result.success(orderPaymentVO);
	}

	@GetMapping("/historyOrders")
	@Operation(summary = "历史订单查询")
	public Result<PageResult> historyOrders(@RequestParam("page") Integer page,
	                                        @RequestParam("pageSize") Integer pageSize,
	                                        @RequestParam(value = "status", required = false) Integer status) {
		log.info("历史订单查询");
		PageResult pageResult = orderService.historyOrders(page, pageSize, status);

		return Result.success(pageResult);
	}

	/**
	 * 查询订单详情
	 *
	 * @param id
	 * @return {@link Result }<{@link OrderVO }>
	 */
	@GetMapping("orderDetail/{id}")
	@Operation(summary = "查询订单详情")
	public Result<OrderVO> orderDetail(@PathVariable("id") Long id) {
		log.info("订单详情查询，id:{}", id);
		OrderVO orderVO = orderService.orderDetail(id);

		return Result.success(orderVO);
	}

	/**
	 *  用户取消订单
	 *
	 * @param id
	 * @return {@link Result }<{@link Boolean }>
	 */
	@PutMapping("/cancel/{id}")
	@Operation(summary = "取消订单")
	public Result<Boolean> cancelOrder(@PathVariable("id") Long id) {
		log.info("用户取消订单，订单id:{}", id);
		boolean b = orderService.cancelOrder(id);
		return Result.success(b);
	}
}
