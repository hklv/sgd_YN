package org.activiti.uboss.behavior;

import org.activiti.engine.impl.bpmn.behavior.ScriptTaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class ZScriptTaskActivityBehavior extends ScriptTaskActivityBehavior {

	public ZScriptTaskActivityBehavior(String script, String language,
			String resultVariable) {
		super(script, language, resultVariable);
	}

	public void doBiz(ActivityExecution execution) {

	}
	
	  public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
		  if("leaveEx".equals(signalName)) {
				//to do
			  	execute(execution);
			    } else {
			      super.signal(execution, signalName, signalData);
			    }
	  }
}
