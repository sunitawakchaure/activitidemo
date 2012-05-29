package nanquan.test;

import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.PvmExecution;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmProcessInstance;
import org.activiti.examples.pvm.WaitState;

public class PVMTest {

	public static void main(String[] args) {
		PvmProcessDefinition processDefinition = new ProcessDefinitionBuilder()
		  .createActivity("a")
		    .initial()
		    .behavior(new WaitState())
		    .transition("b")
		  .endActivity()
		  .createActivity("b")
		    .behavior(new WaitState())
		    .transition("c")
		  .endActivity()
		  .createActivity("c")
		    .behavior(new WaitState())
		  .endActivity()
		  .buildProcessDefinition();

		PvmProcessInstance processInstance = processDefinition.createProcessInstance();
		processInstance.start();

		PvmExecution activityInstance = processInstance.findExecution("a");
		System.out.println(activityInstance);
		
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder();
//		builder.
		
	}
}
