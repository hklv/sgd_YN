package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;

import com.ztesoft.uboss.bpm.runtime.client.ActivityType;

public class CallActivityExecution extends TaskExecution {

	public CallActivityExecution(Execution execution, ActivityImpl activity) {
		super(execution, activity, ActivityType.CallActivity);
	}

}
