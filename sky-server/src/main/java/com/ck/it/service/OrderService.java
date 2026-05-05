package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.OrdersPageQueryDTO;
import com.ck.it.dto.OrdersPaymentDTO;
import com.ck.it.dto.OrdersRejectionDTO;
import com.ck.it.dto.OrdersSubmitDTO;
import com.ck.it.entity.Orders;
import com.ck.it.result.PageResult;
import com.ck.it.vo.OrderPaymentVO;
import com.ck.it.vo.OrderSubmitVO;
import com.ck.it.vo.OrderVO;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 22:26
 */
public interface OrderService extends IService<Orders> {
	OrderSubmitVO submit(OrdersSubmitDTO dto);

	OrderPaymentVO payment(OrdersPaymentDTO dto);

	/**
	 *  查询历史订单
	 *
	 * @param page
	 * @param pageSize
	 * @param status
	 * @return {@link PageResult }
	 */
	PageResult historyOrders(Integer page, Integer pageSize, Integer status);

	OrderVO orderDetail(Long id);

	/**
	 *  用户取消订单
	 *
	 * @param id
	 * @return boolean
	 */
	boolean cancelOrder(Long id);

	/**
	 *  用户再来一单
	 *
	 * @param id
	 * @return boolean
	 */
	boolean repetition(Long id);

	/**
	 *  管理端查询订单详情
	 *
	 * @param id
	 * @return {@link OrderVO }
	 */
	OrderVO queryDetail(Long id);

	/**
	 *  管理端订单搜索
	 *
	 * @param dto
	 * @return {@link PageResult }
	 */
	PageResult conditionSearch(OrdersPageQueryDTO dto);

	/**
	 * 管理端拒单
	 *
	 * @param dto
	 * @return boolean
	 */
	boolean rejection(OrdersRejectionDTO dto);
}
