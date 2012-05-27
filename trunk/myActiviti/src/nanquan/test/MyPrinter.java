package nanquan.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class MyPrinter implements JavaDelegate {
	
	public void printMsg(Object obj){
		System.out.println(obj);
	}

//	@Override
//	public void execute(ActivityExecution execution) throws Exception {
//		System.out.println("Run Service!");
//	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Run Service!");
	}

}
