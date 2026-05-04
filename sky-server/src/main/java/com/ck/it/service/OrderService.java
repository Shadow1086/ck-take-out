package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.OrdersPaymentDTO;
import com.ck.it.dto.OrdersSubmitDTO;
import com.ck.it.entity.Orders;
import com.ck.it.result.PageResult;
import com.ck.it.vo.OrderPaymentVO;
import com.ck.it.vo.OrderSubmitVO;

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
}
