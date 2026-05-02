package com.ck.it.controller.admin;

import com.ck.it.constant.JwtClaimsConstant;
import com.ck.it.dto.EmployeeDTO;
import com.ck.it.dto.EmployeeLoginDTO;
import com.ck.it.dto.EmployeePageQueryDTO;
import com.ck.it.entity.Employee;
import com.ck.it.properties.JwtProperties;
import com.ck.it.result.PageResult;
import com.ck.it.result.Result;
import com.ck.it.service.EmployeeService;
import com.ck.it.utils.JwtUtil;
import com.ck.it.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工接口", description = "员工用户的登录注册操作")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private JwtProperties jwtProperties;

	/**
	 * 登录
	 *
	 * @param employeeLoginDTO
	 * @return
	 */
	@Operation(summary = "员工登录")
	@PostMapping("/login")
	public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
		log.info("员工登录：{}", employeeLoginDTO);

		Employee employee = employeeService.login(employeeLoginDTO);

		//登录成功后，生成jwt令牌
		Map<String, Object> claims = new HashMap<>();
		claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
		String token = JwtUtil.createJWT(
				jwtProperties.getAdminSecretKey(),
				jwtProperties.getAdminTtl(),
				claims);

		EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
				.id(employee.getId())
				.userName(employee.getUsername())
				.name(employee.getName())
				.token(token)
				.build();

		return Result.success(employeeLoginVO);
	}

	/**
	 * 退出
	 *
	 * @return
	 */
	@Operation(summary = "员工退出登录", security = @SecurityRequirement(name = "token"))
	@PostMapping("/logout")
	public Result<String> logout() {
		return Result.success();
	}

	/**
	 * 新增员工
	 *
	 * @param employee
	 * @return {@link Result }
	 */
	@PostMapping
	@Operation(summary = "新增员工")
	public Result<Object> save(@RequestBody EmployeeDTO employee) {
		log.info("新增员工：{}", employee);
		employeeService.save(employee);
		return Result.success();
	}

	@GetMapping("page")
	@Operation(summary = "员工分页查询")
	public Result<PageResult> page(EmployeePageQueryDTO dto) {
		log.info("员工分页查询，参数为：{}",dto);
		PageResult pageResult = employeeService.pageQuery(dto);
		return Result.success(pageResult);
	}


}
