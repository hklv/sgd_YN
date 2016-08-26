package org.activiti.uboss.model;

public class BpmTaskSiginType {
	private String siginType;
	
	private String siginName;
	
	private Long voteCnt;

	public String getSiginType() {
		return siginType;
	}

	public void setSiginType(String siginType) {
		this.siginType = siginType;
	}
	
	public Long getVoteCnt() {
		return voteCnt;
	}

	public void setVoteCnt(Long voteCnt) {
		this.voteCnt = voteCnt;
	}

	public String getSiginName() {
		return siginName;
	}

	public void setSiginName(String siginName) {
		this.siginName = siginName;
	}
}
