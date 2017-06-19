package headfirst.test4factory.factorymethod;

public class BYDFactoryS extends BYDFactoryAbstract{

	@Override
	public BYDCar getCar(String car) {
		if(car.equals("qin")) {
			return new BYDqinS();
		} else if (car.equals("tang")){
			return new BYDtangS();
		} else {
			return null;
		}
	}
}
