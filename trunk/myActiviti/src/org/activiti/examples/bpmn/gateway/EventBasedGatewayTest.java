package org.activiti.examples.bpmn.gateway;

import java.util.HashMap;
import java.util.Map;

import nanquan.test.ActivitiTestBase;

import org.activiti.engine.runtime.ProcessInstance;

public class EventBasedGatewayTest  extends ActivitiTestBase {

	private String autoDueTime = "PT1M";
	private String key = "catchSignal";
	private String signal = "alert";
	private String processInstanceId;
	private String executionId;
	
  public void startEvent() {
	  	Map map = new HashMap();
	  	map.put("autoDueTime", autoDueTime);
	  	
		// Start process instance
		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey(key, map );
		processInstanceId = pi.getProcessInstanceId();
		executionId = pi.getId();
		
		// There should be one task, with a timer : first line support
//		Task task = taskService.createTaskQuery().processDefinitionKey(key)
//				.singleResult();
//		taskService.complete(task.getId());
		
  }
  
  public void signalEvent(){
	  runtimeService.signalEventReceived(signal);
	  System.out.println("signalEvent");
//	  Execution execution = runtimeService.createExecutionQuery()
//      .processInstanceId(this.processInstanceId)
//      .activityId("signalintermediatecatchevent1")
//      .singleResult();
//	  runtimeService.signal(execution.getId());
  }
  
  public static void main(String[] args) {
	  EventBasedGatewayTest test = new EventBasedGatewayTest();
		String deploymentName = "EventBasedGatewayDeployment";
//		test.loadProcessByZip(deployFile, deploymentName);
		test.loadProcess("org/activiti/examples/bpmn/gateway/EventBasedGateway.bpmn20.xml", deploymentName);
		
		try {
			test.startEvent();
			
			test.signalEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		test.unloadProcess();
	  
	}
}
