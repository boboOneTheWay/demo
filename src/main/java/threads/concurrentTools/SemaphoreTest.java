package threads.concurrentTools;
/**
 * semaphore:控制并发线程数
 * 		用来控制同时访问特定资源的线程数
 * @author gaobo
 *
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
/**
 * semaphore 可以用于流量控制，特别是公用资源有限的应用场景
 * 构造方法接收一个整形的数字，表示可用的许可证数，也就是最大并发数
 * 首先线程使用semaphore的acquire()方法获取一个许可证，使用完之后调用release方法归还许可证，还可以使用tryAcquire()方法尝试获取许可证
 * 
 * 其他方法：
 * 		intavailablePermits():返回此信号量中当前可用的许可证数。
 * 		intQueueLength():返回正在等待获取许可证的线程数。
 * 		booleanhasQueueThreads():是否有线程正在获取许可证。
 * 		void reducePermits(int p):减少p个许可证，是protected方法
 * 		Collection getQueuedThreads():返回所有等待获取许可证的线程集合，是个protected方法
 */
	private static ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(30);
	private static Semaphore s = new Semaphore(10);
	public static void main(String[] args) {
		for(int i=0 ; i< 30; i++) {
			newFixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					try {
						s.acquire();
						
						System.out.println("save data, name" + Thread.currentThread().getName());
						s.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}});
		}
		
		newFixedThreadPool.shutdown();
		
	}
	
}
