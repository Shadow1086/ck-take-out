package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.MessageConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.OrdersPageQueryDTO;
import com.ck.it.dto.OrdersPaymentDTO;
import com.ck.it.dto.OrdersRejectionDTO;
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
import com.ck.it.result.PageResult;
import com.ck.it.service.AddressBookService;
import com.ck.it.service.OrderService;
import com.ck.it.service.ShoppingCartService;
import com.ck.it.utils.WeChatPayUtil;
import com.ck.it.vo.OrderPaymentVO;
import com.ck.it.vo.OrderStatisticsVO;
import com.ck.it.vo.OrderSubmitVO;
import com.ck.it.vo.OrderVO;
import com.ck.it.websocket.WebSocketServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
	private WebSocketServer webSocketServer;
	@Autowired
	private WeChatProperties weChatProperties;
	@Autowired
	private WeChatPayUtil weChatPayUtil;
	@Autowired
	private UserMapper userMapper;

	@Value("${sky.map.baidu}")
	private String sk;
	@Value("${sky.map.location}")
	private String shop;

	@Override
	@Transactional
	@Operation(summary = "用户下单")
	public OrderSubmitVO submit(OrdersSubmitDTO dto) {
		/// 处理业务异常

		/// 地址簿为空
		AddressBook addressBook = addressBookService.getById(dto.getAddressBookId());
		if (addressBook == null) {
			throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
		}
		/// 购物车数据为空
		ShoppingCart shoppingCart = new ShoppingCart();
		List<ShoppingCart> list = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
		if (list == null || list.isEmpty()) {
			throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
		}
		/// 向订单表插入一条数据
		Orders orders = new Orders();
		// addressBookId,payMethod,remark,estimatedDeliveryTime
		// DeliveryStatus,TablewareNumber,tableWareStatus,PackAmount,Amount
		BeanUtils.copyProperties(dto, orders);

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

			BeanUtils.copyProperties(cart, detail);
			detail.setOrderId(orders.getId());
			details.add(detail);
		}
		orderDetailMapper.insert(details);
		///清空当前用户的购物车数据
		shoppingCartService.remove(new LambdaUpdateWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
		/// 封装vo返回结果
		OrderSubmitVO resultVo = OrderSubmitVO.builder().id(orders.getId())
				.orderNumber(orders.getNumber())
				.orderAmount(orders.getAmount())
				.orderTime(orders.getOrderTime()).build();
		return resultVo;
	}

	/**
	 * 查询历史订单
	 *
	 * @param page
	 * @param pageSize
	 * @param status
	 * @return {@link PageResult }
	 */
	@Override
	public PageResult historyOrders(Integer page, Integer pageSize, Integer status) {
		IPage<Orders> pageResult = new Page<>(page, pageSize);

		LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
		if (status != null) {
			/// TODO 前端传入1(待付款)但是展示的是6(以取消)的数据，应该是数据库没有更新status字段
			wrapper.eq(Orders::getStatus, status);
		}
		wrapper.orderByDesc(Orders::getOrderTime);
		IPage<Orders> ordersIPage = orderMapper.selectPage(pageResult, wrapper);

		PageResult result = new PageResult();
		result.setTotal(ordersIPage.getTotal());

		List<Orders> records = ordersIPage.getRecords();
		List<OrderVO> recordVos = new ArrayList<>();
		for (Orders record : records) {
			OrderVO orderVO = new OrderVO();
			BeanUtils.copyProperties(record, orderVO);
			List<OrderDetail> details = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
					.eq(OrderDetail::getOrderId, record.getId()));
			orderVO.setOrderDetailList(details);
			recordVos.add(orderVO);
		}
		result.setRecords(recordVos);

		return result;
	}

	/**
	 * 订单详情
	 *
	 * @param id
	 * @return {@link OrderVO }
	 */
	@Override
	public OrderVO orderDetail(Long id) {
		Orders orders = orderMapper.selectById(id);
		OrderVO orderVO = new OrderVO();
		if (orders != null && orders.getUserId().equals(BaseContext.getCurrentId())) {
			BeanUtils.copyProperties(orders, orderVO);
			List<OrderDetail> details = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
					.eq(OrderDetail::getOrderId, orders.getId()));
			orderVO.setOrderDetailList(details);
		} else {
			return null;
		}
		return orderVO;
	}

	/**
	 * 用户取消订单
	 *
	 * @param id
	 * @return boolean
	 */
	@Override
	@Transactional
	public boolean cancelOrder(Long id) {
		/// 验证订单所属用户是否为当前用户
		Orders orders = orderMapper.selectById(id);
		if (!orders.getUserId().equals(BaseContext.getCurrentId())) {
//			throw new OrderBusinessException(MessageConstant.)
			return false;
		}
		/// 删除数据库中的Order和OrderDetail
		orderMapper.deleteById(id);
		orderDetailMapper.delete(new LambdaQueryWrapper<OrderDetail>()
				.eq(OrderDetail::getOrderId, id));

		return true;
	}


	/**
	 * 用户再来一单
	 *
	 * @param id
	 * @return boolean
	 */
	@Override
	@Transactional
	public boolean repetition(Long id) {
		/// 验证当前订单所属用户是否为当前用户
		if (id == null) {
			return false;
		}
		Orders orders = orderMapper.selectById(id);
		Long userId = BaseContext.getCurrentId();

		if (orders == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		if (!orders.getUserId().equals(userId)) {
			return false;
		}
		/// 如果是，有权限，复制当前订单的购物车明细
		/// 清空购物车
		shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>()
				.eq(ShoppingCart::getUserId, userId));

		List<ShoppingCart> carts = new ArrayList<>();

		List<OrderDetail> details = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
				.eq(OrderDetail::getOrderId, orders.getId()));
		if (details == null || details.isEmpty()) {
			return false;
		}
		for (OrderDetail detail : details) {
			ShoppingCart cart = new ShoppingCart();
			BeanUtils.copyProperties(detail, cart);
			cart.setId(null);
			cart.setUserId(userId);
			LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<ShoppingCart>()
					.eq(ShoppingCart::getUserId, userId);
			if (cart.getDishId() != null) {
				wrapper.eq(ShoppingCart::getDishId, cart.getDishId())
						.isNull(ShoppingCart::getSetmealId);
			} else {
				wrapper.eq(ShoppingCart::getSetmealId, cart.getSetmealId())
						.isNull(ShoppingCart::getDishId);
			}
			if (cart.getDishFlavor() != null) {
				wrapper.eq(ShoppingCart::getDishFlavor, cart.getDishFlavor());
			}
			ShoppingCart one = shoppingCartService.getOne(wrapper);
			if (one != null) {
				/// 如果改菜品已在购物车内
				one.setNumber(one.getNumber() + 1);
				shoppingCartService.updateById(one);
			} else {
				cart.setCreateTime(LocalDateTime.now());
				carts.add(cart);
			}
		}
		return shoppingCartService.saveBatch(carts);
	}

	/**
	 * 管理端查询订单详情
	 *
	 * @param id
	 * @return {@link OrderVO }
	 */
	@Override
	public OrderVO queryDetail(Long id) {
		if (id == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}

		Orders orders = orderMapper.selectById(id);
		if (orders == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}

		OrderVO orderVO = new OrderVO();
		BeanUtils.copyProperties(orders, orderVO);
		List<OrderDetail> details = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
				.eq(OrderDetail::getOrderId, orders.getId()));
		orderVO.setOrderDetailList(details);

		return orderVO;
	}


	/**
	 * 管理端订单搜索
	 *
	 * @param dto
	 * @return {@link PageResult }
	 */
	@Override
	public PageResult conditionSearch(OrdersPageQueryDTO dto) {
		IPage<Orders> queryPage = new Page<>(dto.getPage(), dto.getPageSize());
		LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
//		wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
		if (dto.getBeginTime() != null) {
			wrapper.ge(Orders::getOrderTime, dto.getBeginTime());
		}
		if (dto.getEndTime() != null) {
			wrapper.le(Orders::getOrderTime, dto.getEndTime());
		}
		if (dto.getNumber() != null) {
			wrapper.eq(Orders::getNumber, dto.getNumber());
		}
		if (dto.getPhone() != null) {
			wrapper.like(Orders::getPhone, dto.getPhone());
		}
		if (dto.getStatus() != null) {
			wrapper.eq(Orders::getStatus, dto.getStatus());
		}
		IPage<Orders> ordersIPage = orderMapper.selectPage(queryPage, wrapper);
		PageResult pageResult = new PageResult();
		pageResult.setTotal(ordersIPage.getTotal());
		pageResult.setRecords(ordersIPage.getRecords());
		return pageResult;
	}

	/**
	 * 管理端拒单
	 *
	 * @param dto
	 * @return boolean
	 */
	@Override
	public boolean rejection(OrdersRejectionDTO dto) {
		Long id = dto.getId();
		String rejectionReason = dto.getRejectionReason();
		Orders orders = orderMapper.selectById(id);
		if (orders == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		if (!Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED)) {
			throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
		}
		orders.setStatus(Orders.CANCELLED);
		orders.setCancelReason(rejectionReason);
		if (orders.getPayStatus().equals(Orders.PAID)) {
			/// TODO 退款
		}
		int i = orderMapper.updateById(orders);

		return i == 1;
	}

	/**
	 * 各个状态的订单数量统计
	 *
	 * @return {@link OrderStatisticsVO }
	 */
	@Override
	public OrderStatisticsVO statistics() {
		Long toBeConfirmed = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
				.eq(Orders::getStatus, Orders.TO_BE_CONFIRMED));
		Long deliveryInProgress = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
				.eq(Orders::getStatus, Orders.DELIVERY_IN_PROGRESS));
		Long confirmed = orderMapper.selectCount(new LambdaQueryWrapper<Orders>()
				.eq(Orders::getStatus, Orders.CONFIRMED));
		OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
		orderStatisticsVO.setConfirmed(confirmed.intValue());
		orderStatisticsVO.setDeliveryInProgress(deliveryInProgress.intValue());
		orderStatisticsVO.setToBeConfirmed(toBeConfirmed.intValue());

		return orderStatisticsVO;
	}

	/**
	 * 派送订单
	 *
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean changeOrderStatus(Long id, Integer status) {
		if (id == null) {
			return false;
		}
		Orders orders = orderMapper.selectById(id);
		if (orders == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		if (orders.getStatus().equals(status) || orders.getStatus().equals(Orders.CANCELLED)) {
			throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
		}
		orders.setStatus(status);
		int i = orderMapper.updateById(orders);

		return i == 1;
	}

	/**
	 * 微信支付
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
		if (orders == null) {
			throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
		}
		if (Orders.PAID.equals(orders.getPayStatus())) {
			/// 已支付
			return OrderPaymentVO.builder().build();
		}
		if (!Orders.PENDING_PAYMENT.equals(orders.getStatus())) {
			/// 订单状态不等于待付款
			throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
		}
		if (weChatProperties.isMockPayment()) {
			return mockPayment(orders);
		}

		return weChatPayment(orders);
	}

	/**
	 * 模拟支付流程
	 *
	 * @param orders
	 * @return {@link OrderPaymentVO }
	 */
	private OrderPaymentVO mockPayment(Orders orders) {
		Orders build = Orders.builder()
				.id(orders.getId())
				/// 设置状态为：待接单
				.status(Orders.TO_BE_CONFIRMED)
				/// 设置支付状态为：已支付
				.payStatus(Orders.PAID)
				.checkoutTime(LocalDateTime.now())
				.build();
		orderMapper.updateById(build);

		// 通过websocket向客户端发送消息，
		Map<String, Object> map = new HashMap<>();
		map.put("type", 1);
		map.put("orderId", build.getId());
		map.put("content", "订单号：" + orders.getNumber());
		try {
			String json = new ObjectMapper().writeValueAsString(map);
			webSocketServer.sendToAllClient(json);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		return OrderPaymentVO.builder().build();
	}

	private OrderPaymentVO weChatPayment(Orders orders) {
		return null;
	}
//
//	private void checkOutOfRange(String address){
//		SearchHttpAK snCal = new
//	}
}
