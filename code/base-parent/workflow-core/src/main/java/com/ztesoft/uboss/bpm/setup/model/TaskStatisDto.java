package com.ztesoft.uboss.bpm.setup.model;

public class TaskStatisDto {

	private String state;
	
	private String stateCount;//特定状态工单数量

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateCount() {
		return stateCount;
	}

	public void setStateCount(String stateCount) {
		this.stateCount = stateCount;
	}
	
}
