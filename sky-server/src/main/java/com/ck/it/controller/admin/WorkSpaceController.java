package com.ck.it.controller.admin;

import com.ck.it.result.Result;
import com.ck.it.service.WorkspaceService;
import com.ck.it.vo.BusinessDataVO;
import com.ck.it.vo.OrderOverViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.ck.it.controller.admin
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/8 20:16
 */
@RestController
@Tag(name = "工作台相关接口")
@RequestMapping("/admin/workspace")
@RequiredArgsConstructor
@Slf4j
public class WorkSpaceController {
	private final WorkspaceService workspaceService;

	/**
	 * 查询今日运营数据
	 *
	 * @return {@link Result }<{@link BusinessDataVO }>
	 */
	@GetMapping("/businessData")
	@Operation(summary = "查询今日运营数据")
	public Result<BusinessDataVO> businessData() {
		log.info("查询今日运营数据");
		return Result.success(workspaceService.businessData());
	}

	/**
	 *  查询订单管理数据
	 *
	 * @return {@link Result }<{@link OrderOverViewVO }>
	 */
	@GetMapping("/overviewOrders")
	@Operation(summary = "查询订单管理数据")
	public Result<OrderOverViewVO> overviewOrders(){
		log.info("查询订单管理数据");
		return Result.success(workspaceService.overviewOrders());
	}

}
