package threads.synchronizedTest;

public class MyThread extends Thread{

	public void run() {
		SynchronizedTest synchronizedTest = new SynchronizedTest();
		synchronizedTest.service1();
		new Runnable(){
			public void run() {}
		};
	}
}
