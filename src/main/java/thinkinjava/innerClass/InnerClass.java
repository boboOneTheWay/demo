package thinkinjava.innerClass;

/**
 * 内部类
 * 
 * @author gaobo
 *
 */
public class InnerClass {
	/**
	 * 1 内部类自动拥有其外围类所有成员的访问权，当某个外围类创建了一个内部类对象时，此内部类会秘密的捕捉一个指向外围类对象的引用(非static的内部类)
	 * .this:当内部类生成对外部类对对象引用使用 .this ,在编译器被知晓并受到检查，因此没有任何运行开销。 .new:创建内部类对引用时使用.new
	 */
	public class Inner {
		public void f() {
			System.out.println("it is inner class");
		}

		public InnerClass outer() {
			return InnerClass.this;
		}
	}

	/**
	 * 2 局部内部类：在方法的作用域内创建一个完整类，这被称为局部内部类
	 */
	public Destination destination() {
		class Pdestination implements Destination {
			private String s;

			private Pdestination(String s) {
				this.s = s;
			}

			@Override
			public void a() {
				System.out.println(s);
			}

		}
		return new Pdestination("ddd");
	}

	/**
	 * 3 匿名内部类：
	 * 
	 */

	public Contents contents(String j) {
		return new Contents() {
			{
				System.out.println("匿名内部类构造器");
			}
			private int i = 666;

			@Override
			public void f() {
				System.out.println(i);
				System.out.println(j);

			}
		};
	}
	
	/**4 静态内部类(嵌套类)
	 * 如果不需要内部类对象与其外围类之间有联系，可以将内部类申明为static
	 * 		要创建静态内部类对象不需要其外围的对象
	 * 		不能从静态内部类访问非静态外围类的对象。
	 */
	
	public static class Prace {
		void f() {
			System.out.println("静态内部类");
		}
	}

	public static void main(String[] args) {
		// 1
		InnerClass in = new InnerClass();
		InnerClass.Inner inner = in.new Inner();
		inner.f();
		System.out.println(inner.outer());

		// 2
		Destination destination = in.destination();
		destination.a();

		// 3
		Contents contents = in.contents("aaa");
		contents.f();
		
		//4
		Prace p = new Prace();
		p.f();
	}
}
