package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.dto.BusinessDataItemDTO;
import com.ck.it.entity.Orders;
import com.ck.it.vo.DishOverViewVO;
import com.ck.it.vo.OrderOverViewVO;
import com.ck.it.vo.SetmealOverViewVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Package: com.ck.it.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/8 20:19
 */
@Mapper
public interface WorkspaceMapper extends BaseMapper<Orders> {

	/**
	 *  查询订单管理数据
	 *
	 * @param begin
	 * @param end
	 * @return {@link OrderOverViewVO }
	 */
	OrderOverViewVO overviewOrders(LocalDateTime begin, LocalDateTime end);

	/**
	 * 查询菜品总览
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

	/**
	 *  导出数据报表的详情部分
	 *
	 * @param begin
	 * @param end
	 * @return {@link List }<{@link BusinessDataItemDTO }>
	 */
	List<BusinessDataItemDTO> businessDataDetailReport(LocalDateTime begin,LocalDateTime end);
}
