package thinkinjava.collections.setTest;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;

public interface SortedSetTest<E> extends Set<E> {
	
	/**
	 * 返回用于排序该集合中元素的比较器
	 */
	Comparator<? super E> comparator();
	
	SortedSet<E> subSet(E fromElement, E toElement);
	
	SortedSet<E> headSet(E toElement);
	
	SortedSet<E> tailSet(E fromElement);
	
	E first();
	
	E last();
	
}
