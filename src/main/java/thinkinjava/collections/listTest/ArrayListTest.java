package thinkinjava.collections.listTest;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public class ArrayListTest<E> extends AbstractList<E>
		implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
    /**
     * 默认初始容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 用于空实例的共享空数组实例。
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 用于默认大小的空实例的共享空数组实例。我们区分这从empty_elementdata知道多少膨胀时，第一个元素添加。
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 数组缓冲区中的数组的元素存储。
     * ArrayList的容量是这个数组缓冲区的长度。任何
     * 空列表elementdata = = defaultcapacity_empty_elementdata
     * 将扩大到default_capacity当第一元素添加。
     */
    transient Object[] elementData; // non-private to simplify nested class access

    private int size;

    public ArrayListTest(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
    
    public ArrayListTest() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
    
    /**
     * 拷贝一份集合数据到elementData
     */
    public ArrayListTest(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }
    
    /**
     * ArrayList每次增长会预申请多一点空间，1.5倍+1，而不是两倍
		这样就会出现当size() = 1000的时候，ArrayList已经申请了1200空间的情况
		trimToSize 的作用只是去掉预留元素位置，就是删除多余的200，改为只申请1000,内存紧张的时候会用到
     */
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
              ? EMPTY_ELEMENTDATA
              : Arrays.copyOf(elementData, size);
        }
    }
    
    /**
     * 我们在使用Arraylist时，经常要对它进行初始化工作，在使用add()方法增加新的元素时，如果要增加的数据量很大，
     * 应该使用ensureCapacity()方法，该方法的作用是预先设置Arraylist的大小，这样可以大大提高初始化速度
     */
    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
            // any size if not default element table
            ? 0
            // larger than default for default empty table. It's already
            // supposed to be at default size.
            : DEFAULT_CAPACITY;

        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }

    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
    
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 增加容量，以确保它至少能容纳
     * 最小容量参数指定的元素数。
     */
    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    

	@Override
	public E get(int index) {
		
		return null;
	}

	public boolean isEmpty() {
        return size == 0;
    }
	
	public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
	
	public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }
	
	public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }
	
	public Object clone() {
        try {
            ArrayListTest<?> v = (ArrayListTest<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

	public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }
	
	//TODO
	@SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }
	
	@SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }
	
}
