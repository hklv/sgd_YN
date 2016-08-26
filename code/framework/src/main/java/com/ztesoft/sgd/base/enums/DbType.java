package com.ztesoft.sgd.base.enums;

import com.ztesoft.zsmart.core.utils.AssertUtil;

/**
 * 表示数据库类型的枚举 <br>
 *
 * @author sunhao<br>
 * @version 1.0<br>
 */
public enum DbType {
    /**
     * 数据库类型
     */
    DB_ORACLE("O", "Oracle"), DB_MYSQL("M", "MySQL");

    /**
     * DbType
     *
     * @param dbType String
     * @param name   String
     */
    DbType(String dbType, String name) {
        this.dbType = dbType;
        this.name = name;
    }

    /**
     * dbType
     */
    private String dbType;

    /**
     * name
     */
    private String name;

    /**
     * Description: <br>
     *
     * @return String
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Description: <br>
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Description: <br>
     *
     * @param eval_ String
     * @return <br>
     */
    public static DbType eval(String eval_) {
        AssertUtil.hasLength(eval_, "eval is required!");

        for (DbType dt : DbType.values()) {
            if (eval_.equals(dt.getDbType())) {
                return dt;
            }
        }

        return null;
    }
}
