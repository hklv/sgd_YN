package org.activiti.uboss.model;

import java.util.ArrayList;
import java.util.List;

public class BpmTaskAssginDto{
 
	private Long userId;
	private Long orgId;
	private Long roleId;
	private Long jobId;
	private String serviceName;
	
	private List<Long> roleUserIds = new ArrayList<Long>();

 	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public List<Long> getRoleUserIds() {
		return roleUserIds;
	}

	public void setRoleUserIds(List<Long> roleUserIds) {
		this.roleUserIds = roleUserIds;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
