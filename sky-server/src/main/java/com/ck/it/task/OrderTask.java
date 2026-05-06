package com.ck.it.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ck.it.entity.Orders;
import com.ck.it.mapper.OrderMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Package: com.ck.it.task
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/6 20:39
 */
@Component
@Slf4j
@Tag(name = "定时任务")
public class OrderTask {
	@Autowired
	private OrderMapper orderMapper;

	/**
	 * 处理订单超时
	 *
	 */
	@Scheduled(cron = "0 * * * * ?")
	public void processTimeoutOrder() {
		log.info("定时处理超时订单：{}", LocalDateTime.now());
		LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

		List<Orders> orders = orderMapper.selectList(new LambdaQueryWrapper<Orders>()
				.eq(Orders::getStatus, Orders.UN_PAID)
				.gt(Orders::getOrderTime, time));
		if (orders != null && !orders.isEmpty()) {
			for (Orders order : orders) {
				order.setStatus(Orders.CANCELLED);
				order.setCancelReason("订单超时，自动取消");
				order.setCancelTime(LocalDateTime.now());
				orderMapper.updateById(order);
			}
		}
	}

	/**
	 * 处理一直处于派送中的订单
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void processDeliveryOrder(){
		log.info("定时处理处于派送中的订单：{}",LocalDateTime.now());
		LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
		List<Orders> orders = orderMapper.selectList(new LambdaQueryWrapper<Orders>()
				.eq(Orders::getStatus, Orders.DELIVERY_IN_PROGRESS)
				.gt(Orders::getOrderTime, time));
		if (orders != null && !orders.isEmpty()) {
			for (Orders order : orders) {
				order.setStatus(Orders.COMPLETED);
				order.setCancelTime(LocalDateTime.now());
				orderMapper.updateById(order);
			}
		}
	}
}
