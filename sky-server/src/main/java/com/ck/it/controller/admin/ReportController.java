package com.ck.it.controller.admin;

import com.ck.it.result.Result;
import com.ck.it.service.ReportService;
import com.ck.it.vo.OrderReportVO;
import com.ck.it.vo.SalesTop10ReportVO;
import com.ck.it.vo.TurnoverReportVO;
import com.ck.it.vo.UserReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/7 21:43
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Tag(name = "统计相关接口")
public class ReportController {
	@Autowired
	private ReportService reportService;


	@GetMapping("/turnoverStatistics")
	@Operation(summary = "营业额统计接口")
	public Result<TurnoverReportVO> turnoverStatistics(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("营业额统计时间：{}->{}", begin, end);
		return Result.success(reportService.turnoverStatistics(begin, end));
	}

	/**
	 * 统计每日新增用户量和当日总用户量
	 *
	 * @param begin
	 * @param end
	 * @return {@link Result }<{@link UserReportVO }>
	 */
	@GetMapping("/userStatistics")
	@Operation(summary = "用户统计接口")
	public Result<UserReportVO> userStatistics(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
	) {
		log.info("用户统计：{}->{}", begin, end);
		return Result.success(reportService.userStatistics(begin, end));
	}

	/**
	 * 订单统计接口
	 *
	 * @param begin
	 * @param end
	 * @return {@link Result }<{@link OrderReportVO }>
	 */
	@GetMapping("/ordersStatistics")
	@Operation(summary = "订单统计接口")
	public Result<OrderReportVO> orderStatistics(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("订单统计查询：{}->{}", begin, end);
		return Result.success(reportService.orderStatistics(begin, end));
	}

	@GetMapping("/top10")
	@Operation(summary = "查询销量排名top10的接口")
	public Result<SalesTop10ReportVO> top(
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("查询销量排名top10的菜品：{}->{}",begin,end);
		return Result.success(reportService.top(begin,end));
	}
}
