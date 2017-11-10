package threads.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedQueue<T> {

	private Object[] items;
	private int addIndex, removeIndex, count;
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
	private Condition notFull = lock.newCondition();

	public BoundedQueue(int size) {
		items = new Object[size];
	}

	public void add(T t) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length) {
				System.out.println("开始等待");
				notFull.await();
			}
			items[addIndex] = t;
			if (++addIndex == items.length) {
				addIndex = 0;
			}
			++count;
			notEmpty.signal();
			
		} finally {
			lock.unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public T remove() throws InterruptedException {
		lock.lock();
		try {
			while(count == 0) 
				notEmpty.await();
			Object x = items[removeIndex];
			if(++removeIndex == items.length) {
				removeIndex = 0;
			}
			--count;
			notFull.signal();
			return (T)x;
		} finally {
			lock.unlock();
		}
	}
	
	
	public static void main(String[] args) {
		BoundedQueue<Integer> myThread = new BoundedQueue<Integer>(5);
		ThreadA threadA = new ThreadA(myThread);
		threadA.setName("threadA");
		ThreadA thread1 = new ThreadA(myThread);
		ThreadA thread2 = new ThreadA(myThread);
		ThreadA thread3 = new ThreadA(myThread);
		ThreadA thread4 = new ThreadA(myThread);
		ThreadA thread5 = new ThreadA(myThread);
		ThreadA thread6 = new ThreadA(myThread);
		ThreadA thread7 = new ThreadA(myThread);
		ThreadA thread8 = new ThreadA(myThread);
		ThreadA thread9 = new ThreadA(myThread);
		ThreadA thread10 = new ThreadA(myThread);
		ThreadA thread11 = new ThreadA(myThread);
		ThreadA thread12 = new ThreadA(myThread);
		ThreadA thread13 = new ThreadA(myThread);
		thread1.setName("thread1");
		thread2.setName("thread2");
		thread3.setName("thread3");
		thread4.setName("thread4");
		thread5.setName("thread5");
		thread6.setName("thread6");
		thread7.setName("thread7");
		thread8.setName("thread8");
		thread9.setName("thread9");
		thread10.setName("thread10");
		thread11.setName("threa11");
		thread12.setName("thread12");
		thread13.setName("thread13");
		ThreadB threadB = new ThreadB(myThread);
		threadA.start();
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
		thread6.start();
		thread7.start();
		thread8.start();
		thread9.start();
		thread10.start();
		thread11.start();
		thread12.start();
		thread13.start();
		threadB.start();
	}
}
