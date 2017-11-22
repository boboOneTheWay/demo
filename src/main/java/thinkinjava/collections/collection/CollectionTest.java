package thinkinjava.collections.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class CollectionTest {
	/**
	 * Collection:一个独立的元素序列，这些元素都服从一个或多个规则(List/Set/Queue)
	 * Collection接口包含了序列的概念，一种存放一组对象的方式
	 * 
	 * Map:一组成对的键值对
	 */

	public static void main(String[] args) {
		Collection<Integer> c = new ArrayList<Integer>();
		for (int i = 1; i <= 10; i++) {
			c.add(i);
		}

		Integer[] a = { 1, 2, 3, 4, 5, 6 };
		Collections.addAll(c, 9, 8, 7, 6, 5);
		Collections.addAll(c, a);

		for (Integer i : c) {
			System.out.println(i);
		}

		/**
		 * Arrays.asList 定长，不能add或remove，只能set修改，ArrayList是Arrays的静态内部类
		 * 
		 */
		List<Integer> asList = Arrays.asList(16, 17, 18, 19, 20);
		asList.set(1, 99);
		for (Integer integer : asList) {
			System.out.println(integer);
		}

		/**
		 * 看一下JDK中的集合类，比如List一族或者Set一族， 都是继承了Iterable接口，但并不直接继承Iterator接口。
		 * 仔细想一下这么做是有道理的。因为Iterator接口的核心方法next()或者hasNext() 是依赖于迭代器的当前迭代位置的。
		 * 如果Collection直接继承Iterator接口，势必导致集合对象中包含当前迭代位置的数据(指针)。
		 * 当集合在不同方法间被传递时，由于当前迭代位置不可预置，那么next()方法的结果会变成不可预知。
		 * 除非再为Iterator接口添加一个reset()方法，用来重置当前迭代位置。 但即时这样，Collection也只能同时存在一个当前迭代位置。
		 * 而Iterable则不然，每次调用都会返回一个从头开始计数的迭代器。 多个迭代器是互不干扰的。
		 * 
		 * Iterable是一个接口，Collection接口时implements Iterable接口，Iterator是Iterable的一个成员变量。
		 */
		List<Integer> it = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			it.add(i);
		}
		Iterator<Integer> iterator = it.iterator();
		while (iterator.hasNext()) {
			Integer next = iterator.next();
			System.out.println(next);
		}
		/**
		 * 一．相同点 都是迭代器，当需要对集合中元素进行遍历不需要干涉其遍历过程时，这两种迭代器都可以使用。 二．不同点
		 * 1.使用范围不同，Iterator可以应用于所有的集合，Set、List和Map和这些集合的子类型。而ListIterator只能用于List及其子类型。
		 * 2.ListIterator有add方法，可以向List中添加对象，而Iterator不能。
		 * 3.ListIterator和Iterator都有hasNext()和next()方法，可以实现顺序向后遍历，但是ListIterator有hasPrevious()和previous()方法，可以实现逆向（顺序向前）遍历。Iterator不可以。
		 * 4.ListIterator可以定位当前索引的位置，nextIndex()和previousIndex()可以实现。Iterator没有此功能。
		 * 5.都可实现删除操作，但是ListIterator可以实现对象的修改，set()方法可以实现。Iterator仅能遍历，不能修改。
		 */
		System.out.println("*****************listIterator***************");
		List<Integer> itList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			itList.add(i);
		}
		ListIterator<Integer> listIterator = itList.listIterator(3);
		while (listIterator.hasNext()) {
			System.out.println(listIterator.next());
			listIterator.set(12);
			listIterator.add(15);
			//
			// while(listIterator.hasPrevious()) {
			// System.out.println(listIterator.previous());
			// }
		}

		for (Integer i : itList) {
			System.out.println("原始list：" + i);
		}

		/**
		 * 队列Queue是一个接口，LinkedList实现了Deque，而Deque implements
		 * Queue，所以LinkedList可以做为Queue的一种实现 offer：将一个元素查到队尾，或者返回false
		 * peek和element在不移除情况情况下返回队头，peek在队列返回空时返回null，而element会报错 poll：返回队头的值并移除
		 * add／remove不建议使用
		 */
		System.out.println("*********************Queue***********************");
		Queue<Integer> q = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			q.offer(i);
		}

		while (q.peek() != null) {
			Integer peek = q.peek();
			System.out.println("队列头的值：" + peek);
			Integer poll = q.poll();
			System.out.println("队列移除的值：" + poll);
		}

		if (q.peek() == null) {
			System.out.println("队列已清空");
		}

		/**
		 * Deque可以双向操作队列
		 */
		System.out.println("*********************Deque***********************");
		Deque<Integer> q1 = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			q1.offerFirst(i);
			q1.offerLast(10 - i);
		}
		while (q1.peek() != null) {
			Integer peek = q1.peek();
			System.out.println("队列头的值：" + peek);
			Integer peekLat = q1.peekLast();
			System.out.println("队列尾的值：" + peekLat);
			Integer poll = q1.pollFirst();
			System.out.println("队列头移除的值：" + poll);
			Integer pollLast = q1.pollLast();
			System.out.println("队列尾移除的值：" + pollLast);
		}
		if (q1.peek() == null) {
			System.out.println("队列已清空");
		}
		if (q1.peekLast() == null) {
			System.out.println("队列尾已清空");
		}

		/**
		 * 带有优先级的队列
		 * PriorityQueue是非线程安全的，所以Java提供了PriorityBlockingQueue（实现BlockingQueue接口）用于Java多线程环境。
		 */
		System.out.println("*********************priorityQueue***********************");
		Random random = new Random();
		Comparator<Integer> orderDesc = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				//大于 返回1 升序
				if (o1 >= o2) {
					return 1;
				}
				return -1;
			}
		};
		Queue<Integer> priorityQueue = new PriorityQueue<Integer>(orderDesc);
		for(int i = 0; i<= 10; i++) {
			priorityQueue.offer(random.nextInt(100));
		}
		while (priorityQueue.peek() != null) {
			Integer peek = priorityQueue.peek();
			System.out.println("队列头的值：" + peek);
			Integer poll = priorityQueue.poll();
			System.out.println("队列移除的值：" + poll);
		}
	}
	
