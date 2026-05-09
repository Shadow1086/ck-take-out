package com.ck.it.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.dto
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/9 16:49
 */
@Data
public class BusinessDataItemDTO {
	private LocalDate date;
	private BigDecimal turnover;
	private Integer orderCount;
	private Integer validOrderCount;
	private Integer newUsers;
}
