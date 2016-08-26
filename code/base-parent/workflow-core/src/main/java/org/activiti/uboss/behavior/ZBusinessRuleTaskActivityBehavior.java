package org.activiti.uboss.behavior;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.BusinessRuleTaskActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.uboss.bpm.utils.RuleUtil;

public class ZBusinessRuleTaskActivityBehavior extends
		BusinessRuleTaskActivityBehavior {
	
	public void execute(ActivityExecution execution) throws Exception {
		
		List<Object> params = new ArrayList<Object>();
		List<String> ruleCodes = new ArrayList<String>();
		
		if (variablesInputExpressions != null) {
			Iterator<Expression> itVariable = variablesInputExpressions
					.iterator();
			while (itVariable.hasNext()) {
				Expression variable = itVariable.next();
				params.add(variable.getValue(execution));
			}
		}
		
		if (rulesExpressions.size() > 0) {

			Iterator<Expression> itRuleNames = rulesExpressions.iterator();
			while (itRuleNames.hasNext()) {
				Expression ruleName = itRuleNames.next();
				ruleCodes.add(ruleName.getValue(execution).toString());
			}
		}
		
		if(ruleCodes.isEmpty()){
			return;
		}
		
		DynamicDict dict = new DynamicDict();
		
		Map<String, Object> obj = execution.getVariables();
		if(obj != null){
			
			Iterator<Map.Entry<String, Object>> entry = obj.entrySet().iterator();
			while(entry.hasNext()){
				
				Map.Entry<String, Object> item = entry.next();
				dict.set(item.getKey(), item.getValue());
			}
		}
		
		for(String ruleCode : ruleCodes){
			
			Collection<Object> outputVariables = RuleUtil.execRule(ruleCode, params, dict);
			
			if (outputVariables != null && outputVariables.size() > 0) {
				
				execution.setVariable(resultVariable, outputVariables);
			}
			
			HashMap<String, Object> map = dict.valueMap;
			Iterator<Map.Entry<String, Object>> entry = map.entrySet().iterator();
			while(entry.hasNext()){
				
				Map.Entry<String, Object> item = entry.next();
				execution.setVariable(item.getKey(), item.getValue());
			}
		}
		
		//插入工单表
		this.doSysTask(execution, null, false);
 		leave(execution);
	}

}
