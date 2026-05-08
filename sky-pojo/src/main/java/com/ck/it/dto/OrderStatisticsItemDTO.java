package com.ck.it.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.dto
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/8 16:59
 */
@Data
public class OrderStatisticsItemDTO {
	private LocalDate orderDate;
	private Integer orderCount;
	private Integer validOrderCount;
}
