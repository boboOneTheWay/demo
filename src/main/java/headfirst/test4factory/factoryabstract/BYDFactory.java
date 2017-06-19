package headfirst.test4factory.factoryabstract;

public class BYDFactory extends BYDFactoryAbstract{

	@Override
	public BYDCar getCar(String car) {
		BYDFactoryPartsS bYDFactoryParts = new BYDFactoryPartsS();
		if(car.equals("qin")) {
			return new BYDqin(bYDFactoryParts);
		} else if (car.equals("tang")){
			return new BYDtang(bYDFactoryParts);
		} else {
			return null;
		}
	}
}
