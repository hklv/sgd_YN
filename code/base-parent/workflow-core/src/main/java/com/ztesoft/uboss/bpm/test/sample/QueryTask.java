package com.ztesoft.uboss.bpm.test.sample;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import junit.framework.TestCase;

public class QueryTask extends TestCase {
	
	public void testQryTaskList() throws BaseAppException
	{
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);

		//设置服务名
		dict.serviceName = "WorkItemService";
		//完成的任务ID
		dict.set("USER_ID", "1");
		//设置服务定义方法
		dict.set("method", "qryTaskList");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
}
