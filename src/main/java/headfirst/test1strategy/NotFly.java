package headfirst.test1strategy;

public class NotFly implements Flyable{

	@Override
	public void fly() {
		System.out.println("can't fly");
	}

}
