package com.ztesoft.sgd.base.enums;

/**
 * 获取表的信息 <br>
 *
 * @author sunhao<br>
 * @version 1.0<br>
 */
public interface DbTable {
    /**
     * 获取表名
     *
     * @return String
     */
    String getTableName();

    /**
     * 获取主键名
     * 
     * @return String
     */
    String getIdColumnName();

    /**
     * 获取序列名
     *
     * @return String
     */
    String getSequenceName();
}
