package nanquan.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public class FinancialReportProcessTest {

	private String pid = "financialReport:6:713";
	private String key = "financialReport2";
//	private String name = "kermit";
	private String name = "nanquan";
	
	private ProcessEngine getProcessEngine(){
		ProcessEngine processEngine = ActivitiHelper.getProcessEngine();
		//ProcessEngines.getDefaultProcessEngine()
		return processEngine;
	}
	
	private void loadProcess(String deployFile) {
		if (deployFile != null && deployFile != "") {

			ProcessEngine processEngine = getProcessEngine();
			  
			  // Get Activiti services
			  RepositoryService repositoryService = processEngine.getRepositoryService();
			  RuntimeService runtimeService = processEngine.getRuntimeService();
			  TaskService taskService = processEngine.getTaskService();
			  
			  // Deploy the process definition
			  Deployment deployment = repositoryService.createDeployment()
			  .name("FinancialReportProcess")
			    .addClasspathResource(deployFile)
			    .deploy();
			  //repositoryService.activateProcessDefinitionByKey("financialReport");
		}
	}
	
	private void viewProcess() {
		
		// init process engine
		ProcessEngine processEngine = getProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		// query process list
		System.out.println("********** Process List **********");
		List<ProcessDefinition> pdList = repositoryService
				.createProcessDefinitionQuery().list();
		System.out
				.println("Process ID\t| Process Name\t| Version\t| Deploy ID");
		for (ProcessDefinition pd : pdList) {
			System.out.println(pd.getId() + "\t| " + pd.getName() + "\t| "
					+ pd.getVersion() + "\t| " + pd.getDeploymentId());
		}

		System.out.println("Found " + pdList.size() + " processes.");
	}
	
	private void startProcess() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init execution service

		// prepare params map
		Map map = new HashMap();
		map.put("owner", name);
		
//		processEngine.getRuntimeService().startProcessInstanceById(pid, map);
//		System.out.println("Process " + pid + " started.");
		
		processEngine.getRuntimeService().startProcessInstanceByKey(key, map);
		System.out.println("Process " + key + " started.");
	}

	private void viewTask() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init task service
		TaskService taskService = processEngine.getTaskService();

		// query task list
		System.out.println("********** Task List **********");

		List<Task> taskList = taskService.createTaskQuery().taskAssignee(name).list();
		List<Task> taskCandidateGroupList = taskService.createTaskQuery().taskCandidateGroup(name).list();
		System.out.println(taskCandidateGroupList);
		
		List<Task> taskCandidateUserList = taskService.createTaskQuery().taskCandidateUser(name).list();
		System.out.println(taskCandidateUserList);
		
		List<Task> taskOwnerList = taskService.createTaskQuery().taskOwner(name).list();
		System.out.println(taskOwnerList);
		
//		List<Task> taskList = taskService.findPersonalTasks(name);
		System.out.println("Personal Task >>");
		System.out.println("Task ID\t| Task Name\t| Assignee\t| Crate Time");
		for (Task tk : taskList) {
			System.out.println(tk.getId() + "\t| " + tk.getName() + "\t| "
					+ tk.getAssignee() + "\t| " + tk.getCreateTime());
		}

		System.out
				.println("Found "
						+ (taskList.size()) + " taskes.");
	}
	
	private void unloadProcess(String deploymentId) {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		try {
			//
			repositoryService.deleteDeployment(deploymentId, true);

			System.out.println("Undeploy succeed.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undeploy failed.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void applyLeave() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init task service
		TaskService taskService = processEngine.getTaskService();

		// complete task
		List<Task> submitFormTaskList = taskService.createTaskQuery().taskAssignee(name).list();
		for (Task tk : submitFormTaskList) {
			taskService.complete(tk.getId());
			System.out.println("[*] Writed the report with task " + tk.getId()
					+ ".");
		}
	}
	
	
	public static void main(String[] args) {
		
		FinancialReportProcessTest processTest = new FinancialReportProcessTest();
		
		  // Deploy the process definition
		  String deployFile = "org/activiti/examples/bpmn/usertask/FinancialReportProcess2.bpmn20.xml";
//		processTest.loadProcess(deployFile );
		processTest.viewProcess();
		processTest.startProcess();
		processTest.viewTask();
		
//		processTest.unloadProcess("910");
		  
	}
}
