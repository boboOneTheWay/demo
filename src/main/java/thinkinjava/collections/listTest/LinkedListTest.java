package thinkinjava.collections.listTest;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedListTest<E> extends AbstractSequentialList<E>
		implements List<E>, Deque<E>, Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	transient int size = 0;

	/**
	 * Pointer to first node. Invariant: (first == null && last == null) ||
	 * (first.prev == null && first.item != null)
	 */
	transient Node<E> first;

	/**
	 * Pointer to last node. Invariant: (first == null && last == null) ||
	 * (last.next == null && last.item != null)
	 */
	transient Node<E> last;

	/**
	 * Constructs an empty list.
	 */
	public LinkedListTest() {
	}

	private static class Node<E> {
		E item;
		Node<E> next;
		Node<E> prev;

		Node(Node<E> prev, E element, Node<E> next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	public LinkedListTest(Collection<? extends E> c) {
		this();
		addAll(c);
	}

	public E getFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return f.item;
	}

	public E getLast() {
		final Node<E> l = last;
		if (l == null)
			throw new NoSuchElementException();
		return l.item;
	}

	public E removeFirst() {
		final Node<E> f = first;
		if (f == null)
			throw new NoSuchElementException();
		return unlinkFirst(f);
	}

	/**
	 * 将下一节点的的值赋给头节点，并返回remove的节点
	 * size减一
	 */
	private E unlinkFirst(Node<E> f) {
		// assert f == first && f != null;
		final E element = f.item;
		final Node<E> next = f.next;
		f.item = null;
		f.next = null; // help GC
		first = next;
		if (next == null)
			last = null;
		else
			next.prev = null;
		size--;
		modCount++;
		return element;
	}

	public E removeLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return unlinkLast(l);
    }

	/**
	 * 将前一个节点的值赋给最后一个节点，并释放内存
	 * size --
	 */
	private E unlinkLast(Node<E> l) {
        // assert l == last && l != null;
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null; // help GC
        last = prev;
        if (prev == null)
            first = null;
        else
            prev.next = null;
        size--;
        modCount++;
        return element;
    }

	public void addFirst(E e) {
        linkFirst(e);
    }

	/**
	 * 
	 * 在链表头添加一个对象，创建一个Node对象，设置前驱节点为空，后继节点为当前链表头元素地址
	 * 将当前链表头前驱节点引用设置为新建的Node对象
	 * size++
	 */
	private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
        modCount++;
    }
	
	public void addLast(E e) {
        linkLast(e);
    }
	
	/**
	 * 在链表尾添加一个节点对象，新建一个节点对象，前驱节点为最后一个节点的引用，后继节点为空
	 * 将给对象赋值给最后一个节点
	 * 如果链表为空，链表头和尾为当前新建的元素
	 * size++
	 * @param e
	 */
	void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }
	
	public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

	public int indexOf(Object o) {
        int index = 0;
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null)
                    return index;
                index++;
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item))
                    return index;
                index++;
            }
        }
        return -1;
    }
	
	public int size() {
        return size;
    }
	
	public boolean add(E e) {
        linkLast(e);
        return true;
    }
	
	public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }
	
	E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        /**
         * 如果前驱节为空说明该节点为头节点，需要将该节点的后继节点设置为首节点
         * 如果前驱节点不为空，将前驱节点的后继继节点设置为要移除节点的后继节点，并将要移除节点的前驱节点设置为空
         * 
         */
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        /**
         * 如果要移除节点的后继节点为空，则证明该节点为尾节点，将该节点的前驱节点设置为尾节点
         * 如果要移除节点的后继节点为空，则将要移除节点的前驱节点设置为该节点后继节点的前驱节点，并将该节点的后继节点设置为空
         */
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        /**
         * 将元素设置为空，help GC
         * size --
         */
        x.item = null;
        size--;
        modCount++;
        return element;
    }
	
	public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }
	
	@Override
	public boolean offerFirst(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offerLast(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E pollFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E pollLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E peekFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E peekLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offer(E e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void push(E e) {
		// TODO Auto-generated method stub

	}

	@Override
	public E pop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<E> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}


}
