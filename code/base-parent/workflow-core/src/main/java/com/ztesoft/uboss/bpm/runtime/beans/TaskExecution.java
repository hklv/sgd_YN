package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;

import com.ztesoft.uboss.bpm.runtime.client.ActivityType;

public class TaskExecution extends ExecutionInfo {

	private final boolean forCompensation;

	public TaskExecution(Execution execution, ActivityImpl activity,
			ActivityType activityType) {
		super(execution, activity, activityType);
		this.forCompensation = false;
	}

	public boolean isForCompensation() {
		return forCompensation;
	}

}
