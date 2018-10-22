package redisson;

public interface IDistLockFactory {
    IDistLock getLock(String var1);

    IDistReadWriteLock getReadWriteLock(String var1);
}