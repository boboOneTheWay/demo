package threads.queuedSynchronizer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
//队列同步器AbstractQueuedSynchronizer是用来构建锁或者其他同步组建的基础框架，使用一个int成员变量表示同步状态，通过FIFO队列来完成资源获取线程排队工作。
//getState：获取当前同步状态，1代表同步
//使用compareAndSetState(int expect,int update) 使用CAS设置当前状态，该方法可以保证状态设置的原子性。
//setState：设置当前同步状态
public class QueuedSynchronizer implements Lock{

	private static class Sync extends AbstractQueuedSynchronizer {
		private static final long serialVersionUID = 1L;
		//是否处于占用状态
		protected boolean isHeldExclusively() {
			//getState：获取当前同步状态，1代表同步
			return getState() == 1;
		}
		//状态为0时获取同步锁
		public boolean tryAcquire(int acquires) {
			//使用compareAndSetState(int expect,int update) 使用CAS设置当前状态，该方法可以保证状态设置的原子性。
			if(compareAndSetState(0,1)) {
				//设置当前拥有独占访问的线程
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}
			return false;
		}
		//释放锁，将状态设置为0
		protected boolean tryRelease(int releases) {
			if(getState() == 0) {
				throw new IllegalMonitorStateException();
			}
			setExclusiveOwnerThread(null);
			setState(0);
			return true;
		}
		//返回一个condition，每个conition都包含一个condition队列
		Condition newCondition() {
			return new ConditionObject();
		}
	}
	//需要将操作代理到Sync上
	private final Sync sync = new Sync();
	@Override
	public void lock() {
		sync.acquire(1);
		
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
		
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.release(1);
		
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}

}
