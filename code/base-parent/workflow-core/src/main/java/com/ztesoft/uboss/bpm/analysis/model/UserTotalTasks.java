package com.ztesoft.uboss.bpm.analysis.model;

import java.sql.Date;


public class UserTotalTasks {
	
	private long total;
	
	private Date analyseDay;
	
	private String taskType;//F：finish;U：unfinish

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Date getAnalyseDay() {
		return analyseDay;
	}

	public void setAnalyseDay(Date analyseDay) {
		this.analyseDay = analyseDay;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
}
