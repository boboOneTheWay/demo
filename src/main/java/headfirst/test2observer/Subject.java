package headfirst.test2observer;

public interface Subject {

	void registObserver(Observer observer);
	
	void notifyObserver();
	
	void removeOberver(Observer observer);
}
