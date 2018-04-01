package spring.test1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class AOPTest {
	

	Advice advice;
	BeforeAdvice beforeAdvice;
	MethodBeforeAdvice methodBeforeAdvice;
/**
 * Advice通知：
 * 		Advice定义在连接点做什么，为切面增强提供织入接口，在spring aop中它主要描述aop围绕方法调用而注入的切面行为。advice是aop联盟定义的
 * 			一个接口
 * MethodBeforeAdvice:
 * 		void before(Method method, Object[] args, Object target) throws Throwable;
 * 		作为回调函数，会在调用目标方法时被回调，具体的参数有Method，这个方法是目标函数的反射后的对象，Object对象组是目标方法的参数
 * 
 */

	Pointcut pointcut;
	MethodMatcher methodMatcher;
/**
 * Pointcut(切点)
 * 		Pointcut决定Advice通知应该作用于哪个连接点，也就是通过Pointcut来定义需要增强的方法集合，这些方法的选取应该按照一定的谷子饿来完成。
 * 		对于Point的匹配判断功能，具体由返回的 MethodMatcher 来完成，也就是说这个MethodMatcher来判断是否由是否要对当前放法调用进行增强，或者是否需要
 * 			对当前调用方法应用已经配置好的advice通知。
 */
	
	Advisor advisor;
	DefaultPointcutAdvisor defaultPointcutAdvisor;
/**
 * advisor通知器：
 * 		完成对切面方法的增强设计(advice)和关注点设计(pointcut)以后，需要一个对象结合起来，完成他们的就是advisor增强器。这个结合使用IOC容器配置AOP应用
 * 		
 */
	
	Proxy proxy;
	InvocationHandler a;
/**
 * 动态代理
 * 		JDK动态代理：
 * 			在java的reflection包中有proxy，这个对象生成后，所起的作用就像Proxy模式中的proxy对象，在使用时还需要对代理对象设计一个回调方法，这个回调方法
 * 				起到的作用是，在其中加入了作为代理需要额外处理的动作
 * 			InvocationHandler
 * 			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
 * 				这个接口方法中第一个参数是代理对象的实例，第二个参数是方法对象，第三个是方法参数，使用时通过调用 Proxy.newInstance()方法生成具体的Proxy
 * 					对象时，把 InvocationHandler 设置到参数里来，剩下的就由java虚拟机来完成。
 * 		CGLIB代理
 * 
 */
	ProxyFactoryBean pp;
	AopProxy aa;
	ReflectiveMethodInvocation aaa;
/**
 * ProxyFactoryBean
 * 		在Aop模块中，一个主要的部分是代理对象的生成，对于Spring应用，可以看到，是通过配置和调用ProxyFactoryBean来完成这个任务的,其中
 * 			封装了两种代理对象生成的过程 JdK 和 CGLIB
 * 		ProxyFactoryBean 的配置和使用
 * 			1.定义通知器Advisor，实现了需要对目标对象进行增强的切面行为
 * 			2.定义ProxyFactoryBean，把它作为另一bean来定义，需要设定proxyInterface代理接口、通知器、需要增强的对象
 * 
 * Aop 拦截器链调用
 * 		JDK 和 CGLIB 会生成不同的AopProxy代理对象，从而构造了不同的回调方法来启动的对拦截器的调用，他们对拦截器的调用都是在 ReflectiveMethodInvocation中的proceed
 * 			方法实现的。在proceed 方法中会逐个拦截器方法。在运行拦截器之前，需要对代理方法完成一个匹配判断，通过这个匹配判断来决定拦截器是否满足切面的要求。在ponitcut切点
 * 			中需要进行matches 的匹配过程，即是否需要进行通知增强。在proceed 方法中，先进行判断，如果已经运行到拦截器尾部，那么就直接调用目标对象的方法，都则沿着拦截器链
 * 			继续进行，得到下一个拦截器，通过这个拦截器进行matches判断，如果适用于横切增强场合，从拦截器中得到通知器，并启动通知器的invoke方法进行横切面增强。迭代调用直至拦截器链调用完毕
 * 
 */
}
