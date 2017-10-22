package threads.interruptTest;
/**
 * interrupted()测试的是当前的线程的中断状态,是静态方法（它测试的是当前线程的中断状态）,返回中断状态，并且同时清空中断状态
 * isInterrupted()测试的是调用该方法的对象所表示的线程,是实例方法（它测试的是实例对象所表示的线程的中断状态。返回中断状态，不会清空中断状态。
 * @author gaobo
 *
 */

public class MyThread extends Thread {

	@Override
	public void run(){
		super.run();
		for(int i = 0; i<= 500000; i++) {
			System.out.println("i = " + (i + 1));
		}
		boolean flag2 = Thread.interrupted();
		System.out.println("interrupted是否停止1" + flag2);
		boolean flag3 = Thread.interrupted();
		System.out.println("interrupted是否停止1" + flag3);
	}
	
	public static void main(String[] args) throws InterruptedException {
		MyThread myThread = new MyThread();
		myThread.start();
		Thread.sleep(1000);
		myThread.interrupt();
		boolean flag = myThread.isInterrupted();
		boolean flag1 = myThread.isInterrupted();
		myThread.interrupt();
		Thread.sleep(3000);
		
		System.out.println("isInterrupted是否停止1" + flag);
		System.out.println("isInterrupted是否停止2" + flag1);
		System.out.println(Thread.currentThread().getName());
		Thread.interrupted();
		Thread.interrupted();
		
	}
}
