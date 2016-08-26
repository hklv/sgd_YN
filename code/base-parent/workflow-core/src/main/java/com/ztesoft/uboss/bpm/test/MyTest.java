package com.ztesoft.uboss.bpm.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import org.activiti.engine.ActivitiException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;


public class MyTest extends BaseTransactionTest {

	@Before
	public void setUp() {
		try {
			Session ses = SessionContext.currentSession();
			try {
				ses.beginTrans();
//				ses.getConnection();
				ses.commitTrans();
			} finally {
				ses.releaseTrans();
			}
		} catch (BaseAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@After
	public void tearDown() {

	}

	
    @Test
    public void testAddBPM() throws ActivitiException, FileNotFoundException{
    	  ProcessServiceManager.getManager().getDeployService().deploy("MyTestFlow.bpmn",new FileInputStream("c:\\MyTestFlow.bpmn"));
 
     }

    @Test
    public void testAddBPMNew() throws ActivitiException, FileNotFoundException{
    	
		try {
			Session ses = SessionContext.currentSession();
			try {
				ses.beginTrans();
				  ProcessServiceManager.getManager().getDeployService().deploy("NewBPMN.bpmn20.xml");
				  boolean b =ProcessServiceManager.getManager().getDeployService().hasProcessDef("Process_1");
				  System.out.println("is Exist = "+b);
				ses.commitTrans();
			} finally {
				ses.releaseTrans();
			}
		} catch (BaseAppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
     } 
    
    
    @Test
    public void testStartFlow(){
    	ProcessServiceManager.getManager().getRuntimeService().startProcess("process1");
     }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
