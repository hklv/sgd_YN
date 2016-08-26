package com.ztesoft.sgd.base.helper;

import com.ztesoft.sgd.base.enums.DbType;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.*;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunhao<br>
 * @version 1.0<br>
 */
public class JdbcUtil4SGD {

    private static ZSmartLogger logger = ZSmartLogger.getLogger(JdbcUtil4SGD.class);

    /**
     * Description:获取dap数据库连接 <br>
     *
     * @return <br>
     * @author li.xiaolong<br>
     * @taskId <br>
     */
    public static DbIdentifier getDefaultDbService() {
        return JdbcUtil.getDbBackService();
    }

    /**
     * Description: 实体转换为参数map
     *
     * @param entity 实体对象
     * @return ParamMap <br>
     */
    public static ParamMap entity2ParamMap(Object entity) {
        Class<?> clazz = entity.getClass();
        ParamMap params = new ParamMap();

        List<Field> fields = new ArrayList<Field>();
        // 获得类中所有申明的字段
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        // 处理父类
        if (clazz.getSuperclass() != null && !Object.class.equals(clazz.getSuperclass())) {
            fields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }

        BeanWrapperImpl bw = new BeanWrapperImpl(entity);
        for (Field f : fields) {
            Object value;
            try {
                value = bw.getPropertyValue(f.getName());
            } catch (Exception e) {
                continue;
            }

            int type = getType(f.getType());

            if (value != null && java.util.Date.class.equals(f.getType())) {
                value = new Timestamp(((java.util.Date) value).getTime());
            }

            params.set(f.getName(), type, value);
        }

        return params;
    }

    /**
     * Description: 取类型<br>
     *
     * @param clazz 类型
     * @return 类型 <br>
     */
    public static int getType(Class<?> clazz) {
        int type = ParamObject.TYPE_UNKNOWN;

        if (Integer.class.equals(clazz)) {
            type = ParamObject.TYPE_INTEGER;
        } else if (Long.class.equals(clazz)) {
            type = ParamObject.TYPE_LONG;
        } else if (BigDecimal.class.equals(clazz)) {
            type = ParamObject.TYPE_BIG_DECIMAL;
        } else if (String.class.equals(clazz)) {
            type = ParamObject.TYPE_STRING;
        } else if (Date.class.equals(clazz)) {
            type = ParamObject.TYPE_DATE;
        } else if (Timestamp.class.equals(clazz) || java.util.Date.class.equals(clazz)) {
            type = ParamObject.TYPE_TIMES_STAMP;
        } else if (Blob.class.equals(clazz)) {
            type = ParamObject.TYPE_BLOB;
        } else if (Clob.class.equals(clazz)) {
            type = ParamObject.TYPE_CLOB;
        } else if (BlobWrapper.class.equals(clazz)) {
            type = ParamObject.TYPE_BLOB_WRAPPER;
        } else if (ClobWrapper.class.equals(clazz)) {
            type = ParamObject.TYPE_CLOB_WRAPPER;
        }

        return type;
    }

    /**
     * Description: 设置字段参数
     *
     * @param paramMap   参数映射
     * @param paramName  参数名称
     * @param paramValue 参数值
     * @param fieldType  字段类型
     * @throws BaseAppException <br>
     */
    public static void setParamMap(ParamMap paramMap, String paramName, Object paramValue, Class<?> fieldType)
            throws BaseAppException {
        if (long.class.equals(fieldType) || Long.class.equals(fieldType)) {
            paramMap.set(paramName, (Long) paramValue);
        } else if (String.class.equals(fieldType)) {
            paramMap.set(paramName, (String) paramValue);
        } else if (Date.class.equals(fieldType)) {
            paramMap.set(paramName, (Date) paramValue);
        } else if (Timestamp.class.equals(fieldType)) {
            paramMap.set(paramName, (Timestamp) paramValue);
        } else if (java.util.Date.class.equals(fieldType)) {
            paramMap.set(paramName, new Timestamp(((java.util.Date) paramValue).getTime()));
        } else if (BigDecimal.class.equals(fieldType)) {
            paramMap.set(paramName, (BigDecimal) paramValue);
        } else if (int.class.equals(fieldType) || Integer.class.equals(fieldType)) {
            paramMap.set(paramName, (Integer) paramValue);
        } else if (BlobWrapper.class.equals(fieldType)) {
            paramMap.set(paramName, (BlobWrapper) paramValue);
        } else if (ClobWrapper.class.equals(fieldType)) {
            paramMap.set(paramName, (ClobWrapper) paramValue);
        } else {
            ExceptionHandler.publish("business dao do not support this param type:" + fieldType);
        }
    }

