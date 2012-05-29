package nanquan.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

/**
 * Integrate LDAP Test
 * @author wh
 *
 */
public class ProcessManager {

	private String key = "LeaveProcess";
	private String name = "admin01";
	
	private String firstMgr = "admin02";
	private String firstMgrGroup = "AdminGroup";
	private String secondMgr = "admin03";
//	private String secondMgr = firstMgr;
	private String secondMgrGroup = "AdminGroup";
	private String deploymentName = "LeaveProcessForLdap";
	private ProcessEngine pe = null;
	
	private String deploymentId;

	private ProcessEngine getProcessEngine() {
		if (pe == null) {

			ProcessEngine processEngine = ProcessEngineConfiguration
			   .createProcessEngineConfigurationFromResource("activiti.cfg-mem-ldap.xml")
			   .buildProcessEngine();
//			ProcessEngine processEngine = ActivitiHelper.getProcessEngine();
			// ProcessEngines.getDefaultProcessEngine()
			pe = processEngine;
		}
		return pe;
	}

	private void loadProcess(String deployFile) {
		if (deployFile != null && deployFile != "") {

			ProcessEngine processEngine = getProcessEngine();

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

	private void buildGroupMember() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// create group & member
		IdentityService identityService = processEngine.getIdentityService();

		List<Group> groupList = identityService.createGroupQuery()
				.groupId(firstMgrGroup).list();
		Group group = groupList.size() > 0 ? groupList.get(0) : null;
		// Group group = identityService.findGroupById(firstMgrGroup);
		if (group == null) {
			System.out.println("Create first manager group.");
			group = identityService.newGroup(firstMgrGroup);
		}
		identityService.saveGroup(group);
		String groupId = group.getId();
		
		List<User> userList = identityService.createUserQuery().userId(firstMgr).list();
		User user = userList.size() > 0 ? userList.get(0) : null;
		if(user == null){
			user = identityService.newUser(firstMgr);
		}
		identityService.saveUser(user);
		identityService.deleteMembership(firstMgr, groupId);
		identityService.createMembership(firstMgr, groupId);
		

		// group =
		// identityService.createGroupQuery().groupId(secondMgrGroup).list().get(0);
		groupList = identityService.createGroupQuery().groupId(secondMgrGroup)
				.list();
		group = groupList.size() > 0 ? groupList.get(0) : null;

		if (group == null) {
			System.out.println("Create second manager group.");
			group = identityService.newGroup(secondMgrGroup);

		}
		identityService.saveGroup(group);
		groupId = group.getId();
		
		userList = identityService.createUserQuery().userId(secondMgr).list();
		user = userList.size() > 0 ? userList.get(0) : null;
		if(user == null){
			user = identityService.newUser(secondMgr);
		}
		identityService.saveUser(user);
		identityService.deleteMembership(secondMgr, groupId);
		identityService.createMembership(secondMgr, groupId);
		
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

	@SuppressWarnings("unchecked")
	private void startProcess() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init execution service

		// prepare params map
		Map map = new HashMap();
		map.put("owner", name);
		// processEngine.getRuntimeService().startProcessInstanceById(pid, map);
		// System.out.println("Process " + pid + " started.");

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

		List<Task> taskList = taskService.createTaskQuery().taskAssignee(name)
				.list();
		// List<Task> taskList = taskService.findPersonalTasks(name);
		System.out.println("Personal Task >>");
		System.out.println("Task ID\t| Task Name\t| Assignee\t| Crate Time");
		for (Task tk : taskList) {
			System.out.println(tk.getId() + "\t| " + tk.getName() + "\t| "
					+ tk.getAssignee() + "\t| " + tk.getCreateTime());
		}
		
		List<Task> firstLineMgrTaskList = taskService.createTaskQuery()
				.taskCandidateGroup(firstMgrGroup).list();
		// List<Task> firstLineMgrTaskList =
		// taskService.findGroupTasks(firstMgr);
		System.out.println("1st Line Manager Task >>");
		System.out.println("Task ID\t| Task Name\t| Assignee\t| Crate Time");
		for (Task tk : firstLineMgrTaskList) {
			System.out.println(tk.getId() + "\t| " + tk.getName() + "\t| "
					+ tk.getAssignee() + "\t| " + tk.getCreateTime());
		}

		// List<Task> secondLineMgrTaskList = taskService
		// .findGroupTasks(secondMgr);
		List<Task> secondLineMgrTaskList = taskService.createTaskQuery()
				.taskCandidateGroup(secondMgrGroup).list();
		System.out.println("2nd Line Manager Task >>");
		System.out.println("Task ID\t| Task Name\t| Assignee\t| Crate Time");
		for (Task tk : secondLineMgrTaskList) {
			System.out.println(tk.getId() + "\t| " + tk.getName() + "\t| "
					+ tk.getAssignee() + "\t| " + tk.getCreateTime());
		}

		System.out
				.println("Found "
						+ (taskList.size() + firstLineMgrTaskList.size() + secondLineMgrTaskList
								.size()) + " taskes.");
	}

	@SuppressWarnings("unchecked")
	private void applyLeave() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init task service
		TaskService taskService = processEngine.getTaskService();

		// prepare apply data
		Map map = new HashMap();
		Integer randomDay = Double.valueOf(Math.random() * 10).intValue();
		System.out.println("[#] Random day for leave: " + randomDay);
		map.put("day", randomDay);
		map.put("reason", "Leave for vacation.");

		// complete task
		// List<Task> submitFormTaskList = taskService.findPersonalTasks(name);
		List<Task> submitFormTaskList = taskService.createTaskQuery()
				.taskAssignee(name).list();
		for (Task tk : submitFormTaskList) {
			taskService.complete(tk.getId(), map);
			System.out.println("[*] Apply for leave with task " + tk.getId()
					+ ".");
		}
	}

