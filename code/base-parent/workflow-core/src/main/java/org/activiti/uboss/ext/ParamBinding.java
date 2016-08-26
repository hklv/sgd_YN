package org.activiti.uboss.ext;


/**
 * serviceTask input 和output参数绑定
 * @author LiybC
 *
 */
public class ParamBinding {

	private String paramName;
	
	private String varName;
	
	private String varType;
	
	private String varInOut;
	
	private String defaultValue;
 
	public ParamBinding() {
 	}
	
	public ParamBinding(String paramName) {
		this.paramName = paramName;
 	}

	public String getParamName() {
		return paramName;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getVarType() {
		return varType;
	}

	public void setVarType(String varType) {
		this.varType = varType;
	}

	public String getVarInOut() {
		return varInOut;
	}

	public void setVarInOut(String varInOut) {
		this.varInOut = varInOut;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
