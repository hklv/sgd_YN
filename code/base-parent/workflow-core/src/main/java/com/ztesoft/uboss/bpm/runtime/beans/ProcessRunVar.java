package com.ztesoft.uboss.bpm.runtime.beans;

public class ProcessRunVar {

	private String varname;
	private String vartype;
	private String vartext;
	private Long vardate; 
	private double vardouble;
	
	public String getVarname() {
		return varname;
	}
	public void setVarname(String varname) {
		this.varname = varname;
	}
	public String getVartype() {
		return vartype;
	}
	public void setVartype(String vartype) {
		this.vartype = vartype;
	}
	public String getVartext() {
		return vartext;
	}
	public void setVartext(String vartext) {
		this.vartext = vartext;
	}
	public double getVardouble() {
		return vardouble;
	}
	public void setVardouble(double vardouble) {
		this.vardouble = vardouble;
	}
	public Long getVardate() {
		return vardate;
	}
	public void setVardate(Long vardate) {
		this.vardate = vardate;
	}
}
