package redisson;

import org.redisson.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistObjectFactory implements IDistObjectFactory {
    @Autowired
    private RedissonClient redisson;

    public DistObjectFactory() {
    }

    public IDistAtomicLong getAcomicLong(String name) {
        return new DistAtomicLong(this.redisson.getAtomicLong(name));
    }
}
