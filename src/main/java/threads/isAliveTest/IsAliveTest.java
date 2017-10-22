package threads.isAliveTest;

/**
myThread start
Thread.currentThread().getName() : main
Thread.currentThread().isAlive()true
this.isAlive()false
this.getName() : Thread-0
myThread end

mian begin t1 :false
main end t1 :true

run start
Thread.currentThread().getName() : Q
Thread.currentThread().isAlive()true
this.isAlive()false
this.getName() : Thread-0
run end

Thead.currentThread和this的区别：
Thread.currentThread().getName()调用的是执行此行代码的线程的getName方法；
this.getName()调用的是该对象的getName方法,this是线程类MyThread的引用，该方法只能用在Thread的子类中
如果线程类启动是直接new对象则Thread和this代表的是一样的，如果是以参数传入的方法，this是指参数线程对象的引用，Thread指的是Thread启动类
 * @author gaobo
 *
 */
public class IsAliveTest {
	public static void main(String args[]) {
		MyThread m = new MyThread();
		Thread	t1 = new Thread(m);
		System.out.println("mian begin t1 :" + t1.isAlive() + "--t1.getId:" + t1.getId());
		t1.setName("Q");
		t1.start();
		System.out.println("main end t1 :" + t1.isAlive());
	}
	
}
