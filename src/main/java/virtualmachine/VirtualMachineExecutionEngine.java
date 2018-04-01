package virtualmachine;
/**
 * 虚拟机执行引擎
 * @author gaobo
 *
 */
public class VirtualMachineExecutionEngine {
//运行时栈帧结构
	//每一个栈帧都包括局部变量表、操作数栈、动态链接、返回地址和一些额外的附加信息，是支持虚拟机方法调用个方法执行的数据结构与
	//栈帧中需要多大的局部变量表，多深的操作数栈都已经完全确定了，并且写入到方发表中code属性中，因此一个栈帧需要分配多少内存不会受到程序运行期变量的数据影响，仅取决于虚拟机的实现
	//一个线程的方法调用链可能很长，只有位于栈顶的栈帧是有效的
	
		//1.局部变量表：用于存储方法参数和方法内部定义的局部变量。
				//局部变量的容量是以变量曹solt为最小单位。虚拟机没有指定一个solt的大小，一个solt可以存放一个基本类型（32位）（外加对象引用）或者更小，是线程私有的数据，不会出现同步问题
				//为了节省栈帧的空间，局部变量表中的solt是可以复用的，方法体中定义的变量起作用域不一定会覆盖整个方法体，如果当前字节码的PC计数器已经超过了某个变量的作用域，那么这个solt可以被复用。
				//solt的复用会导致，如果一个变量占用的Solt没有被其他复用，GC Roots一部分局部变量表仍然保持着对它的关联，这种关联没有被即使打断，如果后面遇到一个耗时很长的方法，GC不会回收该内存，需要手动赋值为null
		//2.操作数栈
				//操作数栈可以称为操作栈，是一个先进后出的栈，方法刚开始的时候栈是空的，在方法执行的过程中，会有各种字节码指令往操作数栈中写入和提取（出栈和入栈）内容。
				//在概念模型里两个栈帧为虚拟机的元素，是完全相互独立的，但是在大多数虚拟机的实现都会做一些优化处理，让两个栈帧出现一部分重叠，部分操作数栈与局部变量表重叠，
					//这样在方法调用的时候就可以共用一部分数据，无需进行参数传递复制
		//3.动态链接
				//每个栈帧都包含一个指向运行时常量池中该栈帧所属方法都引用，持有这个引用是为了支持方法调用过程中都动态链接
				//Class文件都常量池由大量的符号引用这些符号一部分会在类加载阶段或者第一次使用的时候直接引用,这种转化称为静态解析，另一部分将在每一次运行期间转化为直接引用，称为动态链接
		//4.方法放回地址
				//程序正常或者异常退出时，都要返回到方法被调用的位置。将当前栈出栈，恢复上层方法的局部变量表和操作数栈，若有返回值压入调用者栈的操作数栈中，调整pc计数器多少值以指向方法调用指令后面的以一条指令。
	
	/**
	 * 方法调用：
	 * 		方法调用并不等同于方法执行，方法调用唯一的任务就是确定被调用方法的版本，即调用哪一个方法，暂时不涉及内部具体的运行过程。Class文件的编译过程中不包含传统编译中时内存布局中的入口地址
	 * 			一切方法带调用在Class文件里存储的只是符号引用，而不是方法在实际运行时内存布局的入口地址（相当于直接引用），需要在类加载的时候或者是运行期才能确定目标方法的直接引用。
	 * 
	 * 		解析：
	 * 			所有方法调用中的目标方法在Class中都是一个常量池中的符号引用，在类加载阶段，会将一部分符号引用转化为直接引用。这种解析能能成立的前提是：方法在程序真正的运行之前就有一个可确定的调用版本，
	 * 				并且这个版本在运行期是不可改变的，这类方法称为解析。其中包括：
	 * 				1.调用静态方法
	 * 				2.调用实例构造<init>方法，私有方法，父类方法
	 * 				3.调用所有的虚方法
	 * 				4.调用所有的接口方法，会在运行时再确定一个实现此接口的对象
	 * 				5.先在运行时动态解析出调用点限定符所引用的方法，然后再执行该方法
	 * 			其中前几条分派逻辑固化在java虚拟机内部，最后一条是分派逻辑是由用户所设定的引导方法决定的
	 * 
	 * 	分派：
	 * 		静态分派：
	 * 			编译器在重载时是通过参数的静态类型而不是实际类型作为判定的依据。并且静态类型在编译期可知，因此，编译阶段，Javac编译器会根据参数的静态类型决定使用哪个重载版本。
	 * 			Human称为变量的静态类型，或者叫做外观类型
				所有依赖静态类型来定位方法执行版本的分派动作称为静态分派。静态分派的典型应用就是方法重载。
				静态分派发生在编译阶段，因此确定静态分派的动作实际上不是由虚拟机来执行的，而是由编译器来完成。
				但是，字面量没有显示的静态类型，它的静态类型只能通过语言上的规则去理解和推断。

	 * 
	 */
	
