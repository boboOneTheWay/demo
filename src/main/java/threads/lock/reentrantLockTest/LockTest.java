package threads.lock.reentrantLockTest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest implements Runnable {

	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	int lock;

	public LockTest(int lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			if (lock == 1) {
				lock1.lockInterruptibly();
				Thread.sleep(500);
				lock2.lockInterruptibly();
			} else {
				lock2.lockInterruptibly();
				Thread.sleep(500);
				lock1.lockInterruptibly();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(lock1.isHeldByCurrentThread()) {
				lock1.unlock();
			}
			if(lock2.isHeldByCurrentThread()) {
				lock2.unlock();
			}
			System.out.println(Thread.currentThread().getId() + ":线程退出");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		/**
		 * 中断响应，解决死锁
		 * lockInterruptibly：获得锁，但优先响应中断
		 */
		LockTest r1 = new LockTest(1);
		LockTest r2 = new LockTest(2);
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();
		Thread.sleep(1000);
		t2.interrupt();
		
		/**
		 * tryLock()
		 *  new ReentrantLock(true) 公平锁，不会产生饥饿现象，但是需要维护一个队列，性能比较低
		 */
		new Thread(new Runnable() {
			ReentrantLock lock = new ReentrantLock(true);
			@Override
			public void run() {
				try {
					if(lock.tryLock(5, TimeUnit.SECONDS)) {
						Thread.sleep(6000);
					} else {
						System.out.println("get lock fail");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
		
	}

}
