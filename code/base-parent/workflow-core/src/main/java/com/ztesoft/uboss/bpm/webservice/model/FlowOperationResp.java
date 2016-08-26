package com.ztesoft.uboss.bpm.webservice.model;

import java.util.List;

public class FlowOperationResp {
	
	int resultCode;
	
	String errorMsg;
	
	List<String> currTaskListIds;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public List<String> getCurrTaskListIds() {
		return currTaskListIds;
	}

	public void setCurrTaskListIds(List<String> currTaskListIds) {
		this.currTaskListIds = currTaskListIds;
	}
}
