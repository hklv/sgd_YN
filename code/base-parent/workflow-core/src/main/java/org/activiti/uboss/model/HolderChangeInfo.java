package org.activiti.uboss.model;

public class HolderChangeInfo {
	
	String holderId;
	String action;
	String userName; 
	String reason;
	
	public String getHolderId() {
		return holderId;
	}
	public void setHolderId(String holderId) {
		this.holderId = holderId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
