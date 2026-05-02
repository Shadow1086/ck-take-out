package com.ck.it.aspect;

import com.ck.it.annotation.AutoFill;
import com.ck.it.constant.AutoFillConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Package: com.ck.it.aspect
 * Description:
 * <p>
 * 自定义切面，实现公共字段自动填充处理逻辑
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 19:21
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
	/// 指定切入点：拦截所有mapper下的有@AutoFill注解的方法
	@Pointcut("execution(* com.ck.it.mapper.*.*(..)) && @annotation(com.ck.it.annotation.AutoFill)")
	public void autoFillPointCut() {

	}

	/**
	 * 前置通知，在通知中进行公共字段的赋值
	 */
	@Before("autoFillPointCut()")
	public void autoFill(JoinPoint joinPoint) {
		log.info("开始进行公共字段的自动填充");

		/// 获取到当前被拦截的方法上的数据库操作类型
		MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 方法签名对象
		AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);// 获取方法上的注解对象
		OperationType operationType = autoFill.value();     // 获取操作类型，也就是OperationType

		/// 获取到当前被拦截的方法的参数 ==== 实体对象
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length == 0) {
			return;
		}
		Object entity = args[0];
		/// 准备赋值的数据
		LocalDateTime now = LocalDateTime.now();
		Long currentid = BaseContext.getCurrentId();

		/// 根据当前不同的操作类型，为当前的属性赋值，通过 反射
		if (operationType == OperationType.INSERT) {
			try {
				/// 使用常量代替手写方法名
				Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
				Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
				Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
				Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

				// 通过反射为对象属性赋值
				setCreateTime.invoke(entity, now);
				setUpdateTime.invoke(entity, now);
				setCreateUser.invoke(entity, currentid);
				setUpdateUser.invoke(entity, currentid);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else if (operationType == OperationType.UPDATE) {
			try {
				Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
				Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

				setUpdateTime.invoke(entity, now);
				setUpdateUser.invoke(entity, currentid);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
