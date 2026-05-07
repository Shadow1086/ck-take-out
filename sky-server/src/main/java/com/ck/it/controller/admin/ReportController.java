package com.ck.it.controller.admin;

import com.ck.it.result.Result;
import com.ck.it.service.ReportService;
import com.ck.it.vo.TurnoverReportVO;
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
}
