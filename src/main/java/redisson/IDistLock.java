package redisson;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public interface IDistLock extends Lock {
    void lockInterruptibly(long var1, TimeUnit var3) throws InterruptedException;

    boolean tryLock(long var1, long var3, TimeUnit var5) throws InterruptedException;

    void lock(long var1, TimeUnit var3);

    void forceUnlock();

    boolean isLocked();

    boolean isHeldByCurrentThread();
}
