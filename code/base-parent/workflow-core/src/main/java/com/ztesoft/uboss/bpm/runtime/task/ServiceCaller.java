package com.ztesoft.uboss.bpm.runtime.task;

import org.activiti.engine.ActivitiException;
import org.activiti.uboss.ext.CallableDefinition;

public abstract class ServiceCaller {
	private CallableDefinition callableDefinition = null;

	public CallableDefinition getCallableDefinition() {
		return callableDefinition;
	}

	public void setCallableDefinition(CallableDefinition callableDefinition) {
		if (this.callableDefinition != null) {
			throw new ActivitiException("service name has already been set : " + this.callableDefinition + ", new service name : " + callableDefinition);
		}
		this.callableDefinition = callableDefinition;
	}
}
