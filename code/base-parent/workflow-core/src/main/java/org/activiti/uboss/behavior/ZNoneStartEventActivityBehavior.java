package org.activiti.uboss.behavior;

import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.task.ITaskHolderEntity;

public class ZNoneStartEventActivityBehavior extends
		NoneStartEventActivityBehavior {

	public void execute(ActivityExecution execution) throws Exception {
		
		ITaskHolderEntity th = (ITaskHolderEntity) Class.forName(TASK_HOLDER_ENTITY_STR).newInstance();
		
		th.createEntity(execution);
		leave(execution);
	}
}
