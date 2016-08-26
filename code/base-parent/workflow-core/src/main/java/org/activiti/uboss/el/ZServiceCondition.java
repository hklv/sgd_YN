package org.activiti.uboss.el;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.Condition;

import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;

public class ZServiceCondition implements Condition {

	private String serviceName;
	
	private String methodName;
	
	public ZServiceCondition(String serviceName, String methodName)
	{
		this.serviceName = serviceName;
		this.methodName = methodName;
	}
	
	public boolean evaluate(DelegateExecution execution) {
		
		Map<String, Object> variables = execution.getVariablesLocal();
		DynamicDict dict = new DynamicDict();
		dict.valueMap = (HashMap<String, Object>)variables;
		dict.serviceName = serviceName;
		
		try
		{
			dict.set("method", methodName);
			ServiceFlow.callService(dict);
			return dict.getBoolean("CONDITION_RESULT");
		}
		catch(Exception e)
		{
			throw new ActivitiException("执行服务条件 错误", e);
		}
	}

}
