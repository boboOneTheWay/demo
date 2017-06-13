package headfirst.test1strategy;

public class QuackSlow implements Quackable {

	@Override
	public void quack() {
		System.out.println("quack slow");
	}

}
