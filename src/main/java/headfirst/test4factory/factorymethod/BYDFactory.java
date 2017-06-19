package headfirst.test4factory.factorymethod;

public class BYDFactory extends BYDFactoryAbstract{

	@Override
	public BYDCar getCar(String car) {
		if(car.equals("qin")) {
			return new BYDqin();
		} else if (car.equals("tang")){
			return new BYDtang();
		} else {
			return null;
		}
	}
}
