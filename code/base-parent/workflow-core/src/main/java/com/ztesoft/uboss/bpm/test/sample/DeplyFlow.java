package com.ztesoft.uboss.bpm.test.sample;

import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import junit.framework.TestCase;
import org.activiti.engine.ActivitiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class DeplyFlow extends TestCase {
    public static void main(String[] args) throws ActivitiException,
            FileNotFoundException {
//		File file = new File("c:\\sample1.bpmn");
//		ProcessServiceManager.getManager().getDeployService()
//				.deploy(file.getName(), new FileInputStream(file));


        File file = new File("d:\\Process_1.bpmn");
        ProcessServiceManager.getManager().getDeployService()
                .deploy(file.getName(), new FileInputStream(file));

    }

    public void testqryAllProcessDef() throws BaseAppException {
        //模拟界面session
        DynamicDict dict = new DynamicDict();
        DynamicDict uboss_session = new DynamicDict();
        uboss_session.set("user-id", 1);
        dict.set("zsmart_session", uboss_session);

        //设置服务名
        dict.serviceName = "BpmClientService";

        //设置服务定义方法
        dict.set("method", "qryAllProcessDef");
        //调用服务
        ServiceFlow.callService(dict, true);
    }

    public void testqryAllSimProcessVersion() throws BaseAppException {
        //模拟界面session
        DynamicDict dict = new DynamicDict();
        DynamicDict uboss_session = new DynamicDict();
        uboss_session.set("user-id", 1);
        dict.set("zsmart_session", uboss_session);

        //设置服务名
        dict.serviceName = "BpmClientService";

        //设置服务定义方法
        dict.set("method", "qryAllSimProcessVersion");
        //调用服务
        ServiceFlow.callService(dict, true);
    }
}
