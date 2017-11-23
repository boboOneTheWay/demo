package thinkinjava.collections.setTest;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

public interface SetTest<E> extends Collection<E> {

	int size();
	
	boolean isEmpty();
	
	boolean contains(Object o);
	
	Iterator<E> iterator();
	
	Object[] toArray();
	
	<T> T[] toArray(T[] a);
	
	boolean add(E e);
	
	boolean remove(Object o);
	
	boolean containsAll(Collection<?> c);
	
	boolean addAll(Collection<? extends E> c);
	
	boolean retainAll(Collection<?> c);
	
	boolean removeAll(Collection<?> c);
	
	void clear();
	
	boolean equals(Object o);
	
	int hashCode();
	
	@Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT);
    }
}
