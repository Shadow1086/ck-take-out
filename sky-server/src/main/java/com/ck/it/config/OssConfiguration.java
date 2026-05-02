package com.ck.it.config;

import com.ck.it.properties.AliOssProperties;
import com.ck.it.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.ck.it.config
 * Description:
 * <p>
 * 配置类，用于创建AliOssUtil对象
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/2 21:51
 */
@Configuration
@Slf4j
public class OssConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
		log.info("开始创建阿里云文件上传工具类对象:{}",aliOssProperties);
		return new AliOssUtil(aliOssProperties.getEndpoint(),
				aliOssProperties.getAccessKeyId(),
				aliOssProperties.getAccessKeySecret(),
				aliOssProperties.getBucketName());
	}
}
