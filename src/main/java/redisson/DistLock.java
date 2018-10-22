package redisson;

import org.redisson.core.RLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class DistLock implements IDistLock {
    private final RLock lockInner;

    public DistLock(RLock lockInner) {
        this.lockInner = lockInner;
    }

    public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
        this.lockInner.lockInterruptibly(leaseTime, unit);
    }

    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return this.lockInner.tryLock(waitTime, leaseTime, unit);
    }

    public void lock(long leaseTime, TimeUnit unit) {
        this.lockInner.lock(leaseTime, unit);
    }

    public void forceUnlock() {
        this.lockInner.forceUnlock();
    }

    public boolean isLocked() {
        return this.lockInner.isLocked();
    }

    public boolean isHeldByCurrentThread() {
        return this.lockInner.isHeldByCurrentThread();
    }

    public void lock() {
        this.lockInner.lock();
    }

    public void lockInterruptibly() throws InterruptedException {
        this.lockInner.lockInterruptibly();
    }

    public boolean tryLock() {
        return this.lockInner.tryLock();
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return this.lockInner.tryLock(time, unit);
    }

    public void unlock() {
        this.lockInner.unlock();
    }

    public Condition newCondition() {
        return this.lockInner.newCondition();
    }
}
