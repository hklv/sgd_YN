package com.ztesoft.uboss.bpm.analysis.model;

import java.sql.Date;

public class UserDelayedTasks {

	private long total;
	
	private String delayedType;
	
	private Date analyseDay;
	
	private Float warnPercentage;
	
	private Float alarmPercentage;
	
	private long delayedTotal;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public String getDelayedType() {
		return delayedType;
	}

	public void setDelayedType(String delayedType) {
		this.delayedType = delayedType;
	}

	public Date getAnalyseDay() {
		return analyseDay;
	}

	public void setAnalyseDay(Date analyseDay) {
		this.analyseDay = analyseDay;
	}

	public Float getWarnPercentage() {
		return warnPercentage;
	}

	public void setWarnPercentage(Float warnPercentage) {
		this.warnPercentage = warnPercentage;
	}

	public Float getAlarmPercentage() {
		return alarmPercentage;
	}

	public void setAlarmPercentage(Float alarmPercentage) {
		this.alarmPercentage = alarmPercentage;
	}

	public long getDelayedTotal() {
		return delayedTotal;
	}

	public void setDelayedTotal(long delayedTotal) {
		this.delayedTotal = delayedTotal;
	}
}
