package thinkinjava.genericity.genericityInterface;

import java.util.Iterator;
import java.util.Random;

public class CoffeeGenerator implements Generator<Coffee> ,Iterable<Coffee>{
	@SuppressWarnings("rawtypes")
	private Class[] types = {Latte.class, Mocha.class, Cappuccino.class};
	private static Random rand = new Random();
	public CoffeeGenerator() {
		
	}
	private int size = 0;
	public CoffeeGenerator(int size) {
		this.size = size;
	}
	
	class CoffeeIterator implements Iterator<Coffee>{
		int count = size;
		@Override
		public boolean hasNext() {
			return count > 0;
		}

		@Override
		public Coffee next() {
			count --;
			return CoffeeGenerator.this.next();
		}
	}

	@Override
	public Iterator<Coffee> iterator() {
		return new CoffeeIterator();
	}

	@Override
	public Coffee next() {
		try {
			return (Coffee)
					types[rand.nextInt(types.length)].newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		CoffeeGenerator gen = new CoffeeGenerator(6);
		Iterator<Coffee> iterator = gen.iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		for(int i = 0; i<5; i++) {
			System.out.println(gen.next());
		}

		CoffeeGenerator coffeeGenerator = new CoffeeGenerator(6);
		for(Coffee e : coffeeGenerator) {
			System.out.println(e);
		}
		
		
	}

}
