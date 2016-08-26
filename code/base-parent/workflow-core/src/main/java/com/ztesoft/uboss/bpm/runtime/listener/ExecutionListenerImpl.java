package com.ztesoft.uboss.bpm.runtime.listener;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.uboss.ext.CallableDefinition;
import org.activiti.uboss.ext.ZServiceDefinition;

import com.ztesoft.uboss.bpm.runtime.task.ServiceCaller;

public class ExecutionListenerImpl extends ServiceCaller implements ExecutionListener {

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
	
	public void notify(DelegateExecution execution) throws Exception {
	}

}
