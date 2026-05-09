package com.ck.it.service.impl;

import com.ck.it.dto.BusinessDataItemDTO;
import com.ck.it.dto.GoodsSalesDTO;
import com.ck.it.dto.OrderStatisticsItemDTO;
import com.ck.it.mapper.OrderMapper;
import com.ck.it.mapper.UserMapper;
import com.ck.it.mapper.WorkspaceMapper;
import com.ck.it.service.ReportService;
import com.ck.it.service.WorkspaceService;
import com.ck.it.vo.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
	private final WorkspaceService workspaceService;
	private final WorkspaceMapper workspaceMapper;

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

			date = date.plusDays(1);
		}
		// 求和
		totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
		validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

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

	/**
	 * 查询销量排名top10的菜品/套餐
	 *
	 * @param begin
	 * @param end
	 * @return {@link SalesTop10ReportVO }
	 */
	@Override
	public SalesTop10ReportVO top(LocalDate begin, LocalDate end) {
		LocalDateTime beginTime = begin.atStartOfDay();
		LocalDateTime endTime = end.plusDays(1).atStartOfDay();
		List<GoodsSalesDTO> topList = orderMapper.top(beginTime, endTime);

		List<String> names = topList.stream().map(GoodsSalesDTO::getName).toList();
		List<Integer> numbers = topList.stream().map(GoodsSalesDTO::getNumber).toList();
		return SalesTop10ReportVO.builder()
				.nameList(joinList(names))
				.numberList(joinList(numbers)).build();
	}

	/**
	 * 导出excel报表
	 *
	 * @return {@link Boolean }
	 */
	@Override
	public Boolean export(HttpServletResponse response) {
		/// 查询数据库，获取营业数据
		LocalDate now = LocalDate.now();
		LocalDate begin = now.plusDays(-30);

		LocalDateTime beginTime = begin.atStartOfDay();
		LocalDateTime endTime = now.atStartOfDay();

		BusinessDataVO businessDataVO = workspaceService.businessData(beginTime, endTime);
		if (businessDataVO == null) {
			return false;
		}

		/// 通过POI件给数据写到Excel文件中
//		File file = new File("/Volumes/study/02-java/Project/sky-take-out/sky-server/src/main/resources/templates/运营数据报表模板.xlsx");
		try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("templates/运营数据报表模板.xlsx")) {
			if(in==null){
				throw new RuntimeException("运营数据报表模板不存在");
			}
			///    基于模板文件创建一个新的excel文件
			try (XSSFWorkbook excel = new XSSFWorkbook(in)) {
				XSSFSheet sheet = excel.getSheet("Sheet1");
				/// 概览数据
				XSSFRow row = sheet.getRow(1);
				row.getCell(1).setCellValue("时间：" + beginTime + "至" + endTime);

				row = sheet.getRow(3);
				row.getCell(2).setCellValue(businessDataVO.getTurnover());
				row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
				row.getCell(6).setCellValue(businessDataVO.getNewUsers());

				row = sheet.getRow(4);
				row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
				row.getCell(4).setCellValue(businessDataVO.getUnitPrice());
				/// 明细数据
				List<BusinessDataItemDTO> items = workspaceMapper.businessDataDetailReport(beginTime, endTime);
				Map<LocalDate, BusinessDataItemDTO> itemMap = items.stream()
						.collect(Collectors.toMap(BusinessDataItemDTO::getDate, item -> item));

				/// 循环30天补零
				int detailStartRow = 7;
				for (int i = 0; i < 30; i++) {
					LocalDate date = begin.plusDays(i);
					BusinessDataItemDTO item = itemMap.get(date);

					BigDecimal turnover = item == null || item.getTurnover() == null
							? BigDecimal.ZERO
							: item.getTurnover();
					int orderCount = item == null || item.getOrderCount() == null
							? 0
							: item.getOrderCount();
					int validOrderCount = item == null || item.getValidOrderCount() == null
							? 0
							: item.getValidOrderCount();
					int newUsers = item == null || item.getNewUsers() == null
							? 0
							: item.getNewUsers();
					double orderCompletionRate = orderCount == 0
							? 0.0
							: validOrderCount * 1.0 / orderCount;
					double unitPrice = validOrderCount == 0
							? 0.0
							: turnover.divide(BigDecimal.valueOf(validOrderCount), 2, RoundingMode.HALF_UP).doubleValue();

					row = sheet.getRow(detailStartRow + i);
					if (row == null) {
						row = sheet.createRow(detailStartRow + i);
					}

					row.getCell(1).setCellValue(date.toString());
					row.getCell(2).setCellValue(turnover.doubleValue());
					row.getCell(3).setCellValue(validOrderCount);
					row.getCell(4).setCellValue(orderCompletionRate);
					row.getCell(5).setCellValue(unitPrice);
					row.getCell(6).setCellValue(newUsers);

				}

				/// 通过输出流将Excel下载到客户端
				ServletOutputStream outputStream = response.getOutputStream();
				excel.write(outputStream);
				outputStream.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	private <T> String joinList(List<T> list) {
		StringJoiner joiner = new StringJoiner(",");
		for (T item : list) {
			joiner.add(item == null ? "0" : item.toString());
		}
		return joiner.toString();
	}

}