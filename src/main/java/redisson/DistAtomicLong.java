package redisson;


import org.redisson.core.RAtomicLong;

public class DistAtomicLong implements IDistAtomicLong {
    private final RAtomicLong innerAtomicLong;

    public DistAtomicLong(RAtomicLong innerAtomicLong) {
        this.innerAtomicLong = innerAtomicLong;
    }

    public long getAndDecrement() {
        return this.innerAtomicLong.getAndDecrement();
    }

    public long addAndGet(long delta) {
        return this.innerAtomicLong.addAndGet(delta);
    }

    public boolean compareAndSet(long expect, long update) {
        return this.innerAtomicLong.compareAndSet(expect, update);
    }

    public long decrementAndGet() {
        return this.innerAtomicLong.decrementAndGet();
    }

    public long get() {
        return this.innerAtomicLong.get();
    }

    public long getAndAdd(long delta) {
        return this.innerAtomicLong.getAndAdd(delta);
    }

    public long getAndSet(long newValue) {
        return this.innerAtomicLong.getAndSet(newValue);
    }

    public long incrementAndGet() {
        return this.innerAtomicLong.incrementAndGet();
    }

    public long getAndIncrement() {
        return this.innerAtomicLong.getAndIncrement();
    }

    public void set(long newValue) {
        this.innerAtomicLong.set(newValue);
    }

    public boolean decrementWithBottom(int minAmount, long amount) {
        boolean ret = false;
        int retryTimes = 10;

        for(long old = this.innerAtomicLong.get(); old - amount >= (long)minAmount; --retryTimes) {
            ret = this.innerAtomicLong.compareAndSet(old, old - amount);
            if (ret || retryTimes <= 0) {
                break;
            }

            old = this.innerAtomicLong.get();
        }

        return ret;
    }

    public boolean addWithUpper(long maxAmount, long amount) {
        boolean ret = false;
        int retryTimes = 10;

        for(long old = this.innerAtomicLong.get(); old + amount <= maxAmount; --retryTimes) {
            ret = this.innerAtomicLong.compareAndSet(old, old + amount);
            if (ret || retryTimes <= 0) {
                break;
            }

            old = this.innerAtomicLong.get();
        }

        return ret;
    }
}
