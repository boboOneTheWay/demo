package redisson;

public interface IDistReadWriteLock {
    IDistLock readLock();

    IDistLock writeLock();
}
