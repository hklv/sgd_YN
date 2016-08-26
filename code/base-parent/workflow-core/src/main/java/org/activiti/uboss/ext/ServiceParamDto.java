package org.activiti.uboss.ext;

public class ServiceParamDto {

	String serviceName;
	Long templateId;
	Long taskTemplateId;
	String varCode;
	String varName;
	String varType;
	String varValue;
	String defaultValue;
	String scope;
	String comments;
	String tacheId;
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public Long getTaskTemplateId() {
		return taskTemplateId;
	}
	public void setTaskTemplateId(Long taskTemplateId) {
		this.taskTemplateId = taskTemplateId;
	}
	public String getVarCode() {
		return varCode;
	}
	public void setVarCode(String varCode) {
		this.varCode = varCode;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getVarType() {
		return varType;
	}
	public void setVarType(String varType) {
		this.varType = varType;
	}
	public String getVarValue() {
		return varValue;
	}
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getTacheId() {
		return tacheId;
	}
	public void setTacheId(String tacheId) {
		this.tacheId = tacheId;
	}
}
