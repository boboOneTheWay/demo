package thinkinjava.genericity.genericityInterface;
/**
 * 生成器
 * @author gaobo
 *
 * @param <T>
 * 一种专门负责创建对象的类，实际上这是工厂方法模式的一种应用，不过当生成器创建新对象时不需要任何参数
 * 无需额外信息就知道如何创建新对象
 */
public interface Generator<T> {

	T next();
}
