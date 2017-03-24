package rules.demo;


import java.math.BigDecimal;
import java.util.Date;

import org.kie.api.runtime.KieSession;

import com.util.DroolsUtil;



/**
 *  客户电话/地址不能为空
	账户号必须是唯一的
	账户拥有人不能为空
	银行账户余额至少大于100，否则显示一个警告消息
	只有客户年龄小于27岁，才能开一个学生账户
 */
public class DroolsTest {

    public static final void main(String[] args) {
        try {
        	KieSession kSession = DroolsUtil.getKieSession("src/main/resources/rules/validation.drl");
            
            
            Customer customer_addressRequired = new Customer();
            customer_addressRequired.setDateOfBirth(new Date());
            customer_addressRequired.setFirstName("x");
            customer_addressRequired.setLastName("j");
            customer_addressRequired.setPhoneNumber("123456");
            kSession.insert(customer_addressRequired);
            
            
            //phoneNumberRequired
            Customer customer_phoneNumberRequired = new Customer();
            customer_phoneNumberRequired.setAddress("shenzhen");
            customer_phoneNumberRequired.setDateOfBirth(new Date());
            customer_phoneNumberRequired.setFirstName("x");
            customer_phoneNumberRequired.setLastName("j");
            customer_phoneNumberRequired.setPhoneNumber(null);
            kSession.insert(customer_phoneNumberRequired);
            
            //accountOwnerRequired
            Account account_accountOwnerRequired = new Account();
            account_accountOwnerRequired.setBalance(new BigDecimal(200));
            account_accountOwnerRequired.setOwner(null);
            account_accountOwnerRequired.setUuid("abc");
            kSession.insert(account_accountOwnerRequired);
            
            //accountBalanceAtLeast
            Account account_accountBalanceAtLeast = new Account();
            account_accountBalanceAtLeast.setBalance(new BigDecimal(20));
            account_accountBalanceAtLeast.setOwner(new Customer());
            account_accountBalanceAtLeast.setUuid("abc");
            kSession.insert(account_accountBalanceAtLeast);
            
            //accountNumberUnique
            Account account_accountNumberUnique = new Account();
            account_accountNumberUnique.setBalance(new BigDecimal(200));
            account_accountNumberUnique.setOwner(new Customer());
            account_accountNumberUnique.setUuid("1");
            kSession.insert(account_accountNumberUnique);
            
            kSession.fireAllRules();
            kSession.dispose();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
