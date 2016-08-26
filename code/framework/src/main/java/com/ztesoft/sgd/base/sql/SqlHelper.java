package com.ztesoft.sgd.base.sql;

import com.ztesoft.sgd.base.enums.DbType;
import com.ztesoft.sgd.base.id.IDGenerator;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;

/**
 * 动态连接数据库 <br>
 *
 * @author sunhao<br>
 * @version 1.0 15/5/6<br>
 */
public interface SqlHelper {
    /**
     * 获取数据库类型
     *
     * @return 数据库类型
     */
    DbType getDbType();

    /**
     * 获取数据库dbID
     *
     * @param connectionUrl 连接串
     * @param userName      用户名
     * @param password      密码
     * @return DbIdentifier
     * @throws BaseAppException <br>
     */
    DbIdentifier getDbId(String connectionUrl, String userName, String password) throws BaseAppException;

    /**
     * 获得查询所有条数的sql
     *
     * @param sql 原sql
     * @return
     */
    String getCountSql(String sql);

    /**
     * 获得分页的sql
     * 由于各个数据库分页语句不同，故让子类自己实现此方法
     *
     * @param sql    原sql
     * @param offset 偏移量
     * @param limit  数量
     * @return
     */
    String getPageSql(String sql, int offset, int limit);

    /**
     * 判断数据库中是否含有给定的表
     *
     * @param tableName 表名
     * @return
     */
    boolean existTable(String tableName) throws Exception;

    /**
     * 获取主键生成器
     *
     * @return 键生成器
     * @throws Exception
     */
    IDGenerator getIDGenerator() throws BaseAppException;
}
