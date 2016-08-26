package com.ztesoft.uboss.bpm.test.sample;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import junit.framework.TestCase;


public class FlowInstanceRunTime extends TestCase{
	
	public void testSuspendProcess() throws BaseAppException {
		
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);

		//设置服务名
		dict.serviceName = "BpmClientService";
		//流程实例ID
		dict.set("PROCESS_INSTANCE_ID", "8301");
		//挂起原因
		dict.set("REASON", "test suspend");
		//设置服务定义方法
		dict.set("method", "suspendProcess");
		//调用服务
		ServiceFlow.callService(dict,true);
	}

	public void testUnSuspendProcess() throws BaseAppException{
	
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);
	
		//设置服务名
		dict.serviceName = "BpmClientService";
		//流程实例ID
		dict.set("PROCESS_INSTANCE_ID", "8301");
		//挂起原因
		dict.set("REASON", "test unsuspend");
		//设置服务定义方法
		dict.set("method", "unSuspendProcess");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
	
	public void testTerminateProcess() throws BaseAppException{
		
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);
	
		//设置服务名
		dict.serviceName = "BpmClientService";
		//流程实例ID
		dict.set("PROCESS_INSTANCE_ID", "8301");
		//停止原因
		dict.set("REASON", "test terminate");
		//设置服务定义方法
		dict.set("method", "terminateProcess");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
}
