package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.entity.OrderDetail;
import com.ck.it.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

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
	Long countAmount(LocalDateTime beginTime, LocalDateTime endTime);
}
