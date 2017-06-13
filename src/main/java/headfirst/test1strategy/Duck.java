package headfirst.test1strategy;

public abstract class Duck {

	protected Flyable flyable;
	
	protected Quackable quackable;
	
	public abstract void disPlay();
	
	public void swimming(){
		System.out.println("duck can swimming");
	}
	
	public void sleep() {
		System.out.println("duck can sleep");
	}
	
	public String play() {
		return "play with duck";
	}

	public Flyable getFlyable() {
		return flyable;
	}

	public void setFlyable(Flyable flyable) {
		this.flyable = flyable;
	}

	public Quackable getQuackable() {
		return quackable;
	}

	public void setQuackable(Quackable quackable) {
		this.quackable = quackable;
	}
	
	public void fly() {
		flyable.fly();
	}
	
	public void quack() {
		quackable.quack();
	}
}
