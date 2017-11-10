package threads.blockingQueue;

public class BlockingQueue {
/**
 * 阻塞队列是一个支持两个附加操作的队列，这两个附加操作支持阻塞的插入和移除
 * (1)支持阻塞插入的方法：当队列满时，队列会阻塞插入元素的线程，直到队列不满。
 * (2)支持阻塞的移除方法：当队列为空时，获取元素的线程队列为空。
 * 
 * 如果生产者线程往队列里put元素，队列会一直阻塞生产者线程，直到队列可用或者响应中断退出
 * 当队列为空时，如果消费者线程从队列里take元素，队列会阻塞住消费者线程，直到队列不为空
 * 超时退出：当阻塞队列满时，如果生产者线程往队列里插入元素，队列会阻塞生产者线程一段时间，如果超出指定时间，生产者线程会退出
 * 
 * 1.ArrayBlockingQueue:是一个数组实现的有界队列，先进先出，默认不保证线程公平的访问队列，为了保证公平性通常会降低通途量
 * 	 ArrayBlockingQueue a = new ArrayBlockingQueue(1000,true);
 * 
 * 2.LinkedBlockingQueue:是一个用链表实现的有界阻塞队列，此队列的默认和最大长度为Integer.MAX_VALUE,先进先出
 * 
 * 3.PriorityBlockingQueue：是一个支持优先级的无界阻塞队列，默认采用自然顺序升序排列，也可以使用compareTo()方法来指定默认元素排序。
 * 
 * 4.DelayQueue：是一个支持延时获取无界阻塞队列，队列中元素必须实现Delayed接口。只有延时满时才能从队列中提取元素。
 * 		缓存系统设计时，可以使用DelayQueue保存缓存元素有效期，使用一个线程循环查询DelayQueue，一旦从DelayQueue中获取到元素，表示缓存有效期到了
 * 
 * 5.SychronousQueue:是一个不存储元素的阻塞队列，每一个put操作必须等待一个take操作，否则不能继续添加元素，它支持公平的访问队列，默认是非公平的，参数为true时采用顺序先进先出的顺序访问队列
 * 		可以看做是一个传球手，负责把生产者处理的数据直接传递给消费者线程，队列并不存任元素，适合传递性场景，吞吐量高于LinkedBlockingQueue／ArrayBlockingQueue
 * 
 * 6.LinkedTransferQueue:是一个由链表结构组成的无界阻塞队列，相对于其他阻塞队列，多了tryTransferQueue和transfer
 * 		(1):transfer：如果当前消费者正在等待接收元素，transfer可以把生产者传入的元素立刻transfer(传输)给消费者，如果没有消费者在等待接收元素，transfer放法会将元素存放在队列里的tail节点里
 * 				并等该元素并消费者消费了才会返回
 * 		(2):tryTransfer：用来试探生产者传入的元素是否能够直接传递给消费者，如果没有消费者等待接收元素，返回false。方法会立即返回，transfer是等消费者消费了才返回。
 * 	
 * 7.LinkedBlockingDeque:由一个链表组成的双向阻塞队列，可以从队列两端插入和移除元素，相比其他队列多了addFirst,addLast,offerLast,peekFirst,peekLast
 * 		可以设置LinkedBlockingDeque时可以设置容量防止其过度膨胀，另外双向阻塞队列可以作用在工作窃取模式中。
 * 
 */
}
