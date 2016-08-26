package com.ztesoft.uboss.bpm.runtime.beans;

public class FlowActivity {
	private String actId;
	
	private String actName;
	
	private String actType;
	
	private String taskTemplateId;
	
	private String taskTemplateCode;
	
	private String taskTemplateName;

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	public void setTaskTemplateId(String taskTemplateId) {
		this.taskTemplateId = taskTemplateId;
	}

	public String getTaskTemplateCode() {
		return taskTemplateCode;
	}

	public void setTaskTemplateCode(String taskTemplateCode) {
		this.taskTemplateCode = taskTemplateCode;
	}

	public String getTaskTemplateName() {
		return taskTemplateName;
	}

	public void setTaskTemplateName(String taskTemplateName) {
		this.taskTemplateName = taskTemplateName;
	}
}
