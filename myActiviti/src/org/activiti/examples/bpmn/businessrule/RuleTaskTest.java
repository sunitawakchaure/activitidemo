/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.examples.bpmn.businessrule;

import java.util.HashMap;
import java.util.Map;

import nanquan.test.ActivitiTestBase;

import org.activiti.engine.runtime.ProcessInstance;

import demo.Order;


/**
 * @author Joram Barrez
 */
public class RuleTaskTest extends ActivitiTestBase {

  public void testWaitStateBehavior() {
	  	Map map = new HashMap();
	  	Order order = new Order();
	  	order.setType("Type1");
	  	map.put("order", order);
	  	
		
		String key = "simpleBusinessRuleProcess";
		
		// Start process instance
		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey(key, map );
		

		// There should be one task, with a timer : first line support
//		Task task = taskService.createTaskQuery().processDefinitionKey(key)
//				.singleResult();
//		taskService.complete(task.getId());
		
		System.out.println();
    
  }
  
  public static void main(String[] args) {
	  RuleTaskTest test = new RuleTaskTest();
//		String deployFile = "org/activiti/examples/bpmn/rule/BusinessRuleTask.bpmn20.xml";
	  String deployFile = "archive/rules.zip";
		String ruleFile = "org/activiti/examples/bpmn/rule/rules.drl";
		String deploymentName = "ruleTaskDeployment";
//		test.loadProcess(new String[]{deployFile, ruleFile}, deploymentName);
//		test.loadProcess(deployFile, deploymentName);
		test.loadProcessByZip(deployFile, deploymentName);
		
		try {
			test.testWaitStateBehavior();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		test.unloadProcess();
	}

}
