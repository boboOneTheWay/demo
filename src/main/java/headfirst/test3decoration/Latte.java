package headfirst.test3decoration;

public class Latte implements Coffee {

	private String discription = "拿铁咖啡";
	
	@Override
	public String getDescription() {
		return discription;
	}

	@Override
	public Double cost() {
		return 3.05;
	}

}
