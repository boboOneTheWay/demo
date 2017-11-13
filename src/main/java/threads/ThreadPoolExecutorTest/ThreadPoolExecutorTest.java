package threads.ThreadPoolExecutorTest;
/**
 * 线程池原理
 * @author gaobo
 *
 */
public class ThreadPoolExecutorTest {
/**
 * 当一个新任务提交到线程池，线程池做如下处理：
 * 1.线程池判断核心线程池里的线程是否都在执行，如果不是，则创建一个新的工作线程来执行任务。如果核心线程池里的线程都在这执行任务，则进入下个流程。
 * 2.线程池判断工作队列是否已经满了。如果工作线程队列没有满，则提交新任务存储在这个工作线程队列里。如果工作队列满了，则进入下个流程。
 * 3.线程池判断线程池的线程时都都处于工作状态，如果没有，则新创建一个新的工作线程来执行任务，如果已经满了，则交给饱和策略来处理这个任务。
 * 
 * ThreadPoolExecutor:执行execute会分以下4种情况
 * 		1.如果当前线程少于corePoolSize，则创建新线程来执行任务（这一步骤需要全局锁）
 * 		2.如果运行的线程等于或者多于corePoolSize，则将任务加入BlockingQueue
 * 		3.如果无法将任务加入BlockingQueue(队列已满)，则创建新的线程来处理任务（这一步骤需要全局锁）
 * 		4.如果创建线程将使当前运行线程超出maxPoolSize任务被拒绝，并调用RejectedExecutionHandler.rejectedExecution()方法
 * 
 * ThreadPoolExecutor是为了在执行execute()方法时，尽可能避免获取全局锁(该操作是个性能瓶颈)，
 * 		在ThreadPoolExecutor完成预热后(当前线程数大于或者等于corePoolSize)，几乎所有的execute()方法都是调用步骤2，步骤2不需要全局锁
 * 
 * 
 * 
 * 线程池的创建：创建线程池会需要一下几个参数：
 * 		1.corePoolSize:线程池的基本大小，当提交一个任务到线程池时，线程池会创建一个线程来执行任务，即使其他空闲基本线程能够执行新任务也会创建线程，等到需要执行到任务数大于线程池基本大小就不会创建
 * 		2.runnableTaskQueue：用于保存等待执行任务到阻塞队列。
 * 				(1).ArrayBlockingQueue：是一个基于数组结构的有界队列
 * 				(2).LinkedBlockingQueue：基于链表的阻塞队列，吞吐量高于ArrayBlockingQueue，静态工厂方法Execytors.newFixedThreadPool()使用该队列。
 * 				(3).SynchronousQueue：不存存储元素的队列，每个插入操作必须等到另外一个线程调用移除操作，否则插入会一直处于阻塞状态，吞吐量高于LinkedBlockingQueue，静态工厂方法Execytors.newCachedThreadPool()使用该队列
 * 				(4).PriorityBlocking：一个有优先级的阻塞队列。
 * 		3.maximumPoolSize：线程池允许最大的线程数。
 * 		4.ThreadFactory：用于设置创建线程的工厂。
 * 		5.RejectedExecutionHandler：饱和策略
 * 
 * 
 * 向线程池提交任务：
 * 		1.execute():用于提交不需要返回值的任务，所以无法判断是否执行成功
 * 		2.submit():用于提交有返回值的任务，线程池会返回一个future类型的对象，通过这个future对象可以判断任务是否执行成功，并通过future的get()方法获取返回值，get方法会阻塞到线程之行完成
 * 		
 */
}
