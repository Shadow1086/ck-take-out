package com.ck.it.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.properties
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/5 19:48
 */
@Data
@ConfigurationProperties(prefix = "sky.map")
@Component
public class MapProperties {
	private String location;
	private String baidu;
}
