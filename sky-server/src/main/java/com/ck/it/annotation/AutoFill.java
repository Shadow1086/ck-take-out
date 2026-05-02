package com.ck.it.annotation;

import com.ck.it.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Package: com.ck.it.annotation
 * Description:
 * <p>
 *      自定义注解，用于表示某个方法需要进行功能字段自动填充处理
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 19:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
	/// 数据库操作类型： UPDATE，INSERT
	OperationType value();
}
