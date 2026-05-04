package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.MessageConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.OrdersPaymentDTO;
import com.ck.it.dto.OrdersSubmitDTO;
import com.ck.it.entity.AddressBook;
import com.ck.it.entity.OrderDetail;
import com.ck.it.entity.Orders;
import com.ck.it.entity.ShoppingCart;
import com.ck.it.exception.AddressBookBusinessException;
import com.ck.it.exception.OrderBusinessException;
import com.ck.it.exception.ShoppingCartBusinessException;
import com.ck.it.mapper.OrderDetailMapper;
import com.ck.it.mapper.OrderMapper;
import com.ck.it.mapper.UserMapper;
import com.ck.it.properties.WeChatProperties;
import com.ck.it.service.AddressBookService;
import com.ck.it.service.OrderService;
import com.ck.it.service.ShoppingCartService;
import com.ck.it.utils.WeChatPayUtil;
import com.ck.it.vo.OrderPaymentVO;
import com.ck.it.vo.OrderSubmitVO;
import com.ck.it.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.ck.it.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/4 22:26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private AddressBookService addressBookService;
	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private WeChatProperties weChatProperties;
	@Autowired
	private WeChatPayUtil weChatPayUtil;
	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional
	@Operation(summary = "用户下单")
	public OrderSubmitVO submit(OrdersSubmitDTO dto) {
		/// 处理业务异常

		/// 地址簿为空
		AddressBook addressBook = addressBookService.getById(dto.getAddressBookId());
		if(addressBook==null){
			throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
		}
		/// 购物车数据为空
		ShoppingCart shoppingCart = new ShoppingCart();
		List<ShoppingCart> list = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
		if(list==null || list.isEmpty()){
			throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
		}
		/// 向订单表插入一条数据
		Orders orders = new Orders();
		// addressBookId,payMethod,remark,estimatedDeliveryTime
		// DeliveryStatus,TablewareNumber,tableWareStatus,PackAmount,Amount
		BeanUtils.copyProperties(dto,orders);

		// number status,userId,orderTime,patStatus,userName,Phone,Address
		// checkoutTime,consignee,cancelReason,rejectionReason,cancelTime
		orders.setUserId(BaseContext.getCurrentId());
		orders.setPayStatus(Orders.UN_PAID);
		orders.setOrderTime(LocalDateTime.now());
		orders.setStatus(Orders.PENDING_PAYMENT);
		orders.setNumber(String.valueOf(System.currentTimeMillis()));
		// 收件人
		orders.setConsignee(addressBook.getConsignee());
		orders.setPhone(addressBook.getPhone());

		int insert = orderMapper.insert(orders);
		/// 向订单明细表插入n条数据
		List<OrderDetail> details = new ArrayList<>();
		for (ShoppingCart cart : list) {
			OrderDetail detail = new OrderDetail();

			BeanUtils.copyProperties(cart,detail);
			detail.setOrderId(orders.getId());
			details.add(detail);
		}
		orderDetailMapper.insert(details);
		///清空当前用户的购物车数据
		shoppingCartService.remove(new LambdaUpdateWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId,BaseContext.getCurrentId()));
		/// 封装vo返回结果
		OrderSubmitVO resultVo = OrderSubmitVO.builder().id(orders.getId())
				.orderNumber(orders.getNumber())
				.orderAmount(orders.getAmount())
				.orderTime(orders.getOrderTime()).build();
		return resultVo;
	}

	/**
	 *  微信支付
	 *
	 * @param dto
	 * @return {@link OrderPaymentVO }
	 */
	@Override
	@Transactional
	@Operation(summary = "用户订单支付")
	public OrderPaymentVO payment(OrdersPaymentDTO dto) {
		Orders orders = orderMapper.selectOne(new LambdaQueryWrapper<Orders>()
				.eq(Orders::getNumber, dto.getOrderNumber())
				.eq(Orders::getUserId, BaseContext.getCurrentId()));
		if(orders==null){
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		if(Orders.PAID.equals(orders.getPayStatus())){
			/// 已支付
			return OrderPaymentVO.builder().build();
		}
		if(!Orders.PENDING_PAYMENT.equals(orders.getStatus())){
			/// 订单状态不等于待付款
			throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
		}
		if(weChatProperties.isMockPayment()){
			return mockPayment(orders);
		}

		return weChatPayment(orders);
	}

	/**
	 *  模拟支付流程
	 *
	 * @param orders
	 * @return {@link OrderPaymentVO }
	 */
	private OrderPaymentVO mockPayment(Orders orders){
		Orders build = Orders.builder()
				.id(orders.getId())
				/// 设置状态为：待接单
				.status(Orders.TO_BE_CONFIRMED)
				/// 设置支付状态为：已支付
				.payStatus(Orders.PAID)
				.checkoutTime(LocalDateTime.now())
				.build();
		orderMapper.updateById(build);
		return OrderPaymentVO.builder().build();
	}

	private OrderPaymentVO weChatPayment(Orders orders){
		return null;
	}
}
