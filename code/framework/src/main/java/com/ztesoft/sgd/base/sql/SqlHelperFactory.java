package com.ztesoft.sgd.base.sql;

import com.ztesoft.sgd.base.enums.DbType;
import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.zsmart.core.exception.BaseAppException;

/**
 * 动态数据库连接获取的工厂类 <br>
 *
 * @author sunhao<br>
 * @version 1.0<br>
 */
public abstract class SqlHelperFactory {

    /**
     * 获取默认的SqlHelper
     *
     * @return SqlHelper
     */
    public static SqlHelper getSqlHelper() throws BaseAppException {
        DbType dbType = JdbcUtil4SGD.getDbType();

        return getSqlHelper(dbType);
    }

    /**
     * 工厂类根据数据库类型获取SqlHelper
     *
     * @param dbType String
     * @return SqlHelper
     */
    public static SqlHelper getSqlHelper(String dbType) {
        if (DbType.DB_ORACLE.getDbType().equals(dbType)) {
            return new OracleHelper();
        } else if (DbType.DB_MYSQL.getDbType().equals(dbType)) {
            return new MySQLHelper();
        }

        return null;
    }


    /**
     * Description: 工厂类根据数据库类型获取SqlHelper
     *
     * @param dbType DbType
     * @return SqlHelper
     */
    public static SqlHelper getSqlHelper(DbType dbType) {
        return getSqlHelper(dbType.getDbType());
    }
}
