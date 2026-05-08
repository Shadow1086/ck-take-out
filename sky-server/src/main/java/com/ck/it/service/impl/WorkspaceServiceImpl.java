package com.ck.it.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.dto.OrderStatisticsItemDTO;
import com.ck.it.entity.Orders;
import com.ck.it.mapper.OrderMapper;
import com.ck.it.mapper.UserMapper;
import com.ck.it.mapper.WorkspaceMapper;
import com.ck.it.service.WorkspaceService;
import com.ck.it.vo.BusinessDataVO;
import com.ck.it.vo.DishOverViewVO;
import com.ck.it.vo.OrderOverViewVO;
import com.ck.it.vo.SetmealOverViewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/8 20:21
 */
@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl extends ServiceImpl<WorkspaceMapper, Orders> implements WorkspaceService {
	private static final LocalDateTime BEGIN = LocalDate.now().atStartOfDay();
	private static final LocalDateTime END = LocalDate.now().plusDays(1).atStartOfDay();
	private final OrderMapper orderMapper;
	private final UserMapper userMapper;
	private final WorkspaceMapper workspaceMapper;

	/**
	 * 查询今日运营数据
	 *
	 * @return {@link BusinessDataVO }
	 */
	@Override
	public BusinessDataVO businessData() {
		/// 新增用户数
		Integer newUsers = userMapper.newUserCount(BEGIN, END);
		/// 有效订单数
		List<OrderStatisticsItemDTO> orderStatisticsItemDTOS = orderMapper.orderReport(BEGIN, END);
		OrderStatisticsItemDTO dto = new OrderStatisticsItemDTO();
		if (orderStatisticsItemDTOS != null && orderStatisticsItemDTOS.size() == 1) {
			dto = orderStatisticsItemDTOS.getFirst();
		}
		int orderCount = dto.getOrderCount() == null ? 0 : dto.getOrderCount();
		int validOrderCount = dto.getValidOrderCount() == null ? 0 : dto.getValidOrderCount();
		/// 营业额
		BigDecimal turnover = orderMapper.countAmount(BEGIN, END);
		/// 订单完成率
		double orderCompletionRate = orderCount == 0 ? 0.0 : validOrderCount * 1.0 / orderCount;

		/// 平均客单价：营业额/有效订单数
		double unitPrice = validOrderCount == 0
				? 0
				: turnover.divide(BigDecimal.valueOf(validOrderCount), 2, RoundingMode.HALF_UP)
				  .doubleValue();

		return BusinessDataVO.builder()
				.newUsers(newUsers)
				.orderCompletionRate(orderCompletionRate)
				.turnover(turnover.doubleValue())
				.unitPrice(unitPrice)
				.validOrderCount(validOrderCount)
				.build();
	}

	/**
	 * 查询订单管理数据
	 *
	 * @return {@link OrderOverViewVO }
	 */
	@Override
	public OrderOverViewVO overviewOrders() {
		return workspaceMapper.overviewOrders(BEGIN, END);
	}

	/**
	 * 查询菜品总览
	 *
	 * @return {@link DishOverViewVO }
	 */
	@Override
	public DishOverViewVO overviewDishes() {
		return workspaceMapper.overviewDishes();
	}

	/**
	 * 查询套餐总览
	 *
	 * @return {@link SetmealOverViewVO }
	 */
	@Override
	public SetmealOverViewVO overviewSetmeals() {
		return workspaceMapper.overviewSetmeals();
	}
}