    /**
     * Description: 取对象的类型 <br>
     *
     * @param obj 类型对象
     * @return int
     */
    public static int getType(Object obj) {
        int type = ParamObject.TYPE_UNKNOWN;

        if (obj instanceof Integer) {
            type = ParamObject.TYPE_INTEGER;
        } else if (obj instanceof Long) {
            type = ParamObject.TYPE_LONG;
        } else if (obj instanceof BigDecimal) {
            type = ParamObject.TYPE_BIG_DECIMAL;
        } else if (obj instanceof String) {
            type = ParamObject.TYPE_STRING;
        } else if (obj instanceof Date) {
            type = ParamObject.TYPE_DATE;
        } else if (obj instanceof Timestamp || obj instanceof java.util.Date) {
            type = ParamObject.TYPE_TIMES_STAMP;
        } else if (obj instanceof Blob) {
            type = ParamObject.TYPE_BLOB;
        } else if (obj instanceof Clob) {
            type = ParamObject.TYPE_CLOB;
        } else if (obj instanceof BlobWrapper) {
            type = ParamObject.TYPE_BLOB_WRAPPER;
        } else if (obj instanceof ClobWrapper) {
            type = ParamObject.TYPE_CLOB_WRAPPER;
        }

        return type;
    }


    /**
     * Description: 将类中的字段转成数据库中的字段 格式：pkId --> pk_id --> PK_ID
     *
     * @param name camel大小写的字段
     * @return 下划线分隔的字段
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            for (int i = 0; i < name.length(); i++) {
                String tmp = name.substring(i, i + 1);
                // 判断截获的字符是否是大写，大写字母的toUpperCase()还是大写的
                if (tmp.equals(tmp.toUpperCase())) {
                    // 此字符是大写的
                    result.append("_").append(tmp.toLowerCase());
                } else {
                    result.append(tmp);
                }
            }
        }

        return StringUtils.upperCase(result.toString());
    }

    /**
     * Description: 将数据库中的字段转成类中的字段 格式：PK_ID --> pk_id --> pkId
     *
     * @param name 下划线分隔的字段
     * @return camel大小写的字段
     */
    public static String decodeUnderscoreName(String name) {
        name = StringUtils.lowerCase(name);
        StringBuffer result = new StringBuffer();
        boolean up = false;
        if (name != null && name.length() > 0) {
            // 转成小写
            name = name.toLowerCase();
            for (int i = 0; i < name.length(); i++) {
                // 取得i位置的字符
                String tmp = name.substring(i, i + 1);
                if ("_".equals(tmp)) {
                    up = true;
                    // 结束本次循环，进入下次循环
                    continue;
                }

                if (up) {
                    result.append(tmp.toUpperCase());
                    up = false;
                } else {
                    result.append(tmp);
                }
            }
        }

        return result.toString();
    }

    public static int executeSQL(String sql, ParamMap paramMap) throws BaseAppException {
        logger.debug("begin executeSQL!");
        Session ses = SessionContext.currentSession();
        ses.beginTrans();// 启动事务
        int result = JdbcTemplate.executeUpdate(getDefaultDbService(), sql, paramMap);
        logger.debug("end executeSQL!");
        ses.commitTrans();// 提交事务
        ses.releaseTrans();// 释放事务
        return result;
    }

    public static ResultSet executeQuery(String sql, ParamMap paramMap) throws BaseAppException {
        logger.debug("begin executeQuery!");
        Session ses = SessionContext.currentSession();
        ses.beginTrans();// 启动事务
        ResultSet resultSet = JdbcTemplate.query(getDefaultDbService(), sql, paramMap, null).getResultSet();
        logger.debug("end executeQuery!");
        ses.commitTrans();// 提交事务
        ses.releaseTrans();// 释放事务
        return resultSet;
    }

    public static DbType getDbType() throws BaseAppException {
        DbIdentifier identifier = getDefaultDbService();
        DbType dbType;
        Connection connection = null;
        try {
            connection = identifier.getConnectionProvider().getConnection();
            String database = connection.getMetaData().getDatabaseProductName();
            if (StringHelper.equalsIgnoreCase(database, DbType.DB_MYSQL.getName())) {
                dbType = DbType.DB_MYSQL;
            } else if (StringHelper.equalsIgnoreCase(database, DbType.DB_ORACLE.getName())) {
                dbType = DbType.DB_ORACLE;
            } else {
                throw new BaseAppException("S-SYS-00001", String.format("no support this database type '%s'!", database));
            }

            return dbType;
        } catch (SQLException e) {
            throw new BaseAppException("S-SYS-00000", "get connection error!");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
