package org.activiti.uboss.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author LiybC
 *
 */
public abstract class CallableDefinition {

	private final String resultVariableName;

	private final String taskTemplateId;

	private final List<ParamBinding> inputs = new ArrayList<ParamBinding>();

	private final List<ParamBinding> outputs = new ArrayList<ParamBinding>();

	public CallableDefinition(String taskTemplateId, String resultVariableName,
			ParamBinding[] inputs, ParamBinding[] outputs) {
		this.taskTemplateId = taskTemplateId;
		this.resultVariableName = resultVariableName;
		adds(this.inputs, inputs);
		adds(this.outputs, outputs);
	}

	private void adds(List<ParamBinding> l, ParamBinding[] ps) {
		if (ps == null) {
			return;
		}
		for (ParamBinding p : ps) {
			l.add(p);
		}
	}

	public String getResultVariableName() {
		return resultVariableName;
	}

	public String getTaskTemplateId() {
		return taskTemplateId;
	}

	public List<ParamBinding> getInputs() {
		return Collections.unmodifiableList(inputs);
	}

	public List<ParamBinding> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}
}
