package com.ck.it.service.impl;

import com.ck.it.dto.OrderStatisticsItemDTO;
import com.ck.it.mapper.OrderMapper;
import com.ck.it.mapper.UserMapper;
import com.ck.it.service.ReportService;
import com.ck.it.vo.OrderReportVO;
import com.ck.it.vo.TurnoverReportVO;
import com.ck.it.vo.UserReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/7 22:00
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final OrderMapper orderMapper;
	private final UserMapper userMapper;

	/**
	 * 营业额统计
	 *
	 * @param begin
	 * @param end
	 * @return {@link TurnoverReportVO }
	 */
	@Override
	public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
		/// 用于存放begin->end范围内的日期
		List<LocalDate> dateList = new ArrayList<>();
		/// 用于存放营业额
		List<BigDecimal> turnoverList = new ArrayList<>();

		LocalDate date = begin;
		while (!date.isAfter(end)) {
			dateList.add(date);
			/// 查询数据库
			LocalDateTime beginTime = date.atStartOfDay();
			LocalDateTime endTime = date.plusDays(1).atStartOfDay();
			BigDecimal l = orderMapper.countAmount(beginTime, endTime);
			turnoverList.add(l);
			date = date.plusDays(1);
		}
		TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
		/// 将日期存为字符串
		turnoverReportVO.setDateList(joinList(dateList));
		/// 将营业额存为字符串
		turnoverReportVO.setTurnoverList(joinList(turnoverList));

		return turnoverReportVO;
	}

	/**
	 * 用户统计
	 *
	 * @param begin
	 * @param end
	 * @return {@link UserReportVO }
	 */
	@Override
	public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
		UserReportVO userReportVO = new UserReportVO();

		/// 拆分日期
		List<LocalDate> dateList = new ArrayList<>();
		List<Integer> newUserList = new ArrayList<>();
		List<Integer> totalUserList = new ArrayList<>();

		LocalDate date = begin;
		while (!date.isAfter(end)) {
			LocalDateTime beginTime = date.atStartOfDay();
			LocalDateTime endTime = date.plusDays(1).atStartOfDay();
			//// 查询当日新增用户
			Integer newUserCount = userMapper.newUserCount(beginTime, endTime);
			newUserList.add(newUserCount == null ? 0 : newUserCount);
			/// 查询总用户
			Integer totalUserCount = userMapper.totalUserCount(endTime);
			totalUserList.add(totalUserCount == null ? 0 : totalUserCount);

			dateList.add(date);
			date = date.plusDays(1);
		}
		userReportVO.setDateList(joinList(dateList));
		userReportVO.setNewUserList(joinList(newUserList));
		userReportVO.setTotalUserList(joinList(totalUserList));

		return userReportVO;
	}

	/**
	 * 订单统计
	 *
	 * @param begin
	 * @param end
	 * @return {@link OrderReportVO }
	 */
	// TODO : 理解下面的代码
	@Override
	public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
		OrderReportVO orderReport = new OrderReportVO();

		int totalOrderCount = 0;
		int validOrderCount = 0;
		List<LocalDate> dateList = new ArrayList<>();
		List<Integer> orderCountList = new ArrayList<>();
		List<Integer> validOrderCountList = new ArrayList<>();

		LocalDateTime beginTime = begin.atStartOfDay();
		LocalDateTime endTime = end.plusDays(1).atStartOfDay();


		List<OrderStatisticsItemDTO> items = orderMapper.orderReport(beginTime, endTime);
		Map<LocalDate, OrderStatisticsItemDTO> itemMap = items.stream().collect(Collectors.toMap(OrderStatisticsItemDTO::getOrderDate, item -> item));

		LocalDate date = begin;
		while (!date.isAfter(end)) {
			dateList.add(date);
			OrderStatisticsItemDTO item = itemMap.get(date);
			int orderCount = item == null ? 0 : item.getOrderCount();
			int validCount = item == null ? 0 : item.getValidOrderCount();
			orderCountList.add(orderCount);
			validOrderCountList.add(validCount);

			totalOrderCount += orderCount;
			validOrderCount += validCount;

			date = date.plusDays(1);
		}

		double orderCompletionRate = totalOrderCount == 0 ?
				0.0 : validOrderCount * 1.0 / totalOrderCount;

		orderReport.setDateList(joinList(dateList));
		orderReport.setOrderCompletionRate(orderCompletionRate);
		orderReport.setOrderCountList(joinList(orderCountList));
		orderReport.setTotalOrderCount(totalOrderCount);
		orderReport.setValidOrderCount(validOrderCount);
		orderReport.setValidOrderCountList(joinList(validOrderCountList));

		return orderReport;
	}

	private <T> String joinList(List<T> list) {
		StringJoiner joiner = new StringJoiner(",");
		for (T item : list) {
			joiner.add(item == null ? "0" : item.toString());
		}
		return joiner.toString();
	}

}














