package com.ztesoft.uboss.bpm.runtime.call;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.uboss.ZExtendsionUtil;

import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;

public class UserTaskBackCall extends AbstractCallable<Void> {
	private String executionId;
	private Integer position;

	public UserTaskBackCall(ServiceProvider sp) {
		super(sp);
	}
	
    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

	public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
	public Void doTask() {
		RuntimeService runtimeService = sp.getRuntimeService();
		Execution execution = ZExtendsionUtil.assertSingleExecution(runtimeService, null, executionId);
		ZExtendsionUtil.userTaskBack(runtimeService, null, execution.getId(), position);
		return null;
	}
}
