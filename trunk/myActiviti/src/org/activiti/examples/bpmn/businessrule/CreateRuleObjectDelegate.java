package org.activiti.examples.bpmn.businessrule;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import demo.Order;


public class CreateRuleObjectDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
//    Order order = new Order();
//    order.setType("Type1");
//    System.out.println("Setting ruleInput " + order);
//    execution.setVariable("order", order);
	  System.out.println("Run CreateRuleObjectDelegate");
  }

}