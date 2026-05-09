package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.entity.Orders;
import com.ck.it.vo.BusinessDataVO;
import com.ck.it.vo.DishOverViewVO;
import com.ck.it.vo.OrderOverViewVO;
import com.ck.it.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/8 20:21
 */
public interface WorkspaceService extends IService<Orders> {
	/**
	 *  查询今日运营数据
	 *
	 * @return {@link BusinessDataVO }
	 */
	BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);

	/**
	 *  查询订单管理数据
	 *
	 * @return {@link OrderOverViewVO }
	 */
	OrderOverViewVO overviewOrders();

	/**
	 *  查询菜品总览
	 *
	 * @return {@link DishOverViewVO }
	 */
	DishOverViewVO overviewDishes();

	/**
	 *  查询套餐总览
	 *
	 * @return {@link SetmealOverViewVO }
	 */
	SetmealOverViewVO overviewSetmeals();
}
