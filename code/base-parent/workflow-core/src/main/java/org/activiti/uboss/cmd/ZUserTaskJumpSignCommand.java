package org.activiti.uboss.cmd;

import java.io.Serializable;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class ZUserTaskJumpSignCommand implements Serializable, Command<Void> {

	private static final long serialVersionUID = 1L;

	protected final ExecutionEntity execution;

    public ZUserTaskJumpSignCommand (ExecutionEntity execution) {
		this.execution = execution;
	}

	public Void execute(CommandContext commandContext) {
		execution.setVariableLocal("isjump", "y");
		
		return null;
	}

}
