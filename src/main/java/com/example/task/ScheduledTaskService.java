package com.example.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {

	private static final SimpleDateFormat dataFormate = new SimpleDateFormat("HH:mm:ss");
	
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		System.out.println("每隔五秒执行一次" + dataFormate.format(new Date()));
	}
	
	@Scheduled(cron = "0 52 9 ? * *")
	public void fixTimeExecution() {
		System.out.println("每天九点五十二执行一次" + dataFormate.format(new Date()));
	}
}
