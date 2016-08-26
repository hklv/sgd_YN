package com.ztesoft.uboss.bpm.test.sample;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import junit.framework.TestCase;

public class CompleteUserTask extends TestCase{

	public void testCommitTask() throws BaseAppException {
		
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);

		//设置服务名
		dict.serviceName = "WorkItemService";
		//完成的任务ID
		dict.set("TASK_LIST_ID", "af066395-4e17-4cbc-858c-2f05010b30de");
		//本地变量，只有当前环节可以访问
		DynamicDict lvar = new DynamicDict();
		lvar.set("TASK_RESULT", "taskResult");
		//全局变量，整个流程都可以访问，并且需要在流程定义时的流程全局变量里面定义
		DynamicDict gvar = new DynamicDict();
		gvar.set("PARAM1", "PARAMI");
		dict.set("LVAR",lvar);
		dict.set ("GVAR",gvar);
		//设置服务定义方法
		dict.set("method", "completeTask");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
	
	public void testAssignNextTaskUser() throws BaseAppException{
		
		DynamicDict dict = new DynamicDict();
		DynamicDict uboss_session = new DynamicDict();
		uboss_session.set("user-id", 1);
		dict.set("zsmart_session", uboss_session);

		//设置服务名
		dict.serviceName = "WorkItemService";
		//完成的任务ID
		dict.set("TASK_LIST_ID", "54efbd9f-fbb6-473d-b4a4-a05ad53d7c45");
		//本地变量，只有当前环节可以访问
		DynamicDict lvar = new DynamicDict();
		lvar.set("TASK_RESULT", "taskResult");
		//全局变量，整个流程都可以访问，并且需要在流程定义时的流程全局变量里面定义
		DynamicDict gvar = new DynamicDict();
		//动态指定下一个环节的处理人
		gvar.set("bpm_user_id", "1");
		dict.set("LVAR",lvar);
		dict.set ("GVAR",gvar);
		//设置服务定义方法
		dict.set("method", "completeTask");
		//调用服务
		ServiceFlow.callService(dict,true);
	}
}
