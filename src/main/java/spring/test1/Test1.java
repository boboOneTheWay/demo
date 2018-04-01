package spring.test1;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.beans.factory.support.FactoryBeanRegistrySupport;
import org.springframework.context.ApplicationContext;
import org.springframework.core.AliasRegistry;
import org.springframework.core.SimpleAliasRegistry;

public class Test1 {
/**
 * 一、spring 整体架构
	1.core container
		core container（核心容器）包含有core、beans、context、expression Language模块
			(1).core模块主要包含spring框架基本核心工具类。spring的其他组件都要使用这个包里的类，core模块是其他组件的基本核心
		 	(2).beans模块是所有应用都会用到的，包含访问配置文件、创建和管理beans以及进行IOC和DI操作的相关所有类。
			(3).context模块构建于core和beans之上，提供了一些类似于JNDI注册器的框架对象访问方法，context模块集成beans的特性，为spring核心提供了大量的扩展，添加了对国际化（如资源绑定）、
				事件传播、资源加载和对context的透明创建的支持。context模块同时也支持j2ee的一些特性。ApplicationContext的接口是context模块的关键。
			(4).Expression Language模块提供了一个强大的表达式用于在运行时查询和操作对象
	2.Data Access／Integration
		Data Access／Integration层包含有Jdbc、ORM、OXM、JMS和Transaction模块
			(1).Jdbc模块包含一个jDBC的抽象层，它可以消除亢长的Jdbc编码和解析数据库厂商的所有错误代码。这个模块包含了spring对jdbc数据访问进行封装的所有类。
			(2).ORM模块为流行的对象-关系映射ApI，如JPA、Jdo、Hibernate、iBatis等，提供了一个交互层，利用ORM封装包，可以混合所有使用spring的特性进行O/R映射。
			(3).OXM模块提供了一个对Object／Xml映射实现的抽象层，Object／XML映射实现包括JAXB、castor、XMLBeans、JiBX和XStream。
			(4).Jms（java Messaging service）模块包括了一些和消费的特性。
			(5).Transaction模块支持编程和申明式事务管理，这类事物必须实现特定接口，并且对所有POJO都适用。
	3.WEB
		WEB上下文模块建立在应用程序上下文模块之上为基于WEB的应用程序提供上下文
			(1).web模块：提供了基础面向web的集成特性，例如多文件上传／使用servlet listeners初始化
 */
	
/********************************************************************** 二、容器的基本实现 **************************************************************************************************/
/**
 * 	1.核心类介绍 DefaultListableBeanFactory
 * 		DefaultListableBeanFactory：XmlBeanFactory继承自DefaultListableBeanFactory，而DefaultListableBeanFactory是整个bean加载的核心部分，是spring注册加载类的默认实现，XmlBeanFactory
 * 			与DefaultListableBeanFactory不同的是XmlBeanFactory使用了自定义的xml读取器xmlBeanDefintionReader，实现了个性化的BeanDefinition读取，DefaultListableBeanFactory继承自
 * 			AbstractAutowireCapableBeanFactory并实现了ConfigurableListableBeanFactory以及BeanDefinitionRegistry接口。
 */
	
	AliasRegistry aliasRegistry;
	SimpleAliasRegistry simapleAliasRegistry;
	SingletonBeanRegistry singletonBeanRegistry;
	BeanFactory beanFactory;
	DefaultSingletonBeanRegistry defaultSingletonBeanRegistry;
	HierarchicalBeanFactory hierarchicalBeanFactory;
	BeanDefinitionRegistry beanDefintionRegistry;
	FactoryBeanRegistrySupport factoryBeanRegistrySupport;
	
