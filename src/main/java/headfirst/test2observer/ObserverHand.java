package headfirst.test2observer;

public class ObserverHand implements Observer {

	@Override
	public void updateObserver(Order order) {
		System.out.println("ObserverHand---" + "id : " + order.getId() + ",  name : " + order.getName());
	}
}
