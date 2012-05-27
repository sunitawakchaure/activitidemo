package nanquan.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

public class ActivitiDeployTest {

	public static void main(String[] args) throws Exception {
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		
		String barFileName = "path/to/process-one.bar";
		
		ZipInputStream inputStream = new ZipInputStream(new FileInputStream(barFileName));
	    
		repositoryService.createDeployment()
		    .name("process-one.bar")
		    .addZipInputStream(inputStream)
		    .deploy();
		
		/*
		repositoryService.createDeployment()
  .name("expense-process.bar")
  .addClasspathResource("org/activiti/expenseProcess.bpmn20.xml")
  .addClasspathResource("org/activiti/expenseProcess.png")
  .deploy();
		 */
	}
}
