package com.ztesoft.uboss.bpm.setup.model;


import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;

public class ButtonQueryCondition{
	
	private String btnId;
	
	private long formId;
	
	private long btnName;
	
	private String comments;
	
	private String state;
	
	private String stateDate;
	
	private RowSetFormatter rowSetFormatter;

	public String getBtnId() {
		return btnId;
	}

	public void setBtnId(String btnId) {
		this.btnId = btnId;
	}

	public long getFormId() {
		return formId;
	}

	public void setFormId(long formId) {
		this.formId = formId;
	}

	public long getBtnName() {
		return btnName;
	}

	public void setBtnName(long btnName) {
		this.btnName = btnName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDate() {
		return stateDate;
	}

	public void setStateDate(String stateDate) {
		this.stateDate = stateDate;
	}

	public RowSetFormatter getRowSetFormatter() {
		return rowSetFormatter;
	}

	public void setRowSetFormatter(RowSetFormatter rowSetFormatter) {
		this.rowSetFormatter = rowSetFormatter;
	}
}
