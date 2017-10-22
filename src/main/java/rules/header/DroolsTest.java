package rules.header;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import com.util.DroolsUtil;

public class DroolsTest {

	public static void main(String[] args) {
		KieSession kieSession = DroolsUtil.getKieSession("src/main/resources/rules/other.drl");
		
//		//用于设置返回值
        //List list = new ArrayList();
       // kieSession.setGlobal("list", list);
//               
//        //设置服务
//        GlobalService service = new GlobalService();
//        kieSession.setGlobal("service", service);
//		
//        kieSession.fireAllRules();
//        list = (List)kieSession.getGlobal("list");
//        System.out.println("全局结果：" + list);
       
//		  Customer c = new Customer();
//          c.setName("gb");
//          kieSession.insert(c);
//          
          Customer c1 = new Customer();
          c1.setName("zjq");
          kieSession.insert(c1);
        QueryResults results = kieSession.getQueryResults("queryName2" , "zjq");
        for(QueryResultsRow qr : results) {
        	Customer msg = (Customer)qr.get("customer");
        	System.out.println("queryDavid = " + msg.getName());
        }
		kieSession.fireAllRules();
		kieSession.dispose();
	}
}
