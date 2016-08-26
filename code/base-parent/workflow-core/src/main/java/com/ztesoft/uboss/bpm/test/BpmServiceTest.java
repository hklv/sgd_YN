package com.ztesoft.uboss.bpm.test;

import java.util.HashMap;
import java.util.Map;

import com.ztesoft.uboss.bpm.runtime.BpmServiceImpl;
import com.ztesoft.uboss.bpm.runtime.IBpmService;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.IAction;
import utils.UbossActionSupport;


public class BpmServiceTest extends UbossActionSupport {

	IBpmService bpmService = BpmServiceImpl.getInstance();
	
	public int startFlow(DynamicDict dict) throws BaseAppException
	{
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("a", "1");
		variables.put("b", "1");
		
		bpmService.startProcess(dict.getString("PROCESS_DEFINITION_ID"), variables);
		
		return 0;
	}

	public int checkOutTask(DynamicDict dict) throws BaseAppException
	{
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("a", "1");
		variables.put("b", "1");
		
		//bpmService.claimTask(dict.getString("TASK_ID"), Long.valueOf(12345), variables);
		
		return 0;
	}
	
	public int completeTask(DynamicDict dict) throws BaseAppException
	{
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("a", "1");
		variables.put("b", "1");
		
		//bpmService.completeTask(dict.getString("TASK_ID"), Long.valueOf(12345), variables);
		
		return 0;
	}
}