//	1、    sort(Collection)方法的使用(含义：对集合进行排序)。
//    例：对已知集合c进行排序？

//         public class Practice {
//                public static void main(String[] args){
//                             List c = new ArrayList();
//                             c.add("l");
//                             c.add("o");
//                             c.add("v");
//                             c.add("e");
//                             System.out.println(c);
//                             Collections.sort(c);
//                             System.out.println(c);
//                }
//         }

//     运行结果为：[l, o, v, e]
//               [e, l, o, v]
	
	
//	2、   shuffle(Collection)方法的使用(含义：对集合进行随机排序)。
//    例：shuffle(Collection)的简单示例？
//
//         public class Practice {
//                   public static void main(String[] args){
//                           List c = new ArrayList();
//                           c.add("l");
//                           c.add("o");
//                           c.add("v");
//                           c.add("e");
//                           System.out.println(c);
//                           Collections.shuffle(c);
//                           System.out.println(c);
//                           Collections.shuffle(c);
//                           System.out.println(c);
//                      }
//           }
//
//          运行结果为：[l, o, v, e]
//                    [l, v, e, o]
//                    [o, v, e, l]
	
	
//	3、 binarySearch(Collection,Object)方法的使用(含义：查找指定集合中的元素，返回所查找元素的索引)。
//    例:binarySearch(Collection,Object)的简单示例？
//
//         public class Practice {
//                  public static void main(String[] args){
//                          List c = new ArrayList();
//                          c.add("l");
//                          c.add("o");
//                          c.add("v");
//                          c.add("e");
//                          System.out.println(c);
//                          int m = Collections.binarySearch(c, "o");
//                          System.out.println(m);
//                         
//                    }
//          }
//
//    运行结果为：[l, o, v, e]
	
