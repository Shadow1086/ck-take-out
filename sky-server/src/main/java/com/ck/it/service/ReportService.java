package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.vo.OrderReportVO;
import com.ck.it.vo.SalesTop10ReportVO;
import com.ck.it.vo.TurnoverReportVO;
import com.ck.it.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/7 21:53
 */
public interface ReportService{

	/**
	 *  营业额统计
	 *
	 * @param begin
	 * @param end
	 * @return {@link TurnoverReportVO }
	 */
	TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

	/**
	 *  用户统计
	 *
	 * @param begin
	 * @param end
	 * @return {@link UserReportVO }
	 */
	UserReportVO userStatistics(LocalDate begin, LocalDate end);

	/**
	 *  订单统计
	 *
	 * @param begin
	 * @param end
	 * @return {@link OrderReportVO }
	 */
	OrderReportVO orderStatistics(LocalDate begin, LocalDate end);

	/**
	 *  查询销量排名top10的菜品/套餐
	 *
	 * @param begin
	 * @param end
	 * @return {@link SalesTop10ReportVO }
	 */
	SalesTop10ReportVO top(LocalDate begin, LocalDate end);

	/**
	 *  导出excel报表
	 *
	 * @return {@link Boolean }
	 */
	Boolean export(HttpServletResponse response);
}
