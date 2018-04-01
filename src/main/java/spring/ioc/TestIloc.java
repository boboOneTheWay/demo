package spring.ioc;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.context.ThemeSource;
import org.springframework.web.context.WebApplicationContext;

public class TestIloc {

	HierarchicalBeanFactory h;
	MessageSource qa;
	ResourceLoader a;
	ListableBeanFactory l;
	AutowireCapableBeanFactory au;
	ApplicationEventPublisher ap;
	ApplicationContext ac;
	ConfigurableBeanFactory ab;
	ConfigurableApplicationContext cc;
	WebApplicationContext w;
	ThemeSource t;
	
	XmlBeanFactory x;
}
