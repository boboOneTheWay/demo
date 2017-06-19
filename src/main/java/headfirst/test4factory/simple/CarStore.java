package headfirst.test4factory.simple;

public class CarStore {
	BYDSimpleFactory bYDSimpleFactory = new BYDSimpleFactory();
	public BYDCar sale () {
		BYDCar car = bYDSimpleFactory.getCar("qin");
		car.setEngine();
		car.setLights();
		car.setMaterial();
		return car;
	}
	
	public static void main(String[] args) {
		CarStore carStore = new CarStore();
		BYDCar car = carStore.sale();
		System.out.println("发动机: " + car.getEngine() + ",大灯：" + car.getLights() + ",材质：" + car.getMaterial());
	}
}
