package threads.copyOnWriteArrayList;

public class CopyOnWriteArrayListTest<E> {
	
//	public boolean add(E e) {
//        final ReentrantLock lock = this.lock;
//        lock.lock();
//        try {
//            Object[] elements = getArray();
//            int len = elements.length;
//            Object[] newElements = Arrays.copyOf(elements, len + 1);
//            newElements[len] = e;
//            setArray(newElements);
//            return true;
//        } finally {
//            lock.unlock();
//        }
//    }
}
