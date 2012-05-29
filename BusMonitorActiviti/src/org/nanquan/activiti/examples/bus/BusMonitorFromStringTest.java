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

package org.nanquan.activiti.examples.bus;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nanquan.test.ActivitiTestBase;

import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @author nanquan
 */
public class BusMonitorFromStringTest extends ActivitiTestBase {

	private String autoDueTime = "PT1M";
	private String key = "BusMonitorProcess";
	private String signal = "alert";
	private String processInstanceId;
	private String executionId;

	// Mail Configuration
//	 private String from = "nanquan520_tmp@126.com";
	private String from = "nanquan520.tmp@gmail.com";
	 
//	private String recipient = "nanquan520@qq.com";
	private String recipient = "wanghcdl@cn.ibm.com";

	public BusMonitorFromStringTest() {
		System.setProperty("mail.smtp.auth", "true");
//		System.setProperty("mail.smtp.socketFactory.port", "465");
		System.setProperty("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		System.setProperty("mail.smtp.socketFactory.fallback", "false");
		System.setProperty("mail.smtp.starttls.required", "true");
	}

	@SuppressWarnings("unchecked")
	public void startEvent() {
		Map map = new HashMap();
		map.put("autoDueTime", autoDueTime);
		BusEvent busEvent = new BusEvent();
		String type = RandomUtils.nextBoolean() ? "Type1" : "Type2";
		busEvent.setType(type);
		map.put("busEvent", busEvent);
		map.put("sender", from);
		map.put("recipient", recipient);

		// Start process instance
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(key, map);
		processInstanceId = pi.getProcessInstanceId();
		executionId = pi.getId();

	}

	public void signalEvent() {
		runtimeService.signalEventReceived(signal);

		// runtimeService.signalEventReceived(signal, executionId);
	}

	public static void main(String[] args) throws IOException {
		BusMonitorFromStringTest test = new BusMonitorFromStringTest();
		String deployFile = "archive/BusMonitorProcess.zip";
		String deploymentName = "BusMonitorProcessDeployment2";
//		test.loadProcessByZip(deployFile, deploymentName);
//		 test.loadProcess(new String[]{"org/nanquan/activiti/examples/bus/BusMonitorProcess.bpmn20.xml", "org/nanquan/activiti/examples/bus/event_rules.drl"},
//		 deploymentName);
		
		String busMonitorProcess = FileUtils.readFileToString(new File("proc/org/nanquan/activiti/examples/bus/BusMonitorProcess.bpmn20.xml"));
		String event_rules = FileUtils.readFileToString(new File("proc/org/nanquan/activiti/examples/bus/event_rules.drl"));
		
		Map<String, String> resources = new HashMap<String, String>();
		resources.put("BusMonitorProcess.bpmn20.xml", busMonitorProcess);
		resources.put("event_rules.drl", event_rules);
		test.loadProcessByString(deploymentName, resources);

		try {
			test.startEvent();

			test.signalEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}

		test.unloadProcess();
	}

}
