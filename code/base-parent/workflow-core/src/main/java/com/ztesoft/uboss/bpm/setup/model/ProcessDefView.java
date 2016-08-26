package com.ztesoft.uboss.bpm.setup.model;

import java.sql.Date;

public class ProcessDefView {
	private long processVerId;
	
	private String flowId;
	
	private String flowName;
	
	private String flowVersion;
	
	private String flowDeploymentId;
	
	private String flowResourceName;
	
	private String flowDgrmResourceName;
	
	private String startName = "startProcess()";
	
	private String start = "启动流程";
	
	private String definitionKey;
	
	private String processName;
	
	private Date createDate;
	
	private Date effectiveDate;
	
	private Date expiredDate;
	
	private String ver;
	
	private String procBizKey;
	
	private String bizKey;
	
	private Long formId;
	
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(String flowVersion) {
		this.flowVersion = flowVersion;
	}
	public String getFlowDeploymentId() {
		return flowDeploymentId;
	}
	public void setFlowDeploymentId(String flowDeploymentId) {
		this.flowDeploymentId = flowDeploymentId;
	}
	public String getFlowResourceName() {
		return flowResourceName;
	}
	public void setFlowResourceName(String flowResourceName) {
		this.flowResourceName = flowResourceName;
	}
	public String getFlowDgrmResourceName() {
		return flowDgrmResourceName;
	}
	public void setFlowDgrmResourceName(String flowDgrmResourceName) {
		this.flowDgrmResourceName = flowDgrmResourceName;
	}
	public String getStartName() {
		return startName;
	}
	public void setStartName(String startName) {
		this.startName = startName;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getDefinitionKey() {
		return definitionKey;
	}
	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}
	public long getProcessVerId() {
		return processVerId;
	}
	public void setProcessVerId(long processVerId) {
		this.processVerId = processVerId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getProcBizKey() {
		return procBizKey;
	}
	public void setProcBizKey(String procBizKey) {
		this.procBizKey = procBizKey;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public String getBizKey() {
		return bizKey;
	}
	public void setBizKey(String bizKey) {
		this.bizKey = bizKey;
	}
}
