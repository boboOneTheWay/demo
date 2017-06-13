package headfirst.test1strategy;

public class TestMian {

	public static void main(String[] args) {
		RedHeadDuck duck = new RedHeadDuck();
		duck.setFlyable(new FlyHigh());
		duck.setQuackable(new QuackLoud());
		duck.fly();
		duck.quack();
	}
}
