package com.example.task;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestMain {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskSchedulerConfig.class);
	}
}
