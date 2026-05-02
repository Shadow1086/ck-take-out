package com.ck.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ck.it.annotation.AutoFill;
import com.ck.it.entity.Employee;
import com.ck.it.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
//	@AutoFill(OperationType.INSERT)
//	@Override
//	int insert(Employee employee);
}
