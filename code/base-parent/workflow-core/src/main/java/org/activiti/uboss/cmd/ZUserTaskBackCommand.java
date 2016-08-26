package org.activiti.uboss.cmd;

import java.io.Serializable;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.uboss.ZExtendsionUtil;

public class ZUserTaskBackCommand implements Serializable, Command<Void> {

	private static final long serialVersionUID = 1L;

	protected final String processInstanceId;
	protected final String executionId;
	protected final Integer position;
	protected final Map<String, Object> variables;

    public ZUserTaskBackCommand(String processInstanceId, String executionId,
            Integer position, Map<String, Object> variables) {
		this.processInstanceId = processInstanceId;
		this.executionId = executionId;
		this.position = position;
		this.variables = variables;
	}

	public Void execute(CommandContext commandContext) {

		ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(executionId);

		ExecutionEntity target = (ExecutionEntity)execution;
		if (target == null) {
			throw new ActivitiException("Cannot find a valid user task for backing process " + processInstanceId);
		}
		//回退位置
        target.setVariableLocal(ZExtendsionUtil.VARIABLE_POSITION, position);
		target.performOperation(ZExtendsionUtil.TRANSITION_USER_TASK_BACK);		
		return null;
	}

}
