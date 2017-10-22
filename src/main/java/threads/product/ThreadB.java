package threads.product;

public class ThreadB extends Thread{

private MyThread myThread;
	
	ThreadB(MyThread myThread) {
		this.myThread = myThread;
	}
	
	public void run() {
		try {
			while(true) {
				myThread.pull();
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
