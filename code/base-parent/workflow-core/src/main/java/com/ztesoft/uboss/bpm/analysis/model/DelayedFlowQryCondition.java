package com.ztesoft.uboss.bpm.analysis.model;

import java.sql.Date;

public class DelayedFlowQryCondition {

	private Date year;
	
	private Long procDefTypeId;
	
	private Long procTempId;
	
	private Date startYear;
	
	private Date endYear;

	public Date getYear() {
		return year;
	}

	public Long getProcDefTypeId() {
		return procDefTypeId;
	}

	public void setProcDefTypeId(Long procDefTypeId) {
		this.procDefTypeId = procDefTypeId;
	}

	public Long getProcTempId() {
		return procTempId;
	}

	public void setProcTempId(Long procTempId) {
		this.procTempId = procTempId;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public Date getStartYear() {
		return startYear;
	}

	public void setStartYear(Date startYear) {
		this.startYear = startYear;
	}

	public Date getEndYear() {
		return endYear;
	}

	public void setEndYear(Date endYear) {
		this.endYear = endYear;
	}
}
