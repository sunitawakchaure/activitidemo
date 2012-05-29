package nanquan.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;

public class ActivitiTestBase {

	protected RepositoryService repositoryService;
	protected RuntimeService runtimeService;
	protected TaskService taskService;
	protected FormService formService;
	protected HistoryService historyService;
	protected IdentityService identityService;
	protected ManagementService managementService;
	protected ProcessEngine processEngine;
	private String deploymentId;

	public ActivitiTestBase() {
		processEngine = ActivitiHelper.getProcessEngine();
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
		formService = processEngine.getFormService();
		historyService = processEngine.getHistoryService();
		identityService = processEngine.getIdentityService();
		managementService = processEngine.getManagementService();
	}

	public void loadProcess(String deployFile, String deploymentName) {
		if (deployFile != null && deployFile != "") {

			// Get Activiti services
			RepositoryService repositoryService = processEngine
					.getRepositoryService();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			TaskService taskService = processEngine.getTaskService();

			// Deploy the process definition
			Deployment deployment = repositoryService.createDeployment()
					.name(deploymentName).addClasspathResource(deployFile)
					.deploy();
			deploymentId = deployment.getId();
			// repositoryService.activateProcessDefinitionByKey("financialReport");
		}
	}

	public void loadProcess(String[] deployFile, String deploymentName) {
		if (deployFile != null && deployFile.length > 0) {

			// Get Activiti services
			RepositoryService repositoryService = processEngine
					.getRepositoryService();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			TaskService taskService = processEngine.getTaskService();

			DeploymentBuilder deploymentBuilder = repositoryService
					.createDeployment().name(deploymentName);
			for (String file : deployFile) {
				// Deploy the process definition
				deploymentBuilder.addClasspathResource(file);
			}
			Deployment deployment = deploymentBuilder.deploy();
			deploymentId = deployment.getId();
			// repositoryService.activateProcessDefinitionByKey("financialReport");
		}
	}
	
	public void loadProcessByZip(String deployFile, String deploymentName) {
		if (deployFile != null && deployFile != "") {

			// Get Activiti services
			RepositoryService repositoryService = processEngine
					.getRepositoryService();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			TaskService taskService = processEngine.getTaskService();

			// put zip file into IO stream
			InputStream is = null;
			try {
				is = new FileInputStream(new File(deployFile));
				ZipInputStream zis = new ZipInputStream(is);
				
				// Deploy the process definition
				Deployment deployment = repositoryService.createDeployment()
				.name(deploymentName).addZipInputStream(zis)
				.deploy();
				deploymentId = deployment.getId();
				// repositoryService.activateProcessDefinitionByKey("financialReport");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadProcessByString(String deploymentName, String resourceName, String text) {
		if (text != null && text != "") {

			// Get Activiti services
			RepositoryService repositoryService = processEngine
					.getRepositoryService();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			TaskService taskService = processEngine.getTaskService();

			// Deploy the process definition
			Deployment deployment = repositoryService.createDeployment()
					.name(deploymentName).addString(resourceName, text)
					.deploy();
			deploymentId = deployment.getId();
			// repositoryService.activateProcessDefinitionByKey("financialReport");
		}
	}
	
	public void loadProcessByString(String deploymentName, Map<String, String> resources) {
		if (resources != null && resources.size() > 0) {
			// Get Activiti services
			RepositoryService repositoryService = processEngine
			.getRepositoryService();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			TaskService taskService = processEngine.getTaskService();
			
			
			DeploymentBuilder deploymentBuilder = repositoryService
					.createDeployment().name(deploymentName);
			
			for (Map.Entry<String, String> entry : resources.entrySet()) {
				String resourceName = entry.getKey(), text = entry.getValue();
				deploymentBuilder.addString(resourceName, text);
			}
			Deployment deployment = deploymentBuilder.deploy();
			deploymentId = deployment.getId();
		}
	}

	public void assertProcessEnded(final String processInstanceId) {
		ProcessInstance processInstance = processEngine.getRuntimeService()
				.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();

		if (processInstance != null) {
			throw new RuntimeException("Expected finished process instance '"
					+ processInstanceId + "' but it was still in the db");
		}
	}

	public void unloadProcess() {
		try {
			repositoryService.deleteDeployment(deploymentId, true);
			System.out.println("Undeploy succeed.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undeploy failed.");
		}
	}
}
