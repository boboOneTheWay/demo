package thinkinjava.genericity.genericityMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenericMethods {

	public <T> void f(T x) {
		System.out.println(x.getClass().getName());
	}
	
	public static <K,V> Map<K,V> map() {
		return new HashMap<K,V>();
	}
	
	public static <T> List<T> arraylist() {
		return new ArrayList<T>();
	}
	
	public static <T> List<T> linkedList() {
		return new LinkedList<T>();
	}
	
	/**
	 * 可变参数范型
	 * @param args
	 * @return
	 */
	public static <T> List<T> makeList(@SuppressWarnings("unchecked") T...args) {
		List<T> result = new ArrayList<>();
		for(T item : args) {
			result.add(item);
		}
		return result;
	}
	
	public static void main(String[] args) {
		Map<String, List<String>> sls = GenericMethods.map();
		sls.put("r", new ArrayList<>());
		
		GenericMethods gm = new GenericMethods();
		gm.f("");
		gm.f(1);
		gm.f(gm);
	}
}
