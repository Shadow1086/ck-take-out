package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.entity.Orders;
import com.ck.it.vo.BusinessDataVO;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/8 20:21
 */
public interface WorkspaceService extends IService<Orders> {
	/**
	 *  查询今日运营数据
	 *
	 * @return {@link BusinessDataVO }
	 */
	BusinessDataVO businessData();
}
