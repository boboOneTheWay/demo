package headfirst.test3decoration;

public class Sugar implements Coffee {

	private Coffee coffee;
	
	private String description = "加糖";
	
	public Sugar(Coffee coffee) {
		this.coffee = coffee;
	}
	
	@Override
	public String getDescription() {
		return coffee.getDescription() + "**" + description;
	}

	@Override
	public Double cost() {
		// TODO Auto-generated method stub
		return coffee.cost() +100;
	}

}
