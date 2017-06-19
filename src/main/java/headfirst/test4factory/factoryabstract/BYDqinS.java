 package headfirst.test4factory.factoryabstract;

public class BYDqinS extends BYDCar {

	
	private PartsFactory partsFactory;
	
	public BYDqinS(PartsFactory partsFactory) {
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
