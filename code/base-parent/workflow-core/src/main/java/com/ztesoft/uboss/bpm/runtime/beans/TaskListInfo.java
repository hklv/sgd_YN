package com.ztesoft.uboss.bpm.runtime.beans;

import java.sql.Date;


public class TaskListInfo {
	private String taskListId;
	
	private String taskListName;
	
	private String taskResult;
	
	private String formId;

	private String taskType;

	private String taskTypeStr;
	
	private String taskState;
	
	private String taskStateStr;
	
	private String executionId;
	
	private String userId;
	
	private String userName;
	
	private Date createDate;
	
	private Date endTime;
	
	private String formType;
	
	private String pageUrl;
	
	private String dynFormId;
	
	private String holderNo;

	public String getHolderNo() {
		return holderNo;
	}

	public void setHolderNo(String holderNo) {
		this.holderNo = holderNo;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getDynFormId() {
		return dynFormId;
	}

	public void setDynFormId(String dynFormId) {
		this.dynFormId = dynFormId;
	}

	public String getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(String taskListId) {
		this.taskListId = taskListId;
	}

	public String getTaskListName() {
		return taskListName;
	}

	public void setTaskListName(String taskListName) {
		this.taskListName = taskListName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTaskTypeStr() {
		return taskTypeStr;
	}

	public void setTaskTypeStr(String taskTypeStr) {
		this.taskTypeStr = taskTypeStr;
	}

	public String getTaskStateStr() {
		return taskStateStr;
	}

	public void setTaskStateStr(String taskStateStr) {
		this.taskStateStr = taskStateStr;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

}