	public static abstract class Human{
	}
	public static class Man extends Human{
	}
	public static class WoMan extends Human{
	}
	
	public void sayHello(Human guy) {
		System.out.println("Human");
	}
	public void sayHello(Man guy) {
		System.out.println("Man");
	}
	public void sayHello(WoMan guy) {
		System.out.println("WoMan");
	}
	public static void main(String[] args) {
		VirtualMachineExecutionEngine v = new VirtualMachineExecutionEngine();
		Human man = new Man();
		Human woMan = new WoMan();
		Man man1 = new Man();
		WoMan woMan1 = new WoMan();
		v.sayHello(man);
		v.sayHello(woMan);
		v.sayHello(man1);
		v.sayHello(woMan1);
		v.sayHello((Man)man);
		v.sayHello((WoMan)woMan1);
	}
	
	/**
	 * 动态分派：
	 * 		1、找到操作数栈顶的第一个元素所指向的对象的实际类型，记作C。
			2、如果在类型C中找到与常量中的描述符和简单名称相符合的方法，然后进行访问权限验证，如果验证通过则返回这个方法的直接引用，
				查找过程结束；如果验证不通过，则抛出java.lang.IllegalAccessError异常。
			3、否则未找到，就按照继承关系从下往上依次对类型C的各个父类进行第2步的搜索和验证过程。
			4、如果始终没有找到合适的方法，则跑出java.lang.AbstractMethodError异常。
				由于invokevirtual指令执行的第一步就是在运行期确定接收者的实际类型，所以两次调用中的invokevirtual指令把常量池中的类方法符号引用解析到了不同的直接引用上，
				这个过程就是Java语言方法重写的本质。我们把这种在运行期根据实际类型确定方法执行版本的分派过程称为动态分派
		
			由于动态分派是非常频繁的动作，而且动态分派的方法版本选择过程需要运行时在类的方法元数据中搜索合适的目标方法，因此虚拟机的实际实现中基于性能的考虑，
			大部分实现都不会真正的进行如此频繁的搜索。面对这种情况，最常用的”稳定优化“手段就是为类在方法区中建立一个虚方法表（Virtual Method Table，也称为vtable），
				使用虚方法表索引来代替元数据查找以提高性能。
			虚方法表中存放着各个方法的实际入口地址。如果某个方法在子类中没有被重写，那子类的虚方法表里面的地址入口和父类相同方法的地址入口是一致的，
				都是指向父类的实际入口。如果子类中重写了这个方法，子类方法表中的地址将会替换为指向子类实际版本的入口地址。
				为了程序实现上的方便，具有相同签名的方法，在父类、子类的虚方法表中具有一样的索引序号，这样当类型变换时，仅仅需要变更查找的方法表，就可以从不同的虚方法表中按索引转换出所需要的入口地址。
				方法表一般在类加载阶段的连接阶段进行初始化，准备了类的变量初始值后，虚拟机会把该类的方法表也初始化完毕。
	 */
	
}
