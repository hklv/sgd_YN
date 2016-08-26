package com.ztesoft.sgd.base.factory;

import com.ztesoft.sgd.base.enums.DbType;
import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.BaseDAO;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;


/**
 * <Description> <br>
 *
 * @author sunhao<br>
 * @version 1.0<br>
 */
public final class SgdDaoFactory {
    private static final String PACKAGE_PATH_IMPL = ".impl";
    private static final String CLASS_IMPL = "Impl";
    private static final String CLASS_NAME_ORACLE = "Oracle" + CLASS_IMPL;
    private static final String CLASS_NAME_MYSQL = "MySQL" + CLASS_IMPL;

    private SgdDaoFactory() {
    }

    /**
     * 入参接口，返回具体数据库的实现.<p/>
     * eg:<p/>
     * 入参： <code>com.ztesoft.zsmartcity.sgd.dict.dao.DictDAO</code> <p/>
     * 如果存在MySQL和Oracle的差异性SQL语句，则<p/>
     * 连接为MySQL,返回<code>com.ztesoft.zsmartcity.sgd.dict.dao.impl.DictDAOMySQLImpl</code><p/>
     * 连接为Oracle,返回<code>com.ztesoft.zsmartcity.sgd.dict.dao.impl.DictDAOOracleImpl</code><p/>
     * <p/>
     * 如果不存在数据库差异性SQL，则<p/>
     * 返回<code>com.ztesoft.zsmartcity.sgd.dict.dao.impl.DictDAOImpl</code><p/>
     *
     * @param clazz DAO接口
     * @param <T>   <br>
     * @return <T> T  泛型
     * @throws BaseAppException
     */
    public static <T> T getDaoImpl(Class<T> clazz) throws BaseAppException {
        Assert.notNull(clazz, "not null!");
        if (!clazz.isInterface()) {
            return initDao(clazz);
        }

        DbType dbType = JdbcUtil4SGD.getDbType();

        String packagePath = clazz.getPackage().getName();
        String className = clazz.getSimpleName();

        packagePath += PACKAGE_PATH_IMPL;
        if (DbType.DB_ORACLE.equals(dbType)) {
            className += CLASS_NAME_ORACLE;
        } else if (DbType.DB_MYSQL.equals(dbType)) {
            className += CLASS_NAME_MYSQL;
        }

        String implClassPathAndName = packagePath + "." + className;
        Class<?> implClazz;

        try {
            // 存在这样的实现
            if (ClassUtils.isPresent(implClassPathAndName, Thread.currentThread().getContextClassLoader())) {
                implClazz = ClassUtils.forName(implClassPathAndName, Thread.currentThread().getContextClassLoader());
            } else {
                // 不存在
                implClassPathAndName = packagePath + "." + clazz.getSimpleName() + CLASS_IMPL;
                implClazz = ClassUtils.forName(implClassPathAndName, Thread.currentThread().getContextClassLoader());
            }
        } catch (ClassNotFoundException e) {
            throw new BaseAppException("S-DAT-00003", e.getMessage());
        }

        if (!ClassUtils.isAssignable(clazz, implClazz)) {
            throw new BaseAppException("S-DAT-00003", String.format("%s need extends from %s", implClazz.getName(), clazz.getName()));
        }

        return (T) initDao(implClazz);
    }

    private static <T> T initDao(Class<T> clazz) throws BaseAppException {
        Assert.isTrue(BaseDAO.class.isAssignableFrom(clazz), "given Class '" + clazz
                + "' must be extends from BaseDAO");

        DbIdentifier dbId = JdbcUtil4SGD.getDefaultDbService();
        try {
            BaseDAO dao = (BaseDAO) clazz.newInstance();
            dao.initRouting();
            dao.setConnection(dbId);
            return (T) dao;
        } catch (Throwable e) {
            throw ExceptionHandler.publish("S-DAT-00003", ExceptionHandler.INNER_ERROR, e);
        }
    }
}
