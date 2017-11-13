package threads.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CoutTask extends RecursiveTask<Integer>{
	private static final long serialVersionUID = 1L;
	private int start;
	private int end;
	private static final int T = 2;//设置分割阀值
	
	public CoutTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		boolean b = (end - start) < T;
		if(b) {
			for(int i = start; i<= end; i++) {
				sum += i;
			}
		} else {
			int middle = (end + start) / 2;
//			System.out.println("-----------------------------------------------");
//			System.out.println("分割左任务start："  + start + ", end : " + middle);
//			System.out.println("分割右任务start："  + (middle + 1) + ", end : " + end);
//			System.out.println("-----------------------------------------------");
			CoutTask leftTask = new CoutTask(start, middle);
			CoutTask rightTask = new CoutTask(middle + 1, end);
			//执行子任务
			leftTask.fork();
			rightTask.fork();
			//子任务执行完后合并
			int le = leftTask.join();
			int ri = rightTask.join();
			sum = le + ri;
		}
		
		return sum;
	}
	
	public static void main (String[] args) {

		long startTime=System.currentTimeMillis(); 
		ForkJoinPool forkJoinPool = new ForkJoinPool(20);
		CoutTask coutTask = new CoutTask(1,10000000);
		ForkJoinTask<Integer> submit = forkJoinPool.submit(coutTask);
		try {
			System.out.println(submit.get());
			long endTime=System.currentTimeMillis(); //获取结束时间
			System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		long startTime1=System.currentTimeMillis(); 
		int sum = 0;
		for(int i =1; i<= 10000000; i++) {
			sum += i;
		}
		System.out.println(sum);
		long endTime1=System.currentTimeMillis(); //获取结束时间
		System.out.println("程序运行时间1： "+(endTime1-startTime1)+"ms");
		
	}

}
