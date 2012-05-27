package nanquan.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

public class ActivitiTest1 {

	public static void main(String[] args) {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		String deployFile = "Alert.bpmn20.xml";
		try {
			// publish zip file to process vm
			Deployment deploy = repositoryService.createDeployment().name("Alert.bpmn20")
					.addClasspathResource(deployFile).deploy();

			System.out.println("Publish succeed.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Publish failed.");
		}
	}
}
