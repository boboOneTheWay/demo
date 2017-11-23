package threads.daemon;
/**
 * 
 * (1) thread.setDaemon(true)必须在thread.start()之前设置，否则会跑出一个IllegalThreadStateException异常。你不能把正在运行的常规线程设置为守护线程。
   (2) 在Daemon线程中产生的新线程也是Daemon的。 
   (3) 不要认为所有的应用都可以分配给Daemon来进行服务，比如读写操作或者计算逻辑。
   (4) 一个java应用内只有守护线程时，java虚拟机就会推出
 *
 */
public class DeamonTest {

	public static void main(String[] args) throws InterruptedException {
		
//		Thread t1 = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				while (true) {
//					System.out.println("deamon666");
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//
//			}
//		});
//		t1.start();
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					System.out.println("deamon");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		t.setDaemon(true);
		t.start();
		Thread.sleep(2000);
		System.out.println("main方法退出");
	}
}
