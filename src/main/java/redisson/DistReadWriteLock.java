package redisson;


import org.redisson.core.RReadWriteLock;

public class DistReadWriteLock implements IDistReadWriteLock {
    private final RReadWriteLock rReadWriteLock;

    public DistReadWriteLock(RReadWriteLock rReadWriteLock) {
        this.rReadWriteLock = rReadWriteLock;
    }

    public IDistLock readLock() {
        return new DistLock(this.rReadWriteLock.readLock());
    }

    public IDistLock writeLock() {
        return new DistLock(this.rReadWriteLock.writeLock());
    }
}
