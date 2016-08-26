package com.ztesoft.uboss.bpm.test;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.i18n.I18NThreadContext;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ztesoft.uboss.bpm.runtime.client.BpmRuntimeService;
import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;


public class RuntimeServiceTest {
    private BpmRuntimeService runtimeService;
    
    @Before
    public void setUp() throws Exception{
        try {
            Session ses = SessionContext.currentSession();
            try{
                ses.beginTrans();
                ses.getConnection();
                ses.commitTrans();
            }
            finally{
                ses.releaseTrans();
            }
        } catch (BaseAppException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
             
        I18NThreadContext.setLocaleName("en_US");
        runtimeService = ProcessServiceManager.getManager().getRuntimeService();
    }

    @After
    public void afterTearDown(){
    }
    
    @Test
    public void testStartProcess(){
        try{
            String processDefinitionKey = "BFM_userTaskBack";
 
            String processInstanceId = runtimeService.startProcess(processDefinitionKey);
            System.out.println("Start process, instance id is " + processInstanceId);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
 
   
    
    @Test
    public void testCommitUserTask(){
        String executionId = "300001";
        try{
            String taskId = runtimeService.completeTaskByExecutionId(executionId,null);
            System.out.println("Commit task " + taskId);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
    
    @Test
    public void testBackUserTask(){
//        String executionId = "300001";
//        try{
//            String activityId = runtimeService.backUserTask(executionId);
//            System.out.println("Back to " + activityId);
// 
//        }catch(Throwable t){
//            t.printStackTrace();
//        }
    }
    
    @Test
    public void testSignal(){
        String executionId = "298401";
        try{
            runtimeService.signal(executionId,null);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
    
    @Test
    public void testExecuteServiceTask(){
        String executionId = "298701";
        try{
            boolean result = runtimeService.executeServiceTask(executionId);
            System.out.println("Execute result = " + result);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
    
    @Test
    public void testTerminateProcess(){
        String processInstanceId = "298701";
        String reason = "No reason";
        try{
            runtimeService.terminateProcess(processInstanceId, reason);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
}
