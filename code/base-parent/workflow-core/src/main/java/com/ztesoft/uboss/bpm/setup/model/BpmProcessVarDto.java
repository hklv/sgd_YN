package com.ztesoft.uboss.bpm.setup.model;

public class BpmProcessVarDto {
	private long id_;
	//
	private String procDefId;
	//
	private String varName;
	//
	private String varType;
	//
	private String defaultValue;
	//
	private String varComments;
	public long getId_() {
		return id_;
	}
	public void setId_(long id_) {
		this.id_ = id_;
	}
	public String getProcDefId() {
		return procDefId;
	}
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
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
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getVarComments() {
		return varComments;
	}
	public void setVarComments(String varComments) {
		this.varComments = varComments;
	}
}
