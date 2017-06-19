package headfirst.test4factory.factoryabstract;

public class BYDFactoryPartsS implements PartsFactory {

	@Override
	public String setMaterial() {
		return "铝合金材料";

	}

	@Override
	public String setLights() {
		return "水晶大灯";

	}

	@Override
	public String setEngine() {
		return "涡轮喷气式发动机";

	}

}
