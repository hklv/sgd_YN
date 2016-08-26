package com.ztesoft.uboss.bpm.test.sample;

import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;
import com.ztesoft.uboss.bpm.test.sample.processvar.AA;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

public class StartFlow {

    public String startSample1() throws BaseAppException {
        Map<String, Object> variables1 = new HashMap<String, Object>();
        variables1.put("task_v111", "p_task_v111");
        variables1.put("process_v2", "p2");
        String processInstId = ProcessServiceManager.getManager()
                .getRuntimeService().startProcess("sample1", variables1);
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            System.out.println("==processInstId====:" + processInstId);
            ServiceProvider sp = ProcessServiceManager.getManager()
                    .getServiceProvider();
            Task task = sp.getTaskService().createTaskQuery()
                    .executionId(processInstId).singleResult();

            if (task != null) {
                Map<String, Object> variables = new HashMap<String, Object>();
                variables.put("task_v111", "t_task_v111");
                variables.put("task_v2", "t2");
                variables.put("task_v3", new AA());
                sp.getTaskService().setVariablesLocal(task.getId(), variables);
//				sp.getTaskService().complete(task.getId(), variables);
            }
            s.commitTrans();
        } finally {
            s.releaseTrans();
        }
        return processInstId;
    }

    public String startSample2() throws BaseAppException {

        String processInstanceId = null;

        try {
            SessionContext.newSession().beginTrans();

            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("a", "1");
            variables.put("b", "1");

            processInstanceId = ProcessServiceManager.getManager()
                    .getRuntimeService().startProcess("sample2", variables);

            SessionContext.currentSession().commitTrans();
        } finally {
            if (SessionContext.existCurrentSession()) {
                SessionContext.currentSession().releaseTrans();
            }
        }

        return processInstanceId;
    }

    /**
     * @param args
     * @throws BaseAppException
     */
    public static void main(String[] args) throws BaseAppException {

        DynamicDict dict = new DynamicDict();
        //设置服务名
        dict.serviceName = "BpmClientService";
        //设置流程定义KEY
        dict.set("PROC_BIZ_KEY", "TEST1");
        //设置服务定义方法
        dict.set("method", "startProcessByBizKey");
        //调用服务
        ServiceFlow.callService(dict, false);
        //获取流程实例单号
        String processHolderNo = dict.getString("PROCESS_HOLDER_NO");

        System.out.println("process " + "" + "start sucessful: " + processHolderNo);

    }

}
