package headfirst.test1strategy;

public class RedHeadDuck extends Duck{

	public RedHeadDuck(){
		quackable = new QuackSlow();
		flyable = new FlyWithRocket();
	}
	
	@Override
	public void disPlay() {
		System.out.println("this duck head is red");
	}

}
