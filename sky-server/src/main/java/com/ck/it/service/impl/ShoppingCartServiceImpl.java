package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.ShoppingCartDTO;
import com.ck.it.entity.Dish;
import com.ck.it.entity.Setmeal;
import com.ck.it.entity.ShoppingCart;
import com.ck.it.mapper.DishMapper;
import com.ck.it.mapper.SetmealMapper;
import com.ck.it.mapper.ShoppingCartMapper;
import com.ck.it.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 16:57
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private DishMapper dishMapper;
	@Autowired
	private SetmealMapper setmealMapper;

	@Override
	@Transactional
	public Integer removeItem(ShoppingCartDTO dto) {

		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(dto, shoppingCart);
		List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

		if (list != null && !list.isEmpty()) {
			/// 如果数据库中有该商品
			ShoppingCart shoppingCart1 = list.getFirst();
			if (shoppingCart1.getNumber() > 1) {
				/// 如果有多份
				shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
				return shoppingCartMapper.updateById(shoppingCart1);
			}
			/// 如果只有一份
			LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<ShoppingCart>()
					.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

			if (dto.getDishId() == null) {
				/// 如果是套餐
				wrapper.eq(ShoppingCart::getSetmealId, dto.getSetmealId())
						.isNull(ShoppingCart::getDishId);
			} else {
				/// 如果是单品
				wrapper.eq(ShoppingCart::getDishId, dto.getDishId())
						.isNull(ShoppingCart::getSetmealId);
			}

			if (dto.getDishFlavor() != null) {
				wrapper.eq(ShoppingCart::getDishFlavor, dto.getDishFlavor());
			} else {
				wrapper.isNull(ShoppingCart::getDishFlavor);
			}
			return shoppingCartMapper.delete(wrapper);
		}
		return null;
	}

	/**
	 * 添加购物车
	 *
	 * @param dto
	 */
	@Override
	@Transactional
	public void add(ShoppingCartDTO dto) {
		ShoppingCart shoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(dto, shoppingCart);
		/// 获取当前用户id
		Long userid = BaseContext.getCurrentId();
		shoppingCart.setUserId(userid);

		List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
		/// 判断当前加入购物车的商品是否存在
		if (list != null && !list.isEmpty()) {
			/// 如果存在，number+1
			ShoppingCart shoppingCart1 = list.get(0);
			shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
			shoppingCartMapper.updateById(shoppingCart1);
		} else {
			/// 如果不存在，插入数据库中
			Long dishId = dto.getDishId();
			if (dishId != null) {
				/// 本次添加的套餐
				Dish dish = dishMapper.selectById(dishId);
				shoppingCart.setDishId(dishId);
				shoppingCart.setImage(dish.getImage());
				shoppingCart.setName(dish.getName());
				shoppingCart.setAmount(dish.getPrice());
			} else {
				Setmeal setmeal = setmealMapper.selectById(dto.getSetmealId());
				shoppingCart.setName(setmeal.getName());
				shoppingCart.setAmount(setmeal.getPrice());
				shoppingCart.setImage(setmeal.getImage());
			}
			shoppingCart.setNumber(1);
			shoppingCartMapper.insert(shoppingCart);
		}


	}
}