//	4、  replaceAll(List list,Object old,Object new)
//    方法的使用(含义：替换批定元素为某元素,若要替换的值存在刚返回true,反之返回false)。
//    例：
//      public class Practice {
//                  public static void main(String[] args){
//                         List list = Arrays.asList("one two three four five six siven".split(" "));
//                         System.out.println(list);
//                         List subList = Arrays.asList("three four five six".split(" "));
//                         System.out.println(Collections.replaceAll(list, "siven", "siven eight"));
//                         System.out.println(list);
//                   }
//         }
//
//   运行结果为：
//               [one, two, three, four, five, six, siven]
//               true
//               [one, two, three, four, five, six, siven eight]
	
	
//	5、   reverse()方法的使用(含义：反转集合中元素的顺序)。
//    例：
//       public class Practice {
//           public static void main(String[] args){
//                  List list = Arrays.asList("one two three four five six siven".split(" "));
//                  System.out.println(list);
//                  Collections.reverse(list);
//                  System.out.println(list);
//            }
//        }
//
// 运行结果为：
//           [one, two, three, four, five, six, siven]
//           [siven, six, five, four, three, two, one]
	
//	6、    rotate(List list,int m)方法的使用(含义：集合中的元素向后移m个位置，在后面被遮盖的元素循环到前面来)。
//    例：
//       public class Practice {
//                public static void main(String[] args){
//                         List list = Arrays.asList("one two three four five six siven".split(" "));
//                         System.out.println(list);
//                         Collections.rotate(list, 1);
//                         System.out.println(list);
//                   }
//         }
//
// 运行结果为：
//           [one, two, three, four, five, six, siven]
//           [siven, one, two, three, four, five, six]
	
	
//	7、    copy(List m,List n)方法的使用(含义：将集合n中的元素全部复制到m中,并且覆盖相应索引的元素)。
//    例：
//         public class Practice {
//                 public static void main(String[] args){
//                         List m = Arrays.asList("one two three four five six siven".split(" "));
//                         System.out.println(m);
//                         List n = Arrays.asList("我 是 复制过来的哈".split(" "));
//                         System.out.println(n);
//                         Collections.copy(m,n);
//                         System.out.println(m);
//                   }
//          }
//
//运行结果为：[one, two, three, four, five, six, siven]
//          [我, 是, 复制过来的哈]
//          [我, 是, 复制过来的哈, four, five, six, siven]
	
	
//	8、     swap(List list,int i,int j)方法的使用(含义：交换集合中指定元素索引的位置)。
//    例：
//        public class Practice {
//                  public static void main(String[] args){
//                         List m = Arrays.asList("one two three four five six siven".split(" "));
//                         System.out.println(m);
//                         Collections.swap(m, 2, 3);
//                         System.out.println(m);
//                    }
//         }
//
//运行结果为：
//       [one, two, three, four, five, six, siven]
//       [one, two, four, three, five, six, siven]
	
//	9、 fill(List list,Object o)方法的使用(含义：用对象o替换集合list中的所有元素)。
//    例：
//       public class Practice {
//                 public static void main(String[] args){
//                            List m = Arrays.asList("one two three four five six siven".split(" "));
//                         System.out.println(m);
//                          Collections.fill(m, "啊啊啊");
//                         System.out.println(m);
//                  }
//       }
//
//     运行结果为：
//              [one, two, three, four, five, six, siven]
//              [啊啊啊, 啊啊啊, 啊啊啊,啊啊啊, 啊啊啊, 啊啊啊, 啊啊啊]
	
//	10、 nCopies(int n,Object o)方法的使用(含义：返回大小为n的List，List不可改变,其中的所有引用都指向o)。
//    例：
//      public class Practice {
//               public static void main(String[] args){
//                         System.out.println(Collections.nCopies(5, "嘿嘿"));
//                  }
//       }
//
//   运行结果为：
//       　　  [嘿嘿, 嘿嘿, 嘿嘿, 嘿嘿, 嘿嘿]

}
