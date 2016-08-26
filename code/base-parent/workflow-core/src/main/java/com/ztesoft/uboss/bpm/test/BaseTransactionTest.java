package com.ztesoft.uboss.bpm.test;

import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import org.junit.After;
import org.junit.Before;


/**
 * 存在数据库操作的Junit测试基类，所有数据库操作会被回退
 * @author wang.xiang23
 * @since 2011-8-25
 */
public abstract class BaseTransactionTest {

	private Session session;

	public BaseTransactionTest() {
		super();
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void beforeSetUp() throws Exception {
		session = SessionContext.newSession();
		session.beginTrans();
		setUp();
	}
	
	protected abstract void setUp();
	protected abstract void tearDown();
	/**
	 * @throws Exception
	 */
	@After
	public void afterTearDown() throws Exception {
		if(session != null)
			session.releaseTrans();
	}

}