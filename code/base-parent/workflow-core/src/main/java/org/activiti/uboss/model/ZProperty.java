package org.activiti.uboss.model;

public class ZProperty {
	private final String id;
	private final String name;
	private final String variable;
	private final Object defaultValue;

	private final String type;
	private final boolean writable;
	private final boolean required;

	public ZProperty(String id, String name, String variable, String defaultValue, String type, String writable, String required) {
		this.id = id;
		this.name = name;
		this.variable = variable;
		this.defaultValue = defaultValue;

		this.type = type;
		if (writable == null) {
			this.writable = true;
		} else {
			this.writable = Boolean.parseBoolean(writable);
		}
		if (required == null) {
			this.required = false;
		} else {
			this.required = Boolean.parseBoolean(required);
		}
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
