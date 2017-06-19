package headfirst.test2observer;

public class TestObserver {

	public static void main(String[] args) {
		InformationDelivery informationDelivery = new InformationDelivery();
		informationDelivery.registObserver(new ObserverHand());
		informationDelivery.registObserver(new ObserverSubmit());
		Order order = new Order();
		order.setId(123456L);
		order.setName("zhangsan");
		order.setOrderId("7988797");
		informationDelivery.setData(order);
	}
}
