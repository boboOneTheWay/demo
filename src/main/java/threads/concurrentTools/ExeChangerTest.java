package threads.concurrentTools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程间数据交换
 * @author gaobo
 *
 */
public class ExeChangerTest {
/**
 * Exechanger: 是一个用于线程间协作的工具类。用于线程间数据交换，它提供一个同步点，在这个同步点两个线程通过exchange方法交换数据，如果一个线程先执行exchange方法，它会一直等待第二个线程也执行exchange方法
 * 				构造参数为需要交换的数据
 * 
 */
	
	private static final Exchanger<String> exgr = new Exchanger<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) {
		pool.execute(new Runnable() {

			@Override
			public void run() {
				String A = "银行流水A";
				try {
					String B = exgr.exchange(A);
					System.out.println("	交换得到B的数据：" + B);
					System.out.println("A执行结束");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}});
		
		pool.execute(new Runnable() {

			@Override
			public void run() {
				String B = "银行流水B";
				try {
					String A = exgr.exchange(B);
					System.out.println("	交换得到A的数据：" + A);
					System.out.println("B执行结束");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}});
	}
}
