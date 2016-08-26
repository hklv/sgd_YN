package com.ztesoft.uboss.bpm.runtime.beans;

import java.util.Date;

import org.activiti.engine.history.HistoricActivityInstance;

/**
 * BPM Activity实体类
 * 
 * @author chen.gang71
 * 
 */
public class ActivityInfo {
	// 活动实例ID
	private String instanceId;
	private String activityId;
	private String activityName;
	private String activityType;
	private String processDefinitionId;
	// 流程实例ID
	private String processInstanceId;
	private String executionId;
	private String assignee;
	private Date startTime;
	private Date endTime;
	private Long durationInMillis;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public String toString() {
		return activityType + "[" + activityId + "]";
	}

	public static ActivityInfo build(HistoricActivityInstance hisInst) {
		ActivityInfo newInstance = new ActivityInfo();
		newInstance.setInstanceId(hisInst.getId());
		newInstance.setExecutionId(hisInst.getExecutionId());
		newInstance.setActivityId(hisInst.getActivityId());
		newInstance.setActivityName(hisInst.getActivityName());
		newInstance.setActivityType(hisInst.getActivityType());
		newInstance.setProcessInstanceId(hisInst.getProcessInstanceId());
		newInstance.setProcessDefinitionId(hisInst.getProcessDefinitionId());
		newInstance.setAssignee(hisInst.getAssignee());
		newInstance.setStartTime(hisInst.getStartTime());
		newInstance.setEndTime(hisInst.getEndTime());
		return newInstance;
	}
}
