package com.ztesoft.uboss.bpm.taskcenter.model;

import java.sql.Date;

import com.ztesoft.uboss.bpm.setup.model.BpmBackCfgDto;

public class TaskBackReasonInfo extends BpmBackCfgDto{
	
	String reasonName;
	
	String reasonCode;
	
	long taskTempId;
	
	String state;
	
	Date stateDate;
	
	String reasonComments;

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public long getTaskTempId() {
		return taskTempId;
	}

	public void setTaskTempId(long taskTempId) {
		this.taskTempId = taskTempId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}

	public String getReasonComments() {
		return reasonComments;
	}

	public void setReasonComments(String reasonComments) {
		this.reasonComments = reasonComments;
	}
}
