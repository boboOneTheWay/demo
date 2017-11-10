package threads.condition;

public class ThreadA extends Thread{
	private BoundedQueue<Integer> myThread;
	
	ThreadA(BoundedQueue<Integer> myThread) {
		this.myThread = myThread;
	}
	
	public void run() {
		try {
			while(true) {
				myThread.add(1);
				System.out.println("threadA add, name" + Thread.currentThread().getName());
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
