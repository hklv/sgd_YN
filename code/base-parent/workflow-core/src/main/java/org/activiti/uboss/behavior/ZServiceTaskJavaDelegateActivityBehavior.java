package org.activiti.uboss.behavior;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.bpmn.behavior.ServiceTaskJavaDelegateActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class ZServiceTaskJavaDelegateActivityBehavior extends
		ServiceTaskJavaDelegateActivityBehavior {
	protected boolean waitForSignal = false;
	
	private String taskTemplateId = null;

	public ZServiceTaskJavaDelegateActivityBehavior(String taskTemplateId,boolean waitForSignal,
			JavaDelegate delegateInstance) {
		
		super(delegateInstance);
		this.taskTemplateId = taskTemplateId;
		this.waitForSignal = waitForSignal;
	}

	public void execute(ActivityExecution execution) throws Exception {

		try {
			execute((DelegateExecution) execution);
		} catch (Exception ex) {

			this.doSysExTask(execution, taskTemplateId, ex.toString());
			return;
		}

		//插入工单表
		this.doSysTask(execution, taskTemplateId, waitForSignal);
		
		if(!waitForSignal){
			leave(execution);
		}
	}
	
	public void signal(ActivityExecution execution, String signalName,
			Object signalData) throws Exception {
		
		if("leaveEx".equals(signalName)) {
			execute((DelegateExecution) execution);
			leave(execution);
		} else if("leaveByWaitSignal".equals(signalName)){
		    leave(execution);
	    } else {
		    super.signal(execution, signalName, signalData);
		}
	}

}
