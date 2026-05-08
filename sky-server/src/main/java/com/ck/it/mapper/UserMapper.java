package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.entity.User;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 22:43
 */

public interface UserMapper extends BaseMapper<User> {

	/**
	 *  统计新增用户
	 *
	 * @param beginTime
	 * @param endTime
	 * @return {@link Integer }
	 */
	Integer newUserCount(LocalDateTime beginTime, LocalDateTime endTime);

	/**
	 *  当日总用户统计量
	 *
	 * @param endTime
	 * @return {@link Integer }
	 */
	Integer totalUserCount(LocalDateTime endTime);
}
