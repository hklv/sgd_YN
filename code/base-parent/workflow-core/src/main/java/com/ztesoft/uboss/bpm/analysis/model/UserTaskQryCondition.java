package com.ztesoft.uboss.bpm.analysis.model;

import java.sql.Date;

public class UserTaskQryCondition {

	private Long userId;
	
	private Long roleId;
	
	private Long orgId;
	
	private Long jobId;
	
	private String templateId;
	
	private String templateType;
	
	private Date month;
	
	private Date startMonth;
	
	private Date endMonth;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	public Date getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Date startMonth) {
		this.startMonth = startMonth;
	}

	public Date getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Date endMonth) {
		this.endMonth = endMonth;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
}
