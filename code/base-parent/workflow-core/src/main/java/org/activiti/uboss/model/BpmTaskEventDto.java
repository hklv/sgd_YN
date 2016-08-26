package org.activiti.uboss.model;

public class BpmTaskEventDto {
	
	private long taskTempLateId;
	
	private String serviceName;
	
	private String eventType;
	
	private String comments;

	public long getTaskTempLateId() {
		return taskTempLateId;
	}

	public void setTaskTempLateId(long taskTempLateId) {
		this.taskTempLateId = taskTempLateId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
