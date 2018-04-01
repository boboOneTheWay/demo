package virtualmachine.agent.dynamic;

public class UserServiceImpl implements UserService {

	@Override
	public void eat(String s) {
		System.out.println("eat " + s);
	}
}
