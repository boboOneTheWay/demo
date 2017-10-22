package threads.product;

import java.util.ArrayList;
import java.util.List;
/**
 * @author gaobo
 * 
 * wait()方法可以使调用该方法的线程释放公共资源的锁，退出运行状态，进入阻塞队列等待被唤醒（释放锁）
 * notify()随机唤醒阻塞队列中一个线程，该线程在阻塞队列中退出，加入到可运行队列（不释放锁）
 * 线程处于wait()状态时，调用线程对象的interrupt()方法时会出现异常
 * 
 *
 */
public class MyThread {
	MyThread(List<String> list) {
		this.list = list;
	}
	private List<String> list;

	public void add() throws InterruptedException {
		
		synchronized(list) {
			while(list.size() == 10) {
				list.wait();
			}
			for(int i =0; i< 10; i++) {
				list.add("add***");
				System.out.println(i + "add");
			}
			list.notify();
		}
	}
	
	public void pull() throws InterruptedException {
		synchronized(list) {
			while(list.size() == 0) {
				list.wait();
			}
			for(int i =0; i< 10; i++) {
				list.remove(0);
				System.out.println(i + "remove");
			}
			
			list.notify();
		}
	}
	
	public static void main(String[] args) {
		MyThread myThread = new MyThread(new ArrayList<String>());
		ThreadA threadA = new ThreadA(myThread);
		ThreadB threadB = new ThreadB(myThread);
		threadA.start();
		threadB.start();
	}
}
