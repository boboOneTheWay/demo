package spring.test1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ApplicationContextTest {

	ApplicationContext applicationContext;
	FileSystemXmlApplicationContext fileSystemXmlApplicationContext;
/**
 * ApplicationContext容器设计原理
 * 		在 FileSystemXmlApplicationContext 设计中，AppplicationContext应用上下文的主要功能已经在 FileSystemXmlApplicationContext
 * 		的基类中AbstractXmlApplicationContext中，作为一个具体的应用上下文，只需要实现和他自身设计相关的两个功能。
 * 		一个功能是，如果直接使用FileSystemXmlApplicationContext ，对于实例化这个应用的支持，同时启动IOC容器的refresh()过程。
 * 		另一个是怎样从文件系统中加载Xml的Bean的定义资源有关，通过这个过程，可以为文件系统中读取以XML形式的BeanDefintion做准备
 * 
 * IOC容器的初始化过程
 * 		IOC容器初始化是由refresh()方法来启动的，这个方法包括BeanDefinition的Resource 定位，载入和注册，spring把这三个过程分来，并使用
 * 			不同的模块来完成，如使用相应的ResourceLoader、BeanDefinitionReader等模块，可以灵活的进行裁剪和扩展，定义出适合自己的IOC容器初始化方式
 * 		1.第一个过程是Resource定位，这个Resource定位指的是BeanDefinition的资源定位，它由ResourceLoader通过统一的Resource接口来完成，这个Resource
 * 			对于各种形式的BeanDefinition 的使用统一提供了接口，这个定位过程类似于容器寻找数据的过程，就像水桶先找到水。
 * 		2.第二个过程是BeanDefinition的载入，这个过程就是将用户定义好的Bean 表示成IOC容器内部数据结构。
 * 		3.第三个过程就是向IOC容器注册这些BeanDefinition的过程，这个过程是调用BeandefinitionRegistry接口完成的，这个过程把载入过程中
 * 			得到的BeanDefinition向IOC容器进行注册，其实就是一个HashMap保存BeanDefinition数据的。
 */
}
