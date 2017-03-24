/**
 * 
 */
package rules.demo;

import java.util.Date;

import org.kie.api.runtime.rule.RuleContext;



public class ValidationHelper {


  public static void error(RuleContext kcontext,
      Object context, String msg) {
	  System.out.println("Error 级别:" + msg);
  }
  
  public static void warning(RuleContext kcontext,
      Object  context, String msg) {
	  System.out.println("警告级别:" + msg);
  }

  public static int yearsPassedSince(Date date) {
	  return 2;
  }

}
