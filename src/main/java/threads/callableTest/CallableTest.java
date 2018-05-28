package threads.callableTest;

import java.util.Random;
import java.util.concurrent.*;

public class CallableTest {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Future<Boolean> future = threadPool.submit(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Thread.sleep(5000);// 可能做一些事情
                return (new Random().nextInt(100) == 100);
            }
        });

        Future<Integer> future1 = threadPool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                Thread.sleep(5000);// 可能做一些事情
                return new Random().nextInt(100);
            }
        });

        Future<Boolean> future2 = threadPool.submit(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Thread.sleep(5000);// 可能做一些事情
                return (new Random().nextInt(100) == 100);
            }
        });

        try {

            System.out.println(future.get());
            System.out.println(future1.get());
            System.out.println(future2.get());

            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
