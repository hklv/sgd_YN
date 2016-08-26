package com.ztesoft.uboss.bpm.setup.model;

import java.sql.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

 
public class ProcessTemplateVersion {
	// 模板版本标识
	private Long processVerId;
	// 模板标识
	private String procTempId;

	// 部署标识
	private String deployId;

	// 定义图标识
	private String procDefId;

	// 版本号
	private String ver;
	// 生效时间
	private Date effectiveDate;
	// 失效时间
	private Date expiredDate;
	// 流程执行模板
	private String bpmnContent;
	// 操作人员
	private String userId;
	// 说明
	private String comments;
	// 创建时间
	private Date createDate;
	// 状态
	private String state;
	// 状态时间
	private Date stateDate;
	// 版本状态
	private String verState;

	// StarForm 表单标示
	private String formId;

	public Long getProcessVerId() {
		return processVerId;
	}

	public void setProcessVerId(Long processVerId) {
		this.processVerId = processVerId;
	}

	public String getProcTempId() {
		return procTempId;
	}

	public void setProcTempId(String procTempId) {
		this.procTempId = procTempId;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
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

 
	public String getBpmnContent() {
		return bpmnContent;
	}

	public void setBpmnContent(String bpmnContent) {
		this.bpmnContent = bpmnContent;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}

	public String getVerState() {
		return verState;
	}

	public void setVerState(String verState) {
		this.verState = verState;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
