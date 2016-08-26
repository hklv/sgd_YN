package org.activiti.uboss;

import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.uboss.cmd.ZExecuteExServiceTaskCmd;
import org.activiti.uboss.cmd.ZExecuteServiceTaskCmd;
import org.activiti.uboss.cmd.ZExecuteSignalServiceTaskCmd;
import org.activiti.uboss.cmd.ZUserTaskBackCommand;

public class ZRuntimeServiceImpl extends RuntimeServiceImpl {

	private final RuntimeServiceImpl runtimeServiceImpl;

	public ZRuntimeServiceImpl(RuntimeService runtimeService) {
		this.runtimeServiceImpl = (RuntimeServiceImpl) runtimeService;
 		setCommandExecutor(runtimeServiceImpl.getCommandExecutor());
	}

	public void userTaskBack(String processInstanceId, String executionId, Integer position) {
		userTaskBack(processInstanceId, executionId, position, null);
	}

	public void userTaskBack(String processInstanceId, String executionId, Integer position, Map<String, Object> variables) {
		commandExecutor.execute(new ZUserTaskBackCommand(processInstanceId, executionId, position, variables));
	}
	
	public boolean executeServiceTask(String processInstanceId, String executionId, String activityId){
	    return commandExecutor.execute(new ZExecuteServiceTaskCmd(processInstanceId, executionId, activityId));
	}
	
	public boolean executeExceptionServiceTask(String processInstanceId, String executionId, String exceptionTaskListId){
	    return commandExecutor.execute(new ZExecuteExServiceTaskCmd(processInstanceId, executionId, exceptionTaskListId));
	}
	
	public boolean executeSignalServiceTask(String executionId, String taskListId, Long userId){
	    return commandExecutor.execute(new ZExecuteSignalServiceTaskCmd(executionId, taskListId, userId));
	}
}
