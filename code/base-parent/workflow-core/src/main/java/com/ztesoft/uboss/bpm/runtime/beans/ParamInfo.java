package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.uboss.ext.ParamBinding;

public class ParamInfo {
	private final String paramName;

	public ParamInfo(ParamBinding b) {
		paramName = b.getParamName();

	}

}
