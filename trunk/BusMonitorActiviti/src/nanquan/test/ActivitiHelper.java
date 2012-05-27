package nanquan.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class ActivitiHelper {

	private static ProcessEngine pe = null;

	public static ProcessEngine getProcessEngine() {
		if (pe == null) {

//			ProcessEngine processEngine = ProcessEngineConfiguration
//					.createStandaloneProcessEngineConfiguration()
//					.buildProcessEngine();
			
			ProcessEngine processEngine = ProcessEngineConfiguration
			   .createProcessEngineConfigurationFromResourceDefault()
			   .buildProcessEngine();
			
			pe = processEngine;
		}
		return pe;
	}

}
