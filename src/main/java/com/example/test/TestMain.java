package com.example.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestMain {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);
		
		TestAsync async = context.getBean(TestAsync.class);
		for(int i= 0; i<=10; i++) {
			async.executeAsnyTask(i);
			async.executeAsyncTaskPlus(i);
		}
		
		context.close();
	}
}
