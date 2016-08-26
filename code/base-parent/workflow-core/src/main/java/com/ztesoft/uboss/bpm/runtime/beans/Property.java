package com.ztesoft.uboss.bpm.runtime.beans;

import org.activiti.uboss.model.ZProperty;

public class Property {
	private final String id;
	private final String name;
	private final String variable;
	private final Object defaultValue;

	private final String type;
	private final boolean writable;
	private final boolean required;

	public Property(ZProperty zProperty) {
		this.id = zProperty.getId();
		this.name = zProperty.getName();
		this.variable = zProperty.getVariable();
		this.defaultValue = zProperty.getDefaultValue();

		this.type = zProperty.getType();
		this.writable = zProperty.isWritable();
		this.required = zProperty.isRequired();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVariable() {
		return variable;
	}

	public String getType() {
		return type;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public boolean isWritable() {
		return writable;
	}

	public boolean isRequired() {
		return required;
	}
}
