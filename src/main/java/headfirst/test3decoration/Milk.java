package headfirst.test3decoration;

public class Milk implements Coffee {

	private Coffee coffee;
	
	private String description = "加牛奶";
	
	public Milk(Coffee coffee) {
		this.coffee = coffee;
	}
	@Override
	public String getDescription() {
		return coffee.getDescription() + "**" + description;
	}

	@Override
	public Double cost() {
		return coffee.cost() + 100;
	}

}
