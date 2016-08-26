package com.ztesoft.uboss.bpm.runtime.listener;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.uboss.ext.CallableDefinition;
import org.activiti.uboss.ext.ZServiceDefinition;

import com.ztesoft.uboss.bpm.runtime.task.ServiceCaller;

public class TaskListenerImpl extends ServiceCaller implements TaskListener {

	@Override
	public void setCallableDefinition(CallableDefinition callableDefinition) {
		if(callableDefinition instanceof ZServiceDefinition){
			super.setCallableDefinition(callableDefinition);
		}
		else{
			throw new ActivitiException("callableDefinition must be a " + ZServiceDefinition.class);
		}
	}
	
	public ZServiceDefinition getServiceDefinition(){
		return (ZServiceDefinition)super.getCallableDefinition();
	}

	public void notify(DelegateTask delegateTask) {

	}

}
