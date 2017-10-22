package threads.product;

public class ThreadA extends Thread{
	private MyThread myThread;
	
	ThreadA(MyThread myThread) {
		this.myThread = myThread;
	}
	
	public void run() {
		try {
			while(true) {
				myThread.add();
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
