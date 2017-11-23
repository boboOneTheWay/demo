package threads.lock.readWriteLockTest;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {

	private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
	//private static Lock lock = new ReentrantLock();
	private static Lock readLock = reentrantReadWriteLock.readLock();
	private static Lock writeLock = reentrantReadWriteLock.writeLock();
	private int value;

	public int handleRead(Lock lock) throws InterruptedException {

		try {
			lock.lock();
			Thread.sleep(1000);
			System.out.println("read : " + value);
			return value;
		} finally {
			lock.unlock();
		}
	}

	public void handleWrite(Lock lock, int index) throws InterruptedException {
		try {
			lock.lock();
			Thread.sleep(1000);
			value = index;
			System.out.println("write : " + value);
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final ReadWriteLockDemo demo = new ReadWriteLockDemo();
		Runnable read = new Runnable() {

			@Override
			public void run() {
				try {
					demo.handleRead(readLock);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};

		Runnable write = new Runnable() {

			@Override
			public void run() {
				try {
					demo.handleWrite(writeLock, new Random().nextInt());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};

		for(int i = 0; i<18; i++) {
			new Thread(read).start();
			if(i/2 == 0) {
				new Thread(write).start();
			}
		}
	}
}
