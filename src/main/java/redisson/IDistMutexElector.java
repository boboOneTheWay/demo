package redisson;

public interface IDistMutexElector {
    void becomeActiveOrDie(Class var1) throws InterruptedException;
}
