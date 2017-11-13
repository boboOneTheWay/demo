package threads.concurrentTools;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
/**
 * CountDownLatch:允许一个或多个线程等到其他线程完成操作。
 * CountDownLatch的构造函数接收一个int类型的参数做为计数器，如果想等待N个点完成，就传入N，调用countDown方法时，N就会减1，CountDownLatch的await方法会阻塞当前线程，直到N变为0
 * 		由于countDown方法可以使用在任何地方，这里的N个点可以时N个线程，也可以时1到N的步骤，在多个线程里时只需要把这个COuntDownLatch的引用传递到线程里即可。
 * 		计数器必须大于0，只是等于0的时候，调用await方法时不会阻塞当前线程，如果某个线程执行的比较慢，可以使用一个待带时间的await方法--await(long time,TimeUnit unit)
 * @throws InterruptedException 
 * 
 */
	
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch c = new CountDownLatch(3);
		new Thread(new Runnable() {
			public void run() {
				System.out.println(1);
				c.countDown();
				System.out.println(2);
				
			}
		}).start();;
		c.countDown();
		c.countDown();
		System.out.println(3);
		c.await();
	}
}
