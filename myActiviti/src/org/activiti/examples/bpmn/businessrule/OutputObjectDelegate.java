package org.activiti.examples.bpmn.businessrule;

import java.util.Collection;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import demo.Order;


public class OutputObjectDelegate implements JavaDelegate {

  @SuppressWarnings("unchecked")
  public void execute(DelegateExecution execution) throws Exception {
	System.out.println("test output");
    Collection<Object> outputList = (Collection<Object>) execution.getVariable("ruleOutput");
    System.out.println("outputList " + outputList);
    if(outputList == null) return;
    for (Object object : outputList) {
    	System.out.println("object " + object);
    	if(object instanceof Order) {
    		execution.setVariable("ruleresult", object);
    	}
	}
  }
}