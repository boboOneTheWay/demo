package thinkinjava.collections.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillingLists {

	public static void main(String[] args) {
		List<StringAddress> list = new ArrayList<>(Collections.nCopies(6, new StringAddress("hello")));
		for (StringAddress stringAddress : list) {
			System.out.println(stringAddress);
		}
		list.add(new StringAddress("hello1"));
		for (StringAddress stringAddress : list) {
			System.out.println(stringAddress);
		}
		Collections.fill(list, new StringAddress("world"));
		for (StringAddress stringAddress : list) {
			System.out.println(stringAddress);
		}
	}
}
