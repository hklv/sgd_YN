package com.ztesoft.uboss.bpm.test.sample;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import junit.framework.TestCase;

public class DynControlUserTask extends TestCase{

	public void testJumpTask() throws BaseAppException {
		
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);

		//设置服务名
		dict.serviceName = "WorkItemService";
		//当前任务
		dict.set("TASK_LIST_ID", "e905110a-de88-4a95-b9a2-6be6a38fc8fc");
		//跳跃目标环节
		dict.set("NEXT_ACTIVITY_ID", "pro37");
		dict.set("JUMP_REASON", "test jump");
		
		//设置服务定义方法
		dict.set("method", "jumpTask");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
	
	public void testForwardTask() throws BaseAppException{
		
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);

		//转派人
		DynamicDict forwardUser = new DynamicDict();
		forwardUser.set("USER_ID", 1);
		dict.set("ASSGIN_IFNO", forwardUser);
		
		//设置服务名
		dict.serviceName = "WorkItemService";
		//转派的任务
		dict.set("TASK_LIST_ID", "6af94803-71df-45f5-8964-4b77263d7260");
		dict.set("FORWARD_REASON", "test forward");
		
		//设置服务定义方法
		dict.set("method", "forwardTask");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
	
	public void testCheckOutTask() throws BaseAppException{
		
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);
		
		//设置服务名
		dict.serviceName = "WorkItemService";
		//签出的任务
		dict.set("TASK_LIST_ID", "6af94803-71df-45f5-8964-4b77263d7260");
		
		//设置服务定义方法
		dict.set("method", "claimTask");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
	
	public void testBackUserTask() throws BaseAppException{
		
		//模拟界面session
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);
		
		//设置服务名
		dict.serviceName = "WorkItemService";
		//当前任务ID
		dict.set("TASK_LIST_ID", "cdf54ea9-bac5-4fcf-aefa-d101ddbd742f");
		//回退的任务ID
		dict.set("BACK_TASK_LIST_ID", "6af94803-71df-45f5-8964-4b77263d7260");
		
		dict.set("BACK_REASON", "test forward");
		
		//设置服务定义方法
		dict.set("method", "backUserTask");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
}
