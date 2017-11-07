package virtualmachine;

public class SynchronizedMonitor {
//	Method void onlyMe Foo 
//	0 aload_1//              将对象f入栈
//	1 dup//                  复制栈顶元素（即f的引用）
//	2 astore_2//             将栈顶元素存储到局部标量Slot_2中
//	3 monitorenter//         将栈顶元素即f作为锁，开始同步  
//	4 aload_0//              将局部变量Slot 0（即this指针）的元素入栈    
//	5 invokevirtual#5//      调用doSomething（）的方法    
//	8 aload_2//              将局部变量Slow 2的元素即f入栈   
//	9 monitorexit//          退出同步
//	10 goto 18//             方法正常结束跳转到18返回
//	13 astore_3//            从现在开始是异常路径
//  14 aload_2//             将局部变量Slow 2的元素即f入栈
//	15 monitorexit//         退出同步
//	16 aload_3//             将局部变量即Slow 3即异常对象入栈           
//	17 athrow//              把异常随想重新抛给onlyMe()方法的调用着 
//	18 return//              方法正常返回
//	Exception table
//	FromTo Target Type
//	4 10 13 any
//	13 16 13 any
	
	void nolyMe(Object f) {
		synchronized (f) {
			doSomething();
		}
	}
	
	void doSomething() {};
}
