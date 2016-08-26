package com.ztesoft.uboss.bpm.runtime.call;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;

import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;

/**
 * 启动流程
 * @author chen.gang71
 *
 */
public class StartCall extends AbstractCallable<String> {
    private String processDefinitionKey;
    private String businessKey;
    private Map<String, Object> variables = new HashMap<String, Object>();

    public StartCall(ServiceProvider sp) {
        super(sp);
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> vars) {
        this.variables.putAll(vars);
    }
    
    public void setVariable(String key, Object value) {
        this.variables.put(key, value);
    }

    public String doTask() {
        ProcessInstance procInst = sp.getRuntimeService().startProcessInstanceByKey(
                processDefinitionKey, businessKey, variables);
        return procInst.getProcessInstanceId();
    }
}
