package headfirst.test3decoration;

public class TestMain {

	public static void main(String[] args) {
		Coffee coffee = new Moka();
		coffee = new Sugar(coffee);
		coffee = new Milk(new Sugar(coffee));
		System.out.println("描述 ：" + coffee.getDescription() + "--价格 ：" + coffee.cost());
	}
}
