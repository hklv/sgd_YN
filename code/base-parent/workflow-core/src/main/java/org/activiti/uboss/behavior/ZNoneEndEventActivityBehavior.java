package org.activiti.uboss.behavior;

import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.model.ZFlowDef;
import org.activiti.uboss.task.ITaskHolderEntity;

public class ZNoneEndEventActivityBehavior extends NoneEndEventActivityBehavior {
	public void execute(ActivityExecution execution) throws Exception {
		
		if(execution.isProcessInstance())
		{
			ITaskHolderEntity th = (ITaskHolderEntity) Class.forName(TASK_HOLDER_ENTITY_STR)
					.newInstance();
					th.close(execution.getVariable(ZFlowDef.BPM_TASK_HOLDER_ID));
		}
		
		execution.end();
	}
}