	ConfigurableBeanFactory configurableBeanFactory;
	ListableBeanFactory listableBeanFactory;
	AbstractBeanFactory abstractBeanFactory;
	AutowireCapableBeanFactory autowireCapableBeanFactory;
	AbstractAutowireCapableBeanFactory abtractAutowireCapableBeanFactory;
	ConfigurableListableBeanFactory configurableListableBeanFactory;
	DefaultListableBeanFactory defaultListableBeanFactory;
/**
 *  2.每个类作用介绍
 *  		AliasRegistry:定义对alias的简单增删改操作。
 *  		SimpleAliasRegistry ：主要使用map作为alias的缓存，并对接口AliasRegistry实现。
 *  		SingletonBeanRegistry：定义对单列对注册和获取。
 *  		BeanFactory：定义获取bean以及bean的各种属性。
 *  		DefaultSingletonBeanRegistry：对接口SingletonBeanRegistry的各函数的实现。
 *  		HierarchicalBeanFactory：继承BeanFactory，在其定义的功能基础上增加了对parentFactory对支持。
 *  		BeanDefinitionRegistry：定义对BeanDefinition的各种增删改操作。
 *  		FactoryBeanRegistrySupport：在DefaultSingletonBeanRegistry基础上增加了对FactoryBean对特殊处理功能。
 *  
 *  3.容器加载相关类
 *  		ConfigurableBeanFactory：提供配置Factory对各种方法。
 *  		ListableBeanFactory：提供各种条件获取Bean的配置清单。
 *  		AbstractBeanFactory：综合 FactoryBeanRegistrySupport 和 ConfigurableBeanFactory 的功能。
 *  		AutowireCapableBeanFactory：提供创建bean、自动注入、初始化以及应用bean后的处理器。
 *  		AbstractAutowireCapableBeanFactory：综合AbstractBeanFactory 并对接口 AutowireCapableBeanFactory 实现。
 *  		ConfigurableListableBeanFactory ：BeanFactory的配置清单，指定忽略类型以及接口等。
 *  		DefaultListableBeanFactory:综合上面所有功能，主要是对Bean注册后的处理。
 *  
 */
	
