package org.nanquan.activiti.examples.bus;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class EventLogService implements JavaDelegate {

	  public void execute(DelegateExecution execution) throws Exception {
		  System.out.println("Log the event");
	  }
	  
}
