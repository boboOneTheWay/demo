package threads.isAliveTest;

public class MyThread extends Thread{

	public MyThread() {
			System.out.println("myThread start");
			System.out.println("Thread.currentThread().getName() : " + Thread.currentThread().getName());
			System.out.println("Thread.currentThread().isAlive()" + Thread.currentThread().isAlive() + "--Thread.currentThread().getId():" + Thread.currentThread().getId());
			System.out.println("this.isAlive()" + this.isAlive() + "--this.getId():" + this.getId());
			System.out.println("this.getName() : " + this.getName());
			System.out.println("myThread end");
	}
	
	public void run() {
		System.out.println("run start");
		System.out.println("Thread.currentThread().getName() : " + Thread.currentThread().getName());
		System.out.println("Thread.currentThread().isAlive()" + Thread.currentThread().isAlive());
		System.out.println("this.isAlive()" + this.isAlive());
		System.out.println("this.getName() : " + this.getName());
		System.out.println("run end");
	}
	
}
