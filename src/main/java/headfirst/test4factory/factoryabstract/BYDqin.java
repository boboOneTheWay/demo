package headfirst.test4factory.factoryabstract;

public class BYDqin extends BYDCar {
	
	private PartsFactory partsFactory;
	
	public BYDqin(PartsFactory partsFactory) {
		this.partsFactory = partsFactory;
	}

	@Override
	public void setEngine() {
		this.engine = partsFactory.setEngine();

	}

	@Override
	public void setMaterial() {
		this.material = partsFactory.setMaterial();

	}

	@Override
	public void setLights() {
		this.lights = partsFactory.setLights();

	}

}
