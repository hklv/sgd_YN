package com.ztesoft.uboss.bpm.webservice.model;

public class TaskOperationReq {

	/**
	 * 200:完成任务
	 * 201:退回任务
	 * 202：增派任务
	 * 203：转派任务
	 * 204：重新执行任务
	 */
	int action;
	
	/**
	 * 流程定义KEY
	 */
	String taskListId;
	
	/**
	 * 用户ID
	 */
	Long userId;
	
	/**
	 * 被转派人ID
	 */
	Long assginUserId;
	
	/**
	 * 回退原因ID
	 */
	Long backReasonId;

	public Long getBackReasonId() {
		return backReasonId;
	}

	public void setBackReasonId(Long backReasonId) {
		this.backReasonId = backReasonId;
	}

	public Long getAssginUserId() {
		return assginUserId;
	}

	public void setAssginUserId(Long assginUserId) {
		this.assginUserId = assginUserId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
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
}
