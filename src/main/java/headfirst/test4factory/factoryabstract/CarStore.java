package headfirst.test4factory.factoryabstract;

public class CarStore {
	BYDFactoryAbstract bYDFactory = new BYDFactoryS();
	public BYDCar sale () {
		BYDCar car = bYDFactory.getCar("qin");
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
