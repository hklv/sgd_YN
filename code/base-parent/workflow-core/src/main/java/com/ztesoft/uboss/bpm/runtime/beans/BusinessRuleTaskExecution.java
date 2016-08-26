package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.ext.ZRuleDefinition;

import com.ztesoft.uboss.bpm.runtime.client.ActivityType;

public class BusinessRuleTaskExecution extends TaskExecution {

	private final String resultVariableName;

	private final String taskTemplateId;

	public BusinessRuleTaskExecution(Execution execution, ActivityImpl activity) {
		super(execution, activity, ActivityType.BusinessRuleTask);
		ZRuleDefinition serviceDefinition = (ZRuleDefinition) ZExtendsionUtil
				.getActivityInfo(activity);
		this.resultVariableName = serviceDefinition.getResultVariableName();
		this.taskTemplateId = serviceDefinition.getTaskTemplateId();
	}

	public String getResultVariableName() {
		return resultVariableName;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

}
