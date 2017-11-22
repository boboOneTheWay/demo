package thinkinjava.collections.arraysTest;

import java.util.Arrays;

public class ArrayTest {

	public static void main(String[] args) {
		/**
		 * 复制数据
		 * src:源数组；srcPos:源数组要复制的起始位置；
		 * dest:目的数组；destPos:目的数组放置的起始位置；length:复制的长度
		 */
		int[] i = new int[8];
		int[] j = new int[10];
		Arrays.fill(i, 66);
		Arrays.fill(j, 99);
		System.out.println("i = " + Arrays.toString(i));
		System.out.println("j = " + Arrays.toString(j));
		System.arraycopy(i, 0, j, 0, i.length);
		System.out.println("j = " + Arrays.toString(j));
		
		/**
		 * Arrays提供了重载后的equals()方法，用来比较数组，条件是元素个数必须相等，并且对应位置相等
		 */
		int a1[] = new int[10];
		int a2[] = new int[10];
		Arrays.fill(a1,66);
		Arrays.fill(a2, 66);
		System.out.println(Arrays.equals(a1, a2));
		a2[3] = 88;
		System.out.println(Arrays.equals(a1, a2));
		
	}
}
