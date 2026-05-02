package com.ck.it.service;

import com.ck.it.dto.EmployeeLoginDTO;
import com.ck.it.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
