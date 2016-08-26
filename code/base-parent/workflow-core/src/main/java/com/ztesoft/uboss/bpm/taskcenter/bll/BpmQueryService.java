package com.ztesoft.uboss.bpm.taskcenter.bll;

import java.util.List;
import java.util.Map;

import com.ztesoft.uboss.bpm.runtime.BpmServiceImpl;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;


public class BpmQueryService {

	private static BpmServiceImpl serviceimpl = BpmServiceImpl.getInstance();
	
	/**
	 * 查询指定流程实例下的所有活动
	 * 
	 * @param dict
	 * @throws BaseAppException
	 */
	public void queryActivityInstance(DynamicDict dict) throws BaseAppException {
		String processInstanceId = dict.getString("PROCESS_INSTANCE_ID");
		List<Map<String, Object>> retVal = serviceimpl
				.queryActivityInstance(processInstanceId);
		dict.set("INSTANCE_LIST", retVal);
	}

	/**
	 * 查询流程实例
	 * 
	 * @param dict
	 * @throws BaseAppException
	 */
	public void queryProcessInstance(DynamicDict dict)
			throws BaseAppException {
		String processInstanceId = dict.getString("PROCESS_INSTANCE_ID");
		String isQueryFinished = dict.getString("IS_QUERY_FINISHED");
		List<Map<String, Object>>  retVal = serviceimpl.queryProcessIntances(processInstanceId,isQueryFinished);
		dict.set("INSTANCE_LIST", retVal);
	}
}
