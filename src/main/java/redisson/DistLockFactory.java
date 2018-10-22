package redisson;

import org.redisson.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistLockFactory implements IDistLockFactory {
    @Autowired
    private RedissonClient redisson;

    public DistLockFactory() {
    }

    public IDistLock getLock(String name) {
        return new DistLock(this.redisson.getLock(name));
    }

    public IDistReadWriteLock getReadWriteLock(String name) {
        return new DistReadWriteLock(this.redisson.getReadWriteLock(name));
    }
}