	private void firstApprove() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init task service
		TaskService taskService = processEngine.getTaskService();
		List<Task> firstLineMgrToDoList = new ArrayList<Task>();

		// find personal task
		// List<Task> firstLineMgrPersoanlTaskList = taskService
		// .findPersonalTasks(firstMgr);
		List<Task> firstLineMgrPersoanlTaskList = taskService.createTaskQuery()
				.taskAssignee(firstMgr).list();
		firstLineMgrToDoList.addAll(firstLineMgrPersoanlTaskList);

		// find group task
		// List<Task> firstLineMgrGroupTaskList = taskService
		// .findGroupTasks(firstMgr);
		List<Task> firstLineMgrGroupTaskList = taskService.createTaskQuery()
				.taskCandidateGroup(firstMgrGroup).list();
		for (Task tk : firstLineMgrGroupTaskList) {
			// taskService.takeTask(tk.getId(), firstMgr);
			taskService.claim(tk.getId(), firstMgr);
		}
		firstLineMgrToDoList.addAll(firstLineMgrGroupTaskList);

		for (Task tk : firstLineMgrToDoList) {
			taskService.complete(tk.getId());
			// taskService.completeTask(tk.getId(), "approve");
			System.out.println("[*] First approve for leave with task "
					+ tk.getId() + ".");
		}
	}

	private void secondApprove() {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();

		// init task service
		TaskService taskService = processEngine.getTaskService();
		List<Task> secondLineMgrToDoList = new ArrayList<Task>();

		// find personal task
		// List<Task> secondLineMgrPersoanlTaskList = taskService
		// .findPersonalTasks(secondMgr);
		List<Task> secondLineMgrPersoanlTaskList = taskService
				.createTaskQuery().taskAssignee(secondMgr).list();
		secondLineMgrToDoList.addAll(secondLineMgrPersoanlTaskList);

		// find group task
		// List<Task> secondLineMgrGroupTaskList = taskService
		// .findGroupTasks(secondMgr);
		List<Task> secondLineMgrGroupTaskList = taskService.createTaskQuery()
				.taskCandidateGroup(secondMgrGroup).list();
		for (Task tk : secondLineMgrGroupTaskList) {
			// taskService.takeTask(tk.getId(), secondMgr);
			taskService.claim(tk.getId(), secondMgr);
		}
		secondLineMgrToDoList.addAll(secondLineMgrGroupTaskList);

		for (Task tk : secondLineMgrToDoList) {
			taskService.complete(tk.getId());
			System.out.println("[*] Second approve for leave with task "
					+ tk.getId() + ".");
		}
	}

	private void unloadProcess(String deploymentId) {
		// init process engine
		ProcessEngine processEngine = getProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();

		try {
			repositoryService.deleteDeployment(deploymentId, true);

			System.out.println("Undeploy succeed.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Undeploy failed.");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ProcessManager manager = new ProcessManager();
		String deployFile = "LeaveProcess.bpmn20.xml";//LeaveProcess.bpmn20.xml_2012-05-23 
		 manager.loadProcess(deployFile);
//		manager.buildGroupMember();
		manager.viewProcess();
		manager.startProcess();
		manager.viewTask();
		manager.applyLeave();
		manager.firstApprove();
		manager.secondApprove();
		manager.viewTask();
		
		 manager.unloadProcess(manager.deploymentId);
	}
}
