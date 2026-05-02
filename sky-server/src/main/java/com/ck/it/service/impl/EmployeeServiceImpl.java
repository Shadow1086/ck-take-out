package com.ck.it.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ck.it.constant.MessageConstant;
import com.ck.it.constant.PasswordConstant;
import com.ck.it.constant.StatusConstant;
import com.ck.it.context.BaseContext;
import com.ck.it.dto.EmployeeDTO;
import com.ck.it.dto.EmployeeLoginDTO;
import com.ck.it.dto.EmployeePageQueryDTO;
import com.ck.it.entity.Employee;
import com.ck.it.exception.AccountLockedException;
import com.ck.it.exception.AccountNotFoundException;
import com.ck.it.exception.PasswordErrorException;
import com.ck.it.mapper.EmployeeMapper;
import com.ck.it.result.PageResult;
import com.ck.it.service.EmployeeService;
import com.ck.it.utils.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;

	/**
	 * 员工登录
	 *
	 * @param employeeLoginDTO
	 * @return
	 */
	public Employee login(EmployeeLoginDTO employeeLoginDTO) {
		String username = employeeLoginDTO.getUsername();
		String password = employeeLoginDTO.getPassword();

		//1、根据用户名查询数据库中的数据
		Employee employee = lambdaQuery().eq(Employee::getUsername, username).one();

		//2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
		if (employee == null) {
			//账号不存在
			throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
		}

		// 密码比对：数据库中应保存 BCrypt 哈希值，不能保存明文密码。
		if (!PasswordUtil.matches(password, employee.getPassword())) {
			//密码错误
			throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
		}

		if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
			//账号被锁定
			throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
		}

		//3、返回实体对象
		return employee;
	}

	/**
	 * 新增员工业务方法
	 *
	 * @param dto 前端出来的数据
	 */
	@Override
	public void save(EmployeeDTO dto) {
		Employee employee = new Employee();

		/// 对象的属性拷贝
		BeanUtils.copyProperties(dto, employee);
		// 设置账号状态
		employee.setStatus(StatusConstant.ENABLE);
		employee.setCreateTime(LocalDateTime.now());
		employee.setUpdateTime(LocalDateTime.now());
		// 默认密码 123456 , 存储在 PasswordConstant 中
		String encodePassword = PasswordUtil.encode(PasswordConstant.DEFAULT_PASSWORD);
		employee.setPassword(encodePassword);

		/// 设置当前记录创建人id 和修改人id

		Long currentId = BaseContext.getCurrentId();
		employee.setCreateUser(currentId);
		employee.setUpdateUser(currentId);

		employeeMapper.insert(employee);
	}

	@Override
	public PageResult pageQuery(EmployeePageQueryDTO dto) {
		IPage<Employee> page = new Page<>(dto.getPage(), dto.getPageSize());

		LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.like(
						dto.getName() != null && !dto.getName().isBlank(),
						Employee::getName, dto.getName())
				.orderByDesc(Employee::getCreateTime);

		IPage<Employee> resultPage = employeeMapper.selectPage(page, queryWrapper);
		/// 将结果封装
		PageResult pageResult = new PageResult();
		pageResult.setTotal(resultPage.getTotal());
		pageResult.setRecords(resultPage.getRecords());

		return pageResult;
	}

	@Override
	public void startOrStop(Integer status, Long id) {
		lambdaUpdate().eq(Employee::getId, id)
				.set(Employee::getStatus, status)
				.set(Employee::getUpdateTime, LocalDateTime.now())
				.set(Employee::getUpdateUser, BaseContext.getCurrentId())
				.update();
	}
}
