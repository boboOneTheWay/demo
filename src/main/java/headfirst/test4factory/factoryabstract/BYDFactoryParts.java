package headfirst.test4factory.factoryabstract;

public class BYDFactoryParts implements PartsFactory {

	@Override
	public String setMaterial() {
		return "航空材料";

	}

	@Override
	public String setLights() {
		return "黄金琉璃大灯";

	}

	@Override
	public String setEngine() {
		return "神州十号液氢发动机";

	}

}
