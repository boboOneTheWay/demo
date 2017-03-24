package com.example.test;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class TestAsync {

	@Async
	public Future<String> executeAsnyTask(Integer i) {
		System.out.println("执行异步任务 ：" + i);
		String name = Thread.currentThread().getName();
		return new AsyncResult<String>(name);
	}
	@Async
	public Future<String> executeAsyncTaskPlus(Integer i) {
		System.out.println("执行异步任务 + 1 : " + (i +1));
		String name = Thread.currentThread().getName();
		return new AsyncResult<String>(name);
	}
}
