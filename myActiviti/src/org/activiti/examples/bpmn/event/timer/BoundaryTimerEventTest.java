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
package org.activiti.examples.bpmn.event.timer;

import nanquan.test.ActivitiTestBase;

import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class BoundaryTimerEventTest extends ActivitiTestBase {

	public void testInterruptingTimerDuration() {

		// Start process instance
		ProcessInstance pi = runtimeService
				.startProcessInstanceByKey("escalationExample");

		// There should be one task, with a timer : first line support
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId())
				.singleResult();
		// assertEquals("First line support", task.getName());
		System.out.println(task.getName());
//		taskService.complete(task.getId());

		// Manually execute the job
		Job timer = managementService.createJobQuery().singleResult();
		managementService.executeJob(timer.getId());

		// The timer has fired, and the second task (secondlinesupport) now
		// exists
		task = taskService.createTaskQuery().processInstanceId(pi.getId())
				.singleResult();
		// assertEquals("Handle escalated issue", task.getName());
		System.out.println(task.getName());
	}

	public static void main(String[] args) {
		BoundaryTimerEventTest test = new BoundaryTimerEventTest();
		String deployFile = "org/activiti/examples/bpmn/event/timer/BoundaryTimerEventTest.testInterruptingTimerDuration.bpmn20.xml";
		String deploymentName = "escalationExampleDeployment";
		test.loadProcess(deployFile, deploymentName);
		
		test.testInterruptingTimerDuration();
		
		test.unloadProcess();
	}

}
