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

package org.activiti.examples.bpmn.receivetask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nanquan.test.ActivitiTestBase;
import nanquan.test.MyPrinter;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.examples.bpmn.event.timer.IntermediateTimerEventTest;
import org.apache.commons.lang.time.DateFormatUtils;


/**
 * @author Joram Barrez
 */
public class ReceiveTaskTest extends ActivitiTestBase {

  public void testWaitStateBehavior() {
	  MyPrinter myPrinter = new MyPrinter();
	  Map map = new HashMap();
//		map.put("printer", myPrinter);
		
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("receiveTask");
    Execution execution = runtimeService.createExecutionQuery()
      .processInstanceId(pi.getId())
      .activityId("waitState")
      .singleResult();
    System.out.println(execution.getProcessInstanceId());
//    assertNotNull(execution);
    
    runtimeService.signal(execution.getId(), map);
    assertProcessEnded(pi.getId());
  }
  
  public static void main(String[] args) {
	  ReceiveTaskTest test = new ReceiveTaskTest();
		String deployFile = "org/activiti/examples/bpmn/receivetask/ReceiveTaskTest.testWaitStateBehavior.bpmn20.xml";
		String deploymentName = "receiveTaskDeployment";
		test.loadProcess(deployFile, deploymentName);
		
		try {
			test.testWaitStateBehavior();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		test.unloadProcess();
	}

}
