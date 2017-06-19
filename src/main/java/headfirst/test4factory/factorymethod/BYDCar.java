package headfirst.test4factory.factorymethod;

public abstract class BYDCar {

	protected String engine;
	
	protected String material;
	
	protected String lights;

	public String getEngine() {
		return engine;
	}

	public abstract void setEngine();

	public String getMaterial() {
		return material;
	}

	public abstract void setMaterial();

	public String getLights() {
		return lights;
	}

	public abstract void setLights();
	
}
