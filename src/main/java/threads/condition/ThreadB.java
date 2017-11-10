package threads.condition;

public class ThreadB extends Thread{

private BoundedQueue<Integer> myThread;
	
	ThreadB(BoundedQueue<Integer> myThread) {
		this.myThread = myThread;
	}
	
	public void run() {
		try {
			while(true) {
				myThread.remove();
				System.out.println("threadB remove");
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
