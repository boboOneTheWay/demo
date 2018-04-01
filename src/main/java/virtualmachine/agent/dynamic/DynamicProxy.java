package virtualmachine.agent.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler{
	
	private Object object;//用于接收具体的实例对象
	
	public DynamicProxy(Object object) {
		this.object = object;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("前置内容");
		method.invoke(object, args);
		System.out.println("后置内容");
		return null;
	}

	public static void main(String[] args) {
		UserService userService = new UserServiceImpl();
		InvocationHandler h = new DynamicProxy(userService);
		UserService userServiceProxy = (UserService)Proxy.newProxyInstance(UserService.class.getClassLoader(), new Class[] {UserService.class}, h);
		userServiceProxy.eat("apple");
	}
}
