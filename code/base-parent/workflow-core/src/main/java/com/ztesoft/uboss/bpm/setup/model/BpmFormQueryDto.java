package com.ztesoft.uboss.bpm.setup.model;


import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;

public class BpmFormQueryDto {
	private String formName;
	
	private String formType;
	
	private String applyType;
	
	private String catgId;
	
	private RowSetFormatter rowSetFormatter;

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public RowSetFormatter getRowSetFormatter() {
		return rowSetFormatter;
	}

	public void setRowSetFormatter(RowSetFormatter rowSetFormatter) {
		this.rowSetFormatter = rowSetFormatter;
	}
	
	public String getCatgId() {
		return catgId;
	}

	public void setCatgId(String catgId) {
		this.catgId = catgId;
	}
	
	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
}
