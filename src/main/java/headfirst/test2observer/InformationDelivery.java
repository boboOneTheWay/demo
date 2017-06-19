package headfirst.test2observer;

import java.util.ArrayList;
import java.util.List;

public class InformationDelivery implements Subject{

	private List<Observer> list;
	
	private Order order;
	
	public InformationDelivery(){
		list = new ArrayList<Observer>();
	}

	@Override
	public void registObserver(Observer observer) {
		list.add(observer);
		System.out.println("已注册类型 : " + observer.getClass().getName());
	}

	@Override
	public void notifyObserver() {
		for(Observer observer : list) {
			observer.updateObserver(order);
		}
	}

	@Override
	public void removeOberver(Observer observer) {
		int i = list.indexOf(observer);
		if(i >= 0) {
			list.remove(i);
		}
	}
	
	public void setData(Order order) {
		this.order = order;
		notifyObserver();
	}

}
