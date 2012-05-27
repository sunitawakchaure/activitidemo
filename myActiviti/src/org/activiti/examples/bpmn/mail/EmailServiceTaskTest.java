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

package org.activiti.examples.bpmn.mail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.activiti.examples.bpmn.gateway.EventBasedGatewayTest;

import nanquan.test.ActivitiTestBase;


/**
 * @author Joram Barrez
 */
public class EmailServiceTaskTest extends ActivitiTestBase {
  
  
  public void testSendEmail() throws Exception {
    
//    String from = "nanquan520.work@gmail.com";
    String from = "nanquan520_tmp@126.com";
    boolean male = true;
    String recipientName = "Nanquan";
    String recipient = "nanquan520@qq.com";
    Date now = new Date();
    String orderId = "123456";
    
    Map<String, Object> vars = new HashMap<String, Object>();
    vars.put("sender", from);
    vars.put("recipient", recipient);
    vars.put("recipientName", recipientName);
    vars.put("male", male);
    vars.put("now", now);
    vars.put("orderId", orderId);
    
    runtimeService.startProcessInstanceByKey("sendMailExample", vars);
    
  }
  
  public static void main(String[] args) {
	  EmailServiceTaskTest test = new EmailServiceTaskTest();
		String deploymentName = "EmailServiceTaskDeployment";
//		test.loadProcessByZip(deployFile, deploymentName);
		test.loadProcess("org/activiti/examples/bpmn/mail/EmailServiceTaskTest.testSendEmail.bpmn20.xml", deploymentName);
		
		try {
			test.testSendEmail();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		test.unloadProcess();
	  
	}
  

}
