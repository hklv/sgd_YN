package com.ztesoft.uboss.bpm.taskcenter.model;


import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;

public class HolderQueryCondition{
	
	private String holderNo;
	
	private long procTempId;
	
	private long procDefTypeId;
	
	private String holderState;
	
	private String startTime;
	
	private String endTime;
	
	private RowSetFormatter rowSetFormatter;

	public String getHolderNo() {
		return holderNo;
	}

	public void setHolderNo(String holderNo) {
		this.holderNo = holderNo;
	}

	public long getProcTempId() {
		return procTempId;
	}

	public void setProcTempId(long procTempId) {
		this.procTempId = procTempId;
	}

	public long getProcDefTypeId() {
		return procDefTypeId;
	}

	public void setProcDefTypeId(long procDefTypeId) {
		this.procDefTypeId = procDefTypeId;
	}

	public String getHolderState() {
		return holderState;
	}

	public void setHolderState(String holderState) {
		this.holderState = holderState;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public RowSetFormatter getRowSetFormatter() {
		return rowSetFormatter;
	}

	public void setRowSetFormatter(RowSetFormatter rowSetFormatter) {
		this.rowSetFormatter = rowSetFormatter;
	}
}
