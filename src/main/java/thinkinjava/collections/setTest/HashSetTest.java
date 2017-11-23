package thinkinjava.collections.setTest;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class HashSetTest<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private transient HashMap<E, Object> map;

	// 与后台映射中的对象关联的虚拟值
	private static final Object PRESENT = new Object();

	public HashSetTest() {
		map = new HashMap<>();
	}

	/**
	 * 构建包含指定集合中元素的新集合。HashMap是默认加载因子产生（0.75）和一个初始容量足以包含在指定集合的元素。
	 */
	public HashSetTest(Collection<? extends E> c) {
		map = new HashMap<>(Math.max((int) (c.size() / .75f) + 1, 16));
		addAll(c);
	}

	/**
	 * 构建了一个新的，空的设置；支持HashMap实例 指定的初始容量和指定的负载因子。
	 */
	public HashSetTest(int initialCapacity, float loadFactor) {
		map = new HashMap<>(initialCapacity, loadFactor);
	}

	public HashSetTest(int initialCapacity) {
		map = new HashMap<>(initialCapacity);
	}

	/**
	 * 构建一个新的空链接哈希集。（
	 * 这个包私有构造函数是唯一使用的linkedhashset。）支持HashMap实例是一个具有指定的初始LinkedHashMap 容量和规定的负荷系数
	 */
	HashSetTest(int initialCapacity, float loadFactor, boolean dummy) {
		map = new LinkedHashMap<>(initialCapacity, loadFactor);
	}

	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	public boolean add(E e) {
		return map.put(e, PRESENT) == null;
	}

	public boolean remove(Object o) {
		return map.remove(o) == PRESENT;
	}
	
	public void clear() {
        map.clear();
    }

}
