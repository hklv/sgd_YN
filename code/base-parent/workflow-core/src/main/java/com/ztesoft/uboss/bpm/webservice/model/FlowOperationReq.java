package com.ztesoft.uboss.bpm.webservice.model;

public class FlowOperationReq {
	
	/**
	 * 100:启动流程
	 * 101:挂起流程
	 * 102：解挂流程
	 * 103：终止流程
	 * 104：撤销流程(撤单)
	 */
	int action;
	
	/**
	 * 流程定义KEY
	 */
	String flowDefKey;
	
	/**
	 * 流程单ID
	 */
	String holderId;
	
	/**
	 * 用户ID
	 */
	Long userId;

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getFlowDefKey() {
		return flowDefKey;
	}

	public void setFlowDefKey(String flowDefKey) {
		this.flowDefKey = flowDefKey;
	}

	public String getHolderId() {
		return holderId;
	}

	public void setHolderId(String holderId) {
		this.holderId = holderId;
	}
}
