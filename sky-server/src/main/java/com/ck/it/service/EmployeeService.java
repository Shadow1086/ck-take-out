package com.ck.it.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ck.it.dto.EmployeeDTO;
import com.ck.it.dto.EmployeeLoginDTO;
import com.ck.it.dto.EmployeePageQueryDTO;
import com.ck.it.entity.Employee;
import com.ck.it.result.PageResult;

public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

	/**
	 *  新增员工业务方法
	 *
	 * @param employee
	 */
	void save(EmployeeDTO employee);

	PageResult pageQuery(EmployeePageQueryDTO dto);

	void startOrStop(Integer status, Long id);

	int updateEmployee(EmployeeDTO dto);
}
