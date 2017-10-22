package threads.synchronizedTest;
/**
 * 关键字synchronized有锁重入功能，当一个线程得到一个对象锁后，再次请求此对象当锁是可以得到的
 * 当存在父子继承当情况是子类完全可以通过可重入锁调用父类当同步方法
 * 父类的同步放法子类继承时同步不会继承，需要在子类加上synchronized关键字
 * 
 * synchronized(this)代码块锁定的是当前对象
 * synchronized同步方法对其他同步方法和synchronized(this)阻塞
 * synchronized(this)对其他同步方法和synthronized(this)阻塞
 * 
 * synchronized(非this对象)，当持有对象监视器为同一个对象当情况下，同一时间只有一个线程可以执行synchronized(非this对象)中的代码
 * synchronized(非this对象)不是一个对象监视器，程序异步执行，与synchronized(this)和synchronized同步方法异步执行
 *
 *synchronized(class)静态方法中的synchronized代码块
 *静态同步方法synchronized和synchronized(class)是一样的，对静态放法加锁或者静态方法代码块中加锁，其实是给类加锁
 *所有不同对象访问呈同步状态，不同于synchronized加到非静态方法是给对象加锁
 *
 *JVM中具有String常量池缓存功能，synchronized(string)同步块与string联合使用时，可能会造成锁不会释放
 *原因：String a = "a"; String b = "b"; a==b is true
 *利用new Object()代替String
 *
 *synchronized和volatile一样，可以将线程工作内存中的私有变量与公共内存中变量同步的过程
 *
 * 
 * @author gaobo
 *
 */
public class SynchronizedTest {

	synchronized public void service1() {
		System.out.println("servcie1");
		service2();
	}
	
	synchronized public void service2() {
		System.out.println("service2");
	}
	
	public static void main(String[] args) {
		MyThread m = new MyThread();
		m.start();
	}
}
