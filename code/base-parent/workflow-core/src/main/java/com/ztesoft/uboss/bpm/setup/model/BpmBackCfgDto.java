package com.ztesoft.uboss.bpm.setup.model;

import java.sql.Date;

public class BpmBackCfgDto {
	
	private String procDefId;
	
	private Long backReasonId;
	
	private String srcActId;
	
	private String tagActId;
	
	private String state;
	
	private Date stateState;

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public Long getBackReasonId() {
		return backReasonId;
	}

	public void setBackReasonId(Long backReasonId) {
		this.backReasonId = backReasonId;
	}

	public String getSrcActId() {
		return srcActId;
	}

	public void setSrcActId(String srcActId) {
		this.srcActId = srcActId;
	}

	public String getTagActId() {
		return tagActId;
	}

	public void setTagActId(String tagActId) {
		this.tagActId = tagActId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getStateState() {
		return stateState;
	}

	public void setStateState(Date stateState) {
		this.stateState = stateState;
	}
}
