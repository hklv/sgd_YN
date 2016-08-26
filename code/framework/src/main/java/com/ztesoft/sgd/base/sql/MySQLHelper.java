package com.ztesoft.sgd.base.sql;

import com.ztesoft.sgd.base.enums.DbType;
import com.ztesoft.sgd.base.id.IDGenerator;
import com.ztesoft.sgd.base.id.MySQLIDGenerator;
import com.ztesoft.zsmart.core.exception.BaseAppException;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/17 下午3:41
 */
public class MySQLHelper extends AbstractSqlHelper {
    @Override
    public void setSpecialConfig(CommonConfigProperties configObject) {
        addConfigObject(configObject, path, "dialect", "com.ztesoft.zsmart.core.jdbc.dialect.MySqlDialect");
        addConfigObject(configObject, path, "driver", "com.mysql.jdbc.Driver");
        addConfigObject(configObject, path, "linkCheckSql", "SELECT 1");
    }

    @Override
    public String existTableSql(String schema, String tableName) {
        StringBuilder sql = new StringBuilder("SELECT count(TABLE_NAME) count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='");
        sql.append(schema).append("' and TABLE_NAME = '").append(tableName).append("'");

        return sql.toString();
    }

    @Override
    public DbType getDbType() {
        return DbType.DB_MYSQL;
    }

    @Override
    public String getPageSql(String sql, int offset, int limit) {
        StringBuilder sqlBuilder = new StringBuilder(sql);

        if (offset <= 0) {
            return sqlBuilder.append(" limit ").append(limit).toString();
        }
        return sqlBuilder.append(" limit ").append(offset).append(",").append(limit).toString();
    }

    @Override
    public IDGenerator getIDGenerator() throws BaseAppException {
        return new MySQLIDGenerator();
    }
}
