package org.activiti.uboss.ext;

public class ZServiceDefinition extends CallableDefinition {
	private final String taskTemplateId;

	public ZServiceDefinition(String serviceName) {
		this(serviceName, null);
	}

	public ZServiceDefinition(String serviceName, String resultVariableName) {
		this(serviceName, resultVariableName, new ParamBinding[] {},
				new ParamBinding[] {});
	}

	public ZServiceDefinition(String taskTemplateId, String resultVariableName,
			ParamBinding[] inputs, ParamBinding[] outputs) {
		super(taskTemplateId, resultVariableName, inputs, outputs);
		this.taskTemplateId = taskTemplateId;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	@Override
	public String toString() {
		return "(" + ZServiceDefinition.class.getName()
				+ " : taskTemplateId = " + taskTemplateId
				+ " : resultVariableName = " + super.getResultVariableName()
				+ ")";
	}
}
