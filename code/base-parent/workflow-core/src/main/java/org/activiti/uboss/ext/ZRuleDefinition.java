package org.activiti.uboss.ext;

public class ZRuleDefinition extends CallableDefinition {

	private final String taskTemplateId;

	public ZRuleDefinition(String taskTemplateId) {
		this(taskTemplateId, null);
	}

	public ZRuleDefinition(String taskTemplateId, String resultVariableName) {
		super(taskTemplateId,resultVariableName, new ParamBinding[] {}, new ParamBinding[] {});
		this.taskTemplateId = taskTemplateId;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	@Override
	public String toString() {
		return "(" + ZRuleDefinition.class.getName() + " : taskTemplateId = "
				+ taskTemplateId + " : resultVariableName = "
				+ super.getResultVariableName() + ")";
	}
}
