package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.ext.ZTaskDefinition;

import com.ztesoft.uboss.bpm.runtime.client.ActivityType;
import com.ztesoft.uboss.bpm.runtime.client.BpmUtils;

public class UserTaskExecution extends TaskExecution {

	private String taskTemplateId = null;

	private ParamInfo[] inputs;

	private ParamInfo[] outputs;

	public UserTaskExecution(Execution execution, ActivityImpl activity) {
		super(execution, activity, ActivityType.UserTask);

		ZTaskDefinition taskDefinition = (ZTaskDefinition) ZExtendsionUtil
				.getActivityInfo(activity);

		this.taskTemplateId = taskDefinition.getTaskTemplateId();
		this.inputs = BpmUtils.wrap(taskDefinition.getInputs());
		this.outputs = BpmUtils.wrap(taskDefinition.getOutputs());

	}

	public ParamInfo[] getPageServiceInputs() {
		return inputs;
	}

	public ParamInfo[] getPageServiceOutputs() {
		return outputs;
	}
}
