package headfirst.test2observer;

public class ObserverSubmit implements Observer {

	@Override
	public void updateObserver(Order order) {
		System.out.println("ObserverSubmit---" + "id : " + order.getId() + ",  name : " + order.getName());

	}

}
