package com.ztesoft.uboss.bpm.runtime.call;

import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;

import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;

/**
 * 终止流程
 * @author chen.gang71
 *
 */
public class TerminateCall extends AbstractCallable<Void> {
    private String processInstanceId;
    private String reason;

    public TerminateCall(ServiceProvider sp) {
        super(sp);
    }
    
    public void setProcessInstanceId(String processInstanceId){
        this.processInstanceId = processInstanceId;
    }
    
    public void setReason(String reason){
        this.reason = reason;
    }

    @Override
    public Void doTask() {
        ProcessInstanceQuery query = 
            sp.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId);
        List<ProcessInstance> instList = query.list();
        if(instList != null && instList.size() > 0){
            sp.getRuntimeService().deleteProcessInstance(processInstanceId, reason);
        }
        return null;
    }
}
