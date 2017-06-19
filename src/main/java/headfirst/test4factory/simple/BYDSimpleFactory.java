package headfirst.test4factory.simple;

public class BYDSimpleFactory {

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
