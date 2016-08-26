package com.ztesoft.uboss.bpm.runtime.beans;

import java.util.Map;

import com.ztesoft.uboss.bpm.runtime.client.ExecuteMode;
import com.ztesoft.uboss.bpm.runtime.listener.AbstractProcessObserver;

/**
 * 流程参数设置
 * 
 * @author chen.gang71
 */
public class BpmParameter {
	private String executionId;
	private String taskId;
	private String processDefinitionKey;

	private ExecuteMode executeMode = ExecuteMode.EXECUTE_AND_WAIT;
	private long timeoutMillis = 0L;
	private AbstractProcessObserver[] observerList;
	private Map<String, Object> variables;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public ExecuteMode getExecuteMode() {
		return executeMode;
	}

	public void setExecuteMode(ExecuteMode executeMode) {
		this.executeMode = executeMode;
	}

	public long getTimeoutMillis() {
		return timeoutMillis;
	}

	public void setTimeoutMillis(long timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	public AbstractProcessObserver[] getObserverList() {
		return observerList;
	}

	public void setObserverList(AbstractProcessObserver[] observerList) {
		this.observerList = observerList;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
}
