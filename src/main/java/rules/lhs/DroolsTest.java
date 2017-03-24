package rules.lhs;

import org.kie.api.runtime.KieSession;

import com.util.DroolsUtil;
/**
 * 
 * @author gaobo
 *
 */
public class DroolsTest {

	public static void main(String[] args) {
		KieSession kieSession = DroolsUtil.getKieSession("src/main/resources/rules/lhs.drl");
		//测试一
//		Customer c = new Customer();
//		c.setCity("bj");
//		c.setAge(10);
//		c.setName("gb");
//		kieSession.insert(c);
//		
		//测试二 and or
//		Customer c = new Customer();
//		c.setName("gaobo");
//		c.setAge(18);
//		Account a = new Account();
//		a.setName("bj");
//		kieSession.insert(a);
//		kieSession.insert(c);
		
		//测试三  exists 存在
//		Customer c = new Customer();
//		c.setAge(18);
//		kieSession.insert(c);
		
		//测试四  not 不存在
//		Customer c = new Customer();
//		c.setName("gaobo1");
//		kieSession.insert(c);
		
		//测试五  from
//		Account a1 = new Account();
//		Account a2 = new Account();
//		Account a3 = new Account();
//		a1.setName("a1");
//		a2.setName("a2");
//		a3.setName("a3");
//		Customer c = new Customer();
//		c.getAccounts().add(a1);
//		c.getAccounts().add(a3);
//		kieSession.insert(c);
		
		
		//测试六  collect
//		Account accout1 = new Account();
//        Account accout2 = new Account();
//        Account accout3 = new Account();
//        Account accout4 = new Account();
//        accout1.setStatus("Y");
//        accout2.setStatus("Y");
//        accout3.setStatus("Y");
//        accout4.setStatus("N");
//        kieSession.insert(accout1);
//        kieSession.insert(accout2);
//        kieSession.insert(accout3);
//        kieSession.insert(accout4);
        
        //测试七 accumulate
//        Account accout1 = new Account();
//        Account accout2 = new Account();
//        Account accout3 = new Account();
//        Account accout4 = new Account();
//        accout1.setNum(100);
//        accout2.setNum(100);
//        accout3.setNum(100);
//        accout4.setNum(100);
//        kieSession.insert(accout1);
//        kieSession.insert(accout2);
//        kieSession.insert(accout3);
//        kieSession.insert(accout4);
		
		
		//测试八 && ||
//		Customer c = new Customer();
//		c.setAge(18);
//		c.setCity("bj");
//		c.setName("gaobo1");
//		Account a = new Account();
//		a.setName("a1");
//		a.setNum(100);
//		kieSession.insert(c);
//		kieSession.insert(a);
		
		
		//测试九 contains 前面的集合是否包含后面的对象
//		Customer c = new Customer();
//		c.setAge(18);
//		c.setName("gb");
//		Account a = new Account();
//		a.setName("a1");
//		c.getAccounts().add(a);
//		kieSession.insert(c);
//		kieSession.insert(a);

		
		//测试十  memberof  对象是否属于集合
//		Account a1 = new Account();
//		Account a2 = new Account();
//		Account a3 = new Account();
//		Account a4 = new Account();
//		a1.setName("a1");
//		a2.setName("a2");
//		a3.setName("a3");
//		a4.setName("a4");
//		Customer c = new Customer();
//		c.getAccounts().add(a1);
//		c.getAccounts().add(a2);
//		c.getAccounts().add(a3);
//		kieSession.insert(a1);
//      kieSession.insert(a2);
//      kieSession.insert(a3);
//      kieSession.insert(a4);
//      kieSession.insert(c);
		
		//测试十一  比较操作符matches
//		Customer c = new Customer();
//		c.setName("zhangjiqiang");
//		kieSession.insert(c);
		
		
		//测试十二  eval 对比
		Customer c = new Customer();
		c.setName("zhangjiqiang");
		kieSession.insert(c);
		
		kieSession.fireAllRules();
		kieSession.dispose();
	}
}
