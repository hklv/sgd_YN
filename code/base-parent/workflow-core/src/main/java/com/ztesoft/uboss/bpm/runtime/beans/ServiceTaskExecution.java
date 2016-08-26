package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.ext.ZServiceDefinition;

import com.ztesoft.uboss.bpm.runtime.client.ActivityType;
import com.ztesoft.uboss.bpm.runtime.client.BpmUtils;

public class ServiceTaskExecution extends TaskExecution {

	private final String resultVariableName;

	private final String taskTemplateId;

	private final ParamInfo[] inputs;

	private final ParamInfo[] outputs;

	public ServiceTaskExecution(Execution execution, ActivityImpl activity) {
		super(execution, activity, ActivityType.ServiceTask);

		ZServiceDefinition serviceDefinition = (ZServiceDefinition) ZExtendsionUtil
				.getActivityInfo(activity);
		this.resultVariableName = serviceDefinition.getResultVariableName();
		this.taskTemplateId = serviceDefinition.getTaskTemplateId();
		this.inputs = BpmUtils.wrap(serviceDefinition.getInputs());
		this.outputs = BpmUtils.wrap(serviceDefinition.getOutputs());
	}

	public String getResultVariableName() {
		return resultVariableName;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	public ParamInfo[] getInputs() {
		return inputs;
	}

	public ParamInfo[] getOutputs() {
		return outputs;
	}
}
