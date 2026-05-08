package com.ck.it.service.impl;

import com.ck.it.mapper.OrderMapper;
import com.ck.it.mapper.UserMapper;
import com.ck.it.service.ReportService;
import com.ck.it.vo.TurnoverReportVO;
import com.ck.it.vo.UserReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
		List<Long> turnoverList = new ArrayList<>();

		LocalDate date = begin;
		while (!date.isAfter(end)) {
			dateList.add(date);
			/// 查询数据库
			LocalDateTime beginTime = date.atStartOfDay();
			LocalDateTime endTime = date.plusDays(1).atStartOfDay();
			Long l = orderMapper.countAmount(beginTime, endTime);
			turnoverList.add(l);
			date = date.plusDays(1);
		}
		TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
		/// 将日期存为字符串
		StringJoiner dateListStr = new StringJoiner(",");
		for (LocalDate localDate : dateList) {
			dateListStr.add(localDate.toString());
		}
		/// 将营业额存为字符串
		StringJoiner turnoverListStr = new StringJoiner(",");
		for (Long i : turnoverList) {
			if (i != null) {
				turnoverListStr.add(i.toString());
			} else {
				turnoverListStr.add("0");
			}
		}
		turnoverReportVO.setDateList(dateListStr.toString());
		turnoverReportVO.setTurnoverList(turnoverListStr.toString());

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
		StringJoiner listStr = new StringJoiner(",");
		for (LocalDate localDate : dateList) {
			listStr.add(localDate.toString());
		}
		userReportVO.setDateList(listStr.toString());
		listStr = new StringJoiner(",");
		for (Integer i : newUserList) {
			if (i != null) {
				listStr.add(i.toString());
			} else {
				listStr.add("0");
			}
		}
		userReportVO.setNewUserList(listStr.toString());
		listStr = new StringJoiner(",");
		for (Integer i : totalUserList) {
			if (i != null) {
				listStr.add(i.toString());
			} else {
				listStr.add("0");
			}
		}
		userReportVO.setTotalUserList(listStr.toString());

		return userReportVO;
	}
}














