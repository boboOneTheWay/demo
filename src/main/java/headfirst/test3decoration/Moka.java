package headfirst.test3decoration;

public class Moka implements Coffee {
	
	private String description = "moka coffee";

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Double cost() {
		return 1.98;
	}

}
