package com.ck.it.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ck.it.constant.MetaObjectHandlerConstant;
import com.ck.it.context.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Package: com.ck.it.handler
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 20:27
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
	/**
	 * 插入元对象字段填充（用于插入时对公共字段的填充）
	 *
	 * @param metaObject 元对象
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, MetaObjectHandlerConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, MetaObjectHandlerConstant.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictInsertFill(metaObject, MetaObjectHandlerConstant.CREATE_USER, Long.class, BaseContext.getCurrentId());
		this.strictInsertFill(metaObject, MetaObjectHandlerConstant.UPDATE_USER, Long.class, BaseContext.getCurrentId());
	}

	/**
	 * 更新元对象字段填充（用于更新时对公共字段的填充）
	 *
	 * @param metaObject 元对象
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictUpdateFill(metaObject, MetaObjectHandlerConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
		this.strictUpdateFill(metaObject, MetaObjectHandlerConstant.UPDATE_USER, Long.class, BaseContext.getCurrentId());
	}
}
