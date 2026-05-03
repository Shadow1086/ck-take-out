package com.ck.it.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 18:16
 */
@Configuration
@Slf4j
public class RedisConfiguration {
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
		log.info("开始创建redis的模板对象...");
		RedisTemplate redisTemplate = new RedisTemplate();
		/// 设置redis的连接工厂对象，由spring-boot-starter-data-redis自动创建
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		/// 设置redis key的序列化器
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);

		redisTemplate.setValueSerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(stringRedisSerializer);

		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
