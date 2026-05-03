package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.UserLoginDTO;
import com.ck.it.entity.User;

/**
 * Package: com.ck.it.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 22:42
 */

public interface UserService extends IService<User> {

	User wxLogin(UserLoginDTO dto);
}
