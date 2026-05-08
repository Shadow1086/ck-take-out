package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.dto.GoodsSalesDTO;
import com.ck.it.dto.OrderStatisticsItemDTO;
import com.ck.it.entity.OrderDetail;
import com.ck.it.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Package: com.ck.it.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 22:19
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {


	/**
	 *  营业额统计
	 *
	 * @param beginTime
	 * @param endTime
	 * @return {@link Long }
	 */
	BigDecimal countAmount(LocalDateTime beginTime, LocalDateTime endTime);

	/**
	 *  查询一天之内的订单总数和有效订单总数
	 *
	 * @param beginTime
	 * @param endTime
	 * @return {@link List }<{@link Integer }>
	 */
	List<OrderStatisticsItemDTO> orderReport(LocalDateTime beginTime, LocalDateTime endTime);

	/**
	 * 查询指定时间段内的销量前十的菜品/套餐
	 *
	 * @param beginTime
	 * @param endTime
	 * @return {@link List }<{@link GoodsSalesDTO }>
	 */
	List<GoodsSalesDTO> top(LocalDateTime beginTime, LocalDateTime endTime);
}
