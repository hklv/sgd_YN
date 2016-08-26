package com.ztesoft.uboss.bpm.runtime.client;

public enum ActivityType {
	UserTask("userTask"),
	ServiceTask("serviceTask"),
	BusinessRuleTask("businessRuleTask"),
	SubProcess("subProcess"),
	CallActivity("callActivity"),
	UnKnown("unknown");
	
	private final String typeName;
	private ActivityType(String typeName){
	    this.typeName = typeName;
	}
	
	public String getTypeName(){
	    return this.typeName;
	}
}
