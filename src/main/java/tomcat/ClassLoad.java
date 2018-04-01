package tomcat;

public class ClassLoad {
/**
 * Tomcat类加载器
 * 		在tomcat中有三组目录可以存放java类库，另外加上WEB应用自身的目录WEB-INF，一共四组
 * 		／common目录中：类库可以被Tomcat和所有的Web应用程序公共使用
 * 		／server目录中：类库可以被Tomcat使用，对Web程序不可见
 * 		／shared目录中：类库可以被所有对Web应用程序共同使用，对Tomcat自己不可见
 * 		／WebApp／WEB-INF目录中：类库仅仅可以被Web应用程序使用，对Tomcat和其他Web应用程序不可见
 * 
 */
}
