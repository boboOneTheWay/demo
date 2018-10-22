package redisson;

public interface IDistAtomicLong {
    long getAndDecrement();

    long addAndGet(long var1);

    boolean compareAndSet(long var1, long var3);

    long decrementAndGet();

    long get();

    long getAndAdd(long var1);

    long getAndSet(long var1);

    long incrementAndGet();

    long getAndIncrement();

    void set(long var1);

    boolean decrementWithBottom(int var1, long var2);

    boolean addWithUpper(long var1, long var3);
}
