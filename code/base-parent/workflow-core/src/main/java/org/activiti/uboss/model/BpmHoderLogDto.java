package org.activiti.uboss.model;

import java.sql.Date;

public class BpmHoderLogDto {
	
	private String logId;
	
	private String holderId;
	
	private String holderNo;
	
	private String processName;
	
	private String action;
	
	private Date actionTime;
	
	private String operator;
	
	private String comments;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getHolderId() {
		return holderId;
	}

	public void setHolderId(String holderId) {
		this.holderId = holderId;
	}

	public String getHolderNo() {
		return holderNo;
	}

	public void setHolderNo(String holderNo) {
		this.holderNo = holderNo;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
