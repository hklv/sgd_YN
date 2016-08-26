package com.ztesoft.uboss.bpm.runtime.beans;

import java.sql.Date;

public class ProcessBackInfo {
	
	private Long backReasonId;
	
	private Long userId;
	
	private String backComments;
	
	private String taskListId;
	
	private String holderId;
	
	private String backUser;
	
	private Date createdTime;
	
	private String backId;
	
	private String state;

	public Long getBackReasonId() {
		return backReasonId;
	}

	public void setBackReasonId(Long backReasonId) {
		this.backReasonId = backReasonId;
	}

	public String getBackComments() {
		return backComments;
	}

	public void setBackComments(String backComments) {
		this.backComments = backComments;
	}

	public String getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(String taskListId) {
		this.taskListId = taskListId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getHolderId() {
		return holderId;
	}

	public void setHolderId(String holderId) {
		this.holderId = holderId;
	}

	public String getBackUser() {
		return backUser;
	}

	public void setBackUser(String backUser) {
		this.backUser = backUser;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getBackId() {
		return backId;
	}

	public void setBackId(String backId) {
		this.backId = backId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
