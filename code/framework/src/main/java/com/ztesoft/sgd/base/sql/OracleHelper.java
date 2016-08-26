package com.ztesoft.sgd.base.sql;

import com.ztesoft.sgd.base.enums.DbType;
import com.ztesoft.sgd.base.id.IDGenerator;
import com.ztesoft.sgd.base.id.OracleIDGenerator;
import com.ztesoft.zsmart.core.exception.BaseAppException;

/**
 * oracle实现 <br>
 *
 * @author sunhao<br>
 * @version 1.0<br>
 */
public class OracleHelper extends AbstractSqlHelper {
    @Override
    public void setSpecialConfig(CommonConfigProperties configObject) {
        addConfigObject(configObject, path, "dialect", "com.ztesoft.zsmart.core.jdbc.dialect.OracleDialect");
        addConfigObject(configObject, path, "driver", "oracle.jdbc.driver.OracleDriver");
        addConfigObject(configObject, path, "linkCheckSql", "SELECT 1 FROM DUAL");
    }

    @Override
    public String existTableSql(String schema, String tableName) {
        return "SELECT COUNT(TABLE_NAME) FROM USER_TABLES WHERE TABLE_NAME = '" + tableName.toUpperCase() + "'";
    }

    @Override
    public DbType getDbType() {
        return DbType.DB_ORACLE;
    }

    @Override
    public String getPageSql(String sql, int offset, int limit) {
        if (offset < 0 || limit < 0)
            return sql;

        StringBuffer pageSql = new StringBuffer(" SELECT * FROM ( ");
        pageSql.append(" SELECT temp.* ,ROWNUM num FROM ( ");
        pageSql.append(sql);
        int last = offset + limit;
        pageSql.append(" ) temp where ROWNUM <= ").append(last);
        pageSql.append(" ) WHERE num > ").append(offset);

        return pageSql.toString();
    }

    @Override
    public IDGenerator getIDGenerator() throws BaseAppException {
        return new OracleIDGenerator();
    }
}
