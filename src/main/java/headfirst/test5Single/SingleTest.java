package headfirst.test5Single;

public class SingleTest {

	private static volatile SingleTest single;
	
	private SingleTest() {
		
	}

	public  static SingleTest getSingleTest() {
		if(single == null) {
			synchronized (SingleTest.class) {
				if(single == null) {
					single = new SingleTest();
				}
			}
		}
		return single;
	}
}
