package org.activiti.uboss.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.task.TaskDefinition;

public class ZTaskDefinition extends TaskDefinition {

	private String taskTemplateId = null;
	private final List<ParamBinding> inputs = new ArrayList<ParamBinding>();

	private final List<ParamBinding> outputs = new ArrayList<ParamBinding>();

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	public void setTaskTemplateId(String taskTemplateId) {
		this.taskTemplateId = taskTemplateId;
	}

	public ZTaskDefinition(TaskFormHandler taskFormHandler) {
		super(taskFormHandler);
	}

	public List<ParamBinding> getInputs() {
		return Collections.unmodifiableList(inputs);
	}

	public List<ParamBinding> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}

}
