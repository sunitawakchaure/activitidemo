package org.activiti.examples.bpmn.gateway;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;


public class HandleTimeOutObjectDelegate implements JavaDelegate {

  public void execute(DelegateExecution execution) throws Exception {
	  System.out.println("Handle Event after time out");
  }

}