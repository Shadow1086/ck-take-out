package com.ck.it.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.ck.it.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 20:08
 */
@Configuration
public class SwaggerConfiguration {
	@Bean
	public GroupedOpenApi adminApi() {
		return GroupedOpenApi.builder()
				.group("管理端接口")
				.packagesToScan("com.ck.it.controller.admin")
				.pathsToMatch("/admin/**")
				.build();
	}
	@Bean
	public GroupedOpenApi userApi() {
		return GroupedOpenApi.builder()
				.group("用户端接口")
				.packagesToScan("com.ck.it.controller.user")
				.pathsToMatch("/user/**")
				.build();
	}
}
