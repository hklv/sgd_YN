package com.ztesoft.sgd.base.id;

import com.ztesoft.sgd.base.sql.SqlHelperFactory;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.junit.Test;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/18 上午9:50
 */
public class IDGeneratorTest {
    @Test
    public void testIDGenerator() throws Exception {
        IDGenerator idGenerator = new MySQLIDGenerator();
        System.out.println("id=" + idGenerator.nextId("test"));
    }

    @Test
    public void testSqlHelperFactory() throws BaseAppException {
        System.out.println(SqlHelperFactory.getSqlHelper());
    }
}

