package com.ck.it.config;

import com.ck.it.interceptor.JwtTokenAdminInterceptor;
import com.ck.it.interceptor.JwtTokenUserInterceptor;
import com.ck.it.json.JacksonObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类，注册web层相关组件
 */
@OpenAPIDefinition(
		info = @Info(
				title = "长空新闻接口文档,",
				version = "2.0",
				description = "长空新闻spring boot 后端api"
		)
)
@SecurityScheme(
		name = "token",
		type = SecuritySchemeType.APIKEY,
		in = SecuritySchemeIn.HEADER,
		paramName = "token"
)
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Autowired
	private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

	@Autowired
	private JwtTokenUserInterceptor jwtTokenUserInterceptor;

	/**
	 * 注册自定义拦截器
	 *
	 * @param registry
	 */
	public void addInterceptors(InterceptorRegistry registry) {
		log.info("开始注册自定义拦截器...");
		registry.addInterceptor(jwtTokenAdminInterceptor)
				.addPathPatterns("/admin/**")
				.excludePathPatterns("/admin/employee/login");
		registry.addInterceptor(jwtTokenUserInterceptor)
				.addPathPatterns("/user/**")
				.excludePathPatterns("/user/user/login")
				.excludePathPatterns("/user/shop/status");
	}

//    /**
//     * 通过knife4j生成接口文档
//     * @return
//     */
//    @Bean
//    public Docket docket() {
//        ApiInfo apiInfo = new ApiInfoBuilder()
//                .title("苍穹外卖项目接口文档")
//                .version("2.0")
//                .description("苍穹外卖项目接口文档")
//                .build();
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.ck.it.controller"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }

	/**
	 * 设置静态资源映射
	 *
	 * @param registry
	 */
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	/**
	 * 扩展spring mvc框架的消息转化器
	 */
	@Override
	public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
		builder.withJsonConverter(new JacksonJsonHttpMessageConverter(new JacksonObjectMapper()));
	}
}
