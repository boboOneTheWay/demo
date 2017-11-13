package threads.executorTest;
/**
 * Executor框架
 * @author gaobo
 *
 */
public class ExecutorTest {
/**
 * Executor框架两级调度模式
 * 		1.在HotSpot VM的线程模型中，java线程被一对一映射到本地操作系统线程。java线程启动时会创建一个本地线程操作系统线程，当该线程终止时，这个操作系统线程也会被回收。操作系统会调度所有的线程并将它们分配给可用的cpu
 * 		2.在上层java多线程程序会把应用分解成多个任务，然后使用用户级的调度框架Executor框架将这些任务映射为固定数量的线程，在底层操作系统内核将这些线程映射到硬件处理器上，下层调度由操作系统内核控制，不受应用程序控制。
 * 
 * Executor框架成员：主要成员ThreadPoolExecutor、ScheduleThreadPoolExecutor、Future接口、Runnable接口、Callable接口和Executors
 * 		1.ThreadPoolExecutor：通常使用工厂类Executors来创建，Executors可以创建三种类型的ThreadPoolExecutor
 * 				(1).FixedThreadPool:用来创建固定线程数的线程池，适用于负载比较重的服务器。
 * 				(2).SingleThreadExecutor：用于创建单个线程的线程池，适用于需要保证顺序执行的各个任务，并且在任意时间点，不会有多个线程活动的场景。
 * 				(3).CacheThreadPool：是大小无界的线程池，适用于执行很多的短期异步的小程序，或者负载比较轻的服务器。
 * 		2.ScheduledThreadPoolExecutor：由Executor创建，通常可以创建两种类型的ScheduledThreadPoolExecutor
 * 				(1).ScheduleThreadPoolExecutor:适用于多个后台线程周期性的执行任务，同时为了满足资源管理需求而需要限制后台线程数的场景。
 * 				(2).SingleThreadScheduleExecutor:适用于需要单个后台线程执行周期性的任务，同时需要保证顺序执行的各个应用场景。
 * 		3.Future接口和实现Future接口的FutureTask类用来异步计算结果，当将Runnable接口或者Callable接口submit给ThreadPoolExecutor或者ScheduledThreadPoolExecutor时，返回一个FutureTask对象。
 * 		4.Runnable接口和Callable接口：都可以被ThreadPoolExecutor或者ScheduledThreadPoolExecutor执行，区别是Runnable不会返回结果，而callable接口可以返回结果
 * 				除此之外可以使用Executors讲一个Runnable包装成一个Callable
 * 		
 */
}
