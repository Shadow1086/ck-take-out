package com.ck.it.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.ck.it.task
 * Description:
 * <p>
 *     自定义定时任务类
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/5 20:53
 */
@Component
@Slf4j
public class MyTask {

	@Scheduled(cron = "0/5 * * * * ?")
	public void executeTask(){
		log.info("定时任务开始执行：{}",new Date());
	}
}
