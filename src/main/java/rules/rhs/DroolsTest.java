package rules.rhs;

import org.kie.api.runtime.KieSession;

import com.util.DroolsUtil;

public class DroolsTest {

	public static void main(String[] args) {
		KieSession kieSession = DroolsUtil.getKieSession("src/main/resources/rules/rhs.drl");
		
		Customer c =  new Customer();
		c.setAge(18);
		c.setName("gb");
		c.setCity("lz");
		
		kieSession.insert(c);
		kieSession.fireAllRules();
		kieSession.dispose();
	}
}
