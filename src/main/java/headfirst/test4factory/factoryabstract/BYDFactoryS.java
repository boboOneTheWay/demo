package headfirst.test4factory.factoryabstract;

public class BYDFactoryS extends BYDFactoryAbstract{

	
	@Override
	public BYDCar getCar(String car) {
		BYDFactoryParts bYDFactoryParts = new BYDFactoryParts();
		if(car.equals("qin")) {
			return new BYDqinS(bYDFactoryParts);
		} else if (car.equals("tang")){
			return new BYDtangS(bYDFactoryParts);
		} else {
			return null;
		}
	}
}
