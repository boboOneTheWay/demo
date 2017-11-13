package threads.concurrentTools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 同步屏障CyclicBarrier
 * @author gaobo
 * 
 * CyclicBarrier和CountDownLatch的区别：
 * 		CountDownLatch的计数器只能使用一次，而CyclicBarrier的计数器可以使用reser()方法重置，如果发生错误可以重置计数器，并让线程重新执行一次。
 * 		CyclicBarrier还提供了其他的放法，比如getNumberWaiting方法可以获得阻塞的线程数。isBroken()方法用来了解阻塞是否被中断
 *
 */
public class CyclicBarrierTest {
/**
 * CyclicBarrier默认的构造方法是CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用await告诉CyclicBarrier我们已到达屏障，然后当前线程被阻塞
 * 
 */
	public static void main(String[] args) {
		CyclicBarrier c = new CyclicBarrier(2);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					c.await();
					System.out.println(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				
			}}).start();
		
		try {
			c.await();
			System.out.println(2);
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		
		/**
		 * CyclicBarrier还提供了一个更高级的构造函数，CyclicBarrier(int p,Runnable a),用于在到达屏障时优先执行a线程。
		 */
		CyclicBarrier d = new CyclicBarrier(2, new A());
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					d.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println(3);
				d.reset();
				
			}}).start();
		
		try {
			d.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
	static class A implements Runnable {
		@Override
		public void run() {
			System.out.println(4);
		}
	}
	
	
	
	
}