	FactoryBean<?> factoryBean;
/**
 * 	4.FactoryBean
 *		实现 FactoryBean 的类表明此类也是一个Bean，类型为工厂Bean（Spring中共有两种bean，一种为普通bean，另一种则为工厂bean）。顾名思义，它也是用来管理Bean的，而它本身由spring管理。
 *		一个Bean想要实现 FactoryBean ，必须实现以下三个接口：
 *			1. Object getObject():返回由FactoryBean创建的Bean的实例
 *			2. boolean isSingleton():确定由FactoryBean创建的Bean的作用域是singleton还是prototype；
 *			3. getObjectType():返回FactoryBean创建的Bean的类型。
 *		有一点需要注意，如果将一个实现了FactoryBean的类成功配置到了spring上下文中，那么通过该类对象的名称（比如appleFactoryBean）从spring的applicationContext或者beanFactory获取bean时，
 *			获取到的是appleFactoryBean创建的apple实例，而不是appleFactoryBean自己，如果想通过spring拿到appleFactoryBean，需要在名称前加 & 符号
 * 5.Aware
 * 		Spring提供了一些以Aware结尾的接口，实现了Aware接口的bean在被初始化后，可以获取相应的资源
 *		通过Aware接口，可以对Spring相应资源进行操作
 *		为Spring进行简单的扩展提供了方便的入口
 *		常见的Aware
 *			ApplicationContextAware：向实现了这个接口的bean提供ApplicationContext（IOC容器的上下文信息），实现了这个接口的bean必须配置到Spring的bean配置文件中去，并且由Spring的bean容器去加载。
 * 			BeanNameAware：提供一个关于BeanName的定义的内容。
 * 			ApplicationEventPublisherAware：事件的发布
 *			BeanClassLoaderAware：找到相关的类加载器
 *
 * 6.InitializingBean
 * 		当Spring中的bean实现了这个接口，那么在bean实例化之前会调用这个接口对应的方法（测试的时候发现，构造方法永远是第一个执行的，所以针对这种说法我个人不是很赞同，应该是实例化的过程中）。

 * 7.BeanPostProcessor接口：这个接口是单独成类的，它的作用范围是Spring容器，一旦在容器中配置了这个类，那么该容器中所有bean在初始化的前后都会调用这个接口对应的方法，
 * 		具体的配置如： <bean class="com.spring.test.BeanPostProcessor_Imp"></bean>
 *		构造方法，InitializingBean和BeanPostProcessor的执行顺序：构造方法-->BeanPostProcessor-->InitializingBean-->bean中的初始化方法-->BeanPostProcessor。
 *		实例化、依赖注入完毕，在调用显示的初始化之前完成一些定制的初始化任务  
 *   		Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;  
 *  	 	实例化、依赖注入、初始化完毕时执行  
 *    		Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
 *  8.DisposableBean：销毁Bean
 *   
 *  9.BeanFactory
 * 		BeanFactory接口是Spring bean容器的根接口.提供获取bean,是否包含bean,是否单例与原型,获取bean类型,bean 别名的方法 
		BeanFactory的三个子接口： 
			HierarchicalBeanFactory：提供父容器的访问功能 
			ListableBeanFactory：提供了批量获取Bean的方法 
			AutowireCapableBeanFactory：在BeanFactory基础上实现对已存在实例的管理 
 * 		FACTORY_BEAN_PREFIX = “&”：&前缀的字符串可以用来引用一个FactoryBean实例，或把它和工厂产生的Bean区分开，就是说，如果一个FactoryBean的名字为a，那么，&a会得到那个FactoryBean
 * 		getBean(String name)： 根据名称获取Bean 
 *		getBean(String name, Class< T> requiredType)： 根据类型获取Bean 
 *		getBean(String name, Object… args)：获取Bean 
 *		getBean(Class< T> requiredType, Object… args)：获取Bean 
 *		isSingleton(String name)：：是否为单实例 
 *		isPrototype(String name)：：是否为原型（多实例） 
 *		isTypeMatch(String name, ResolvableType typeToMatch)：名称、类型是否匹配 
 *		isTypeMatch(String name, Class< ?> typeToMatch)：：名称、类型是否匹配 
 *		Class< ? > getType(String name) ：根据实例的名字 获取类型 
 *		String[] getAliases(String name)： 根据实例的名字获取实例的别名数组
 *
 *
 *  10.ListableBeanFactory
 *  		BeanDefinition相关
 *  			boolean containsBeanDefinition(String beanName); 
 *  			int getBeanDefinitionCount(); 
 *  			String[] getBeanDefinitionNames(); 
 *
 *  		跟据bean 的类型获取bean .它不会检查嵌套的FactoryBean创建的bean 
 *  			String[] getBeanNamesForType(ResolvableType type); 
 *  			String[] getBeanNamesForType(Class<?> type); 
 *  			String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit); 
 *  			<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException; 
 *  			<T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException; 

 *  		根据注解查找相关BeanName数组
 *  			String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType); 
 *  		根据注解查找相关Bean的Map
 *  			Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException; 
 *  		查找一个Bean上的注解
 *  			<A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws NoSuchBeanDefinitionException;
 *  
 *  11.AutowireCapableBeanFactory
 *  		这个接口定义了将容器中的Bean按某种规则（如按名字，类型的匹配等）进行自动装配的方法。在我们开发的应用中很少用到，像ApplicationContext这样的外观实现类不会实现这个接口。
 *  			它更多的作用是和其它组件结合，比如Struts2等，把不在spring容器中的bean加入到spring的生命周期中
 *  
 *  12.HierarchicalBeanFactory
 *  		获取父容器 bean factory
 *　　　		BeanFactory getParentBeanFactory();
 *　		判断当前容器是否保护bean
 *　　　		boolean containsLocalBean(String name);
 *
 *	13.SingletonBeanRegistry
 *		主要为共享实例提供注册机制的接口，后期主要用在beanfactory的单例对象中。
 *  
 *  14.ConfigurableBeanFactory
 *  		ConfigurableBeanFactory同时继承了HierarchicalBeanFactory 和 SingletonBeanRegistry 这两个接口，即同时继承了分层和单例类注册的功能
 *  	 	常亮-单例
 *   		String SCOPE_SINGLETON = "singleton"; 
 *      常量-原型
 *   		String SCOPE_PROTOTYPE = "prototype";
 *   	设置父BeanFactory
 *   		void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException; 
 *   	以下四个：设置和获取BeanClassLoader
 *   		void setBeanClassLoader(ClassLoader beanClassLoader); 
 *   		ClassLoader getBeanClassLoader(); 
 *   		void setTempClassLoader(ClassLoader tempClassLoader); 
 *   		ClassLoader getTempClassLoader();
 *
 *   	是否需要缓存bean metadata,比如bean difinition 和 解析好的classes.默认开启缓存
 *   		void setCacheBeanMetadata(boolean cacheBeanMetadata); 
 *   		boolean isCacheBeanMetadata();
 *
 *     	定义用于解析bean definition的表达式解析器
 *  			void setBeanExpressionResolver(BeanExpressionResolver resolver); 
 *   		BeanExpressionResolver getBeanExpressionResolver();
 *
 *   	类型转化器
 *   		void setConversionService(ConversionService conversionService); 
 *   		ConversionService getConversionService();
 *
 *   	属性编辑器
 *   		void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);
 *			BeanFactory用来转换bean属性值或者参数值的自定义转换器
 *   		void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass); 
 *   		void copyRegisteredEditorsTo(PropertyEditorRegistry registry);
 *
 *    	类型转换器
 *   		void setTypeConverter(TypeConverter typeConverter); 
 *   		TypeConverter getTypeConverter(); 
 *   		void addEmbeddedValueResolver(StringValueResolver valueResolver); 
 *   		String resolveEmbeddedValue(String value);
 *
 *   	Bean处理器
 *   		void addBeanPostProcessor(BeanPostProcessor beanPostProcessor); 
 *   		int getBeanPostProcessorCount();
 *
 *   	作用域定义
 *   		void registerScope(String scopeName, Scope scope); 
 *   		String[] getRegisteredScopeNames(); 
 *   		Scope getRegisteredScope(String scopeName);
 *
 *   	访问权限控制
 *   		AccessControlContext getAccessControlContext();
 *
 *   	合并其他ConfigurableBeanFactory的配置,包括上面说到的BeanPostProcessor,作用域等
 *   		void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);
 *
 *   	bean定义处理
 *   		void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException; 
 *   		void resolveAliases(StringValueResolver valueResolver); 
 *   		BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException; 
 *   		boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;
 *   	bean创建状态控制.在解决循环依赖时有使用
 *   		void setCurrentlyInCreation(String beanName, boolean inCreation); 
 *   		boolean isCurrentlyInCreation(String beanName);
 *
 *   	处理bean依赖问题
 *   		void registerDependentBean(String beanName, String dependentBeanName); 
 *   		String[] getDependentBeans(String beanName); 
 *   		String[] getDependenciesForBean(String beanName);
 *
 *   	bean生命周期管理-- 销毁bean
 *   		void destroyBean(String beanName, Object beanInstance); 
 *   		void destroyScopedBean(String beanName); 
 *   		void destroySingletons();
 *   
 *   15.ConfigurableListableBeanFactory
 *   	设置忽略的依赖关系,注册找到的特殊依赖
    			void ignoreDependencyType(Class<?> type); 
    			void ignoreDependencyInterface(Class<?> ifc); 
    			void registerResolvableDependency(Class<?> dependencyType, Object autowiredValue); 
    			boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor) throws NoSuchBeanDefinitionException;

    		获取bean定义 (可以访问属性值跟构造方法的参数值)
    			BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    		迭代BeanNames
    			Iterator<String> getBeanNamesIterator();

    		清除元数据缓存
    			void clearMetadataCache();

    		锁定配置信息.在调用refresh时会使用到.
    			void freezeConfiguration(); 
    			boolean isConfigurationFrozen();

    		预加载不是懒加载的单例.用于解决循环依赖问题
    			void preInstantiateSingletons() throws BeansException;
    			
    	16.AbstractBeanFactory
    		这个类主要提供了获取Bean的相关方法，实现了BeanFactory的方法，主要实现了getBean方法
    		实现了HierarchicalBeanFactory的方法
		实现了ConfigurableBeanFactory
		留给子类实现的方法
			protected abstract boolean containsBeanDefinition(String beanName);
			protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
			protected abstract Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeanCreationException;
	

	17.AbstractAutowireCapableBeanFactory
	
	整体这个类做的事情有：提供bean创建功能（通过构造器）、属性渲染，装配（包括自动装配）、初始化。处理运行时的bean引用，解析已经管理的结果、调用初始化方法。
		并且提供了自动装配的功能。
	子类需要实现的方法主要是resolveDependency(DependencyDescriptor, String, Set, TypeConverter), 此方法用于实现按类型匹配的自动装配，
		依赖解析成bean。此类没有实现bean definition 注册功能，需要子类来完成。
	此类的构造方法会导致BeanNameAware、BeanFactoryAware、BeanClassLoaderAware类型的依赖无法被解析（调用方法this.ignoredDependencyInterfaces.add(typeToIgnore);）

   private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
		CglibSubclassingInstantiationStrategy是默认bean创建策略，CglibSubclassingInstantiationStrategy需要一个cglib包，
			如果没有也可以正常运行，因为，只有当方法注入需要生成子类时，才需要cglib。
   private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
   		默认的参数名字解析策略，asm字节码技术。

	方法作用大概分为几类：   
		1.实例化非预定义的bean（或者渲染存在的bean）
		2.bean生命周期的精细控制。
		3.父类AbstractBeanFactory模版方法的实现。
		4.其他方法会调用的一些支持方法，大部分通过protected暴露给子类了，（留下扩展空间）

	实例化非预定义的bean（或者渲染存在的bean）－－这类bean都是prototype
		createBean(Class<T> beanClass) 具体创建过程参考doCreateBean(beanName, mbd, args)。这里面功能比较完成，除了实例化，
			还包括其他的spring定义的bean初始化生命周期
		autowireBean(Object existingBean) 自动装配实例bean，具体过程参考populateBean(bd.getBeanClass().getName(), bd, bw);
		configureBean(Object existingBean, String beanName)自动配置，与上个方法相比，多了自动调用
			initializeBean(beanName, exposedObject，mbd)BeanPostProcessor，InitializingBean，xxxAware的过程。
	上面这些方法从整体来看，实际上都是获取到class信息，然后创建一个对应的RootBeanDefinition，
		BeanWrapper逐步调用populateBean(beanName, mbd, instanceWrapper)和initializeBean(beanName, exposedObject, mbd)

	bean生命周期的精细控制－－这里创建的实例也为prototype
		createBean(Class beanClass, int autowireMode, boolean dependencyCheck)
		autowire(Class beanClass, int autowireMode, boolean dependencyCheck)
		autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
		applyBeanPropertyValues(Object existingBean, String beanName)
		initializeBean(Object existingBean, String beanName)
		applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
		applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
	从方法来看普通bean在初始化时，包含创建、自动装配、属性设置、应用beanPostProcessor接口，initializeBean和后面两个方法功能上有点重复，
		因为最initializeBean的调用会导致BeanPostProcessor接口的两个方法被调用。这里没有太多的功能,主要都是调用别人的方法，具体实现细节都在最后一部分

	父类 AbstractBeanFactory 模版方法的实现。
		a.createBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)
			创建bean在[实例化非预定义的bean]时调用过。InstantiationAwareBeanPostProcessor这个接口是有机会返回实例，如果返回了实例，那么不在调用后面的doCreateBean，据说是为了
			b.postProcessBeforeInstantiation(Class<?> beanClass, String beanName) 能够返回代理类。
		c.doCreateBean(final String beanName, final RootBeanDefinition mbd, final Object[] args)看到do前缀就懂了，肯定是给上个方法打工的
		d.predictBeanType(String beanName, RootBeanDefinition mbd, Class... typesToMatch) 
		e.getTypeForFactoryMethod(String beanName, RootBeanDefinition mbd, Class[] typesToMatch)
		f.getTypeForFactoryBean(String beanName, RootBeanDefinition mbd)
		g.getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) 
		
 */
	
	ApplicationContext applicationContext;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
