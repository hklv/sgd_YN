package com.ztesoft.uboss.bpm.analysis.model;

import java.sql.Date;

public class DelayedFlows {

	private long delayedFlowNumber;
	
	private Date analyseMonth;
	
	public long getDelayedFlowNumber() {
		return delayedFlowNumber;
	}

	public void setDelayedFlowNumber(long delayedFlowNumber) {
		this.delayedFlowNumber = delayedFlowNumber;
	}

	public Date getAnalyseMonth() {
		return analyseMonth;
	}

	public void setAnalyseMonth(Date analyseMonth) {
		this.analyseMonth = analyseMonth;
	}
}
