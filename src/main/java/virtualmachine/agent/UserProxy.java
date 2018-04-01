package virtualmachine.agent;
/**
 * 其实代理的一般模式就是静态代理的实现模式：首先创建一个接口（JDK代理都是面向接口的），
 * 然后创建具体实现类来实现这个接口，在创建一个代理类同样实现这个接口，不同指出在于，
 * 具体实现类的方法中需要将接口中定义的方法的业务逻辑功能实现，而代理类中的方法只要调用具体类中的对应方法即可，
 * 这样我们在需要使用接口中的某个方法的功能时直接调用代理类的方法即可，将具体的实现类隐藏在底层。
 *
 */
public class UserProxy implements UserService {

	private UserService userService = new UserServiceImpl();
	@Override
	public void eat(String s) {
		System.out.println("静态代理前内容");
		userService.eat(s);
		System.out.println("静态代理后内容");
		
	}

	public static void main(String[] args) {
		UserProxy p = new UserProxy();
		p.eat("apple");
	}
}
