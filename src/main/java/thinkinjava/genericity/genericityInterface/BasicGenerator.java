package thinkinjava.genericity.genericityInterface;
/**
 * 
 * @author gaobo
 *
 * @param <T>
 * 
 * 通用的Generator
 * 该类提供了一个基本实现，用来生成某个类的对象，这个类需要具备某个特点：
 * 		1.需要该类有默认的构造参数
 * 		2.该类必须为public
 */
public class BasicGenerator<T> implements Generator<T> {

	private Class<T> type;

	public BasicGenerator(Class<T> type) {
		this.type = type;
	}

	@Override
	public T next() {

		try {
			return type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> Generator<T> create(Class<T> type) {
		return new BasicGenerator<T>(type);
	}
	
	public static void main(String[] args) {
		Generator<Coffee> gen = BasicGenerator.create(Coffee.class);
		for(int i=0; i<5; i++) {
			System.out.println(gen.next());
		}
	}

}
