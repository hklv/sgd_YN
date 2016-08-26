package com.ztesoft.sgd.base.sql;

import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.zsmart.core.config.ConfigObject;
import com.ztesoft.zsmart.core.config.ConfigObjectImpl;
import com.ztesoft.zsmart.core.config.ConfigProperties;
import com.ztesoft.zsmart.core.config.commons.CommonBuilderUtils;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.config.ConfigDbIdentifier;
import com.ztesoft.zsmart.core.jdbc.ds.ConnectionProviderFactory;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifier;
import com.ztesoft.zsmart.core.jdbc.ds.DbIdentifierImpl;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试数据源，将公共的方法放在这里 <br>
 *
 * @author sunhao<br>
 * @version 1.0 15/5/6<br>
 */
public abstract class AbstractSqlHelper implements SqlHelper {

    /**
     * logger
     */
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(AbstractSqlHelper.class);

    /**
     * path
     */
    protected static String path = "jdbc/dbBackService";

    /**
     * name
     */
    private static String name = "dbBackService";

    /**
     * 缓存DBID
     */
    private static Map<String, DbIdentifier> dbIDMap = new HashMap<String, DbIdentifier>();

    /**
     * 设置数据库特有的属性
     *
     * @param configObject CommonConfigProperties 参数
     */
    public abstract void setSpecialConfig(CommonConfigProperties configObject);

    @Override
    public DbIdentifier getDbId(String connectionUrl, String userName, String password) throws BaseAppException {

        AssertUtil.isNotEmpty(connectionUrl, "connection url not null!");
        AssertUtil.isNotEmpty(userName, "userName not null!");

        String dbName = connectionUrl + "|" + userName + "|" + password;

        synchronized (dbIDMap) {

            DbIdentifier dbID = dbIDMap.get(dbName);
            if (dbID == null) {

                CommonConfigProperties configObject = new CommonConfigProperties(path, "dbBackService", null,
                        new HashMap<String, String>());
                addConfigObject(configObject, path, "provider",
                        "com.ztesoft.zsmart.core.jdbc.ds.MyPoolConnectionProvider");
                addConfigObject(configObject, path, "routeType", "0");
                addConfigObject(configObject, path, "url", connectionUrl);
                addConfigObject(configObject, path, "usrName", userName);
                addConfigObject(configObject, path, "password", password);
                addConfigObject(configObject, path, "connectionWaitTimeout", "5");
                addConfigObject(configObject, path, "isPasswordEncrypt", "false");
                addConfigObject(configObject, path, "maxConnections", "32");
                addConfigObject(configObject, path, "initalConnections", "1");
                addConfigObject(configObject, path, "maxStatement", "100");
                addConfigObject(configObject, path, "maxIdle", "600");
                addConfigObject(configObject, path, "fullTrace", "true");
                addConfigObject(configObject, path, "linkCheckInterval", "600");
                // 开始设置数据库特有的属性
                this.setSpecialConfig(configObject);

                ConfigDbIdentifier dbConfig = new ConfigDbIdentifier(name);
                dbConfig.getValues(configObject);
                dbID = new DbIdentifierImpl(dbConfig);

                try {
                    ConnectionProviderFactory.newConnectionProvider(dbID);
                } catch (BaseAppException se) {
                    logger.error("db identifier does not exists! dbid = " + dbName, se);
                    return null;
                }

                dbIDMap.put(dbName, dbID);
            }

            return dbID;
        }

    }

    /**
     * Description:addConfigObject <br>
     *
     * @param owner     CommonConfigProperties
     * @param ownerPath String
     * @param name      String
     * @param value     String<br>
     * @author sunhao<br>
     * @taskId <br>
     */
    protected void addConfigObject(CommonConfigProperties owner, String ownerPath, String name, String value) {
        String path = CommonBuilderUtils.getPath(ownerPath, name);
        Map<String, String> attributes = new HashMap<String, String>();
        ConfigObject confObj = new ConfigObjectImpl(path, name, value, attributes);
        owner.addOverride(confObj);
    }

    @Override
    public String getCountSql(String sql) {
        StringBuilder sqlBuilder = new StringBuilder();
        return sqlBuilder.append("select count(*) from (").append(sql).append(") tmp_count").toString();
    }

    @Override
    public boolean existTable(String tableName) throws Exception {
        Assert.hasText(tableName);

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtil4SGD.getDefaultDbService().getConnectionProvider().getConnection();

            String name = connection.getCatalog();
            ps = connection.prepareStatement(existTableSql(name, tableName));
            resultSet = ps.executeQuery();

            return resultSet.next() && resultSet.getInt("count") > 0;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * 判断表是否存在的sql
     *
     * @param schema
     * @param tableName
     * @return
     */
    public abstract String existTableSql(String schema, String tableName);

    /**
     * <Description> 内部类CommonConfigProperties<br>
     *
     * @author sunhao<br>
     * @version 1.0 2015-05-06<br>
     */
    static class CommonConfigProperties extends ConfigProperties {

        /**
         * 构造
         *
         * @param path       String
         * @param name       String
         * @param value      String
         * @param attributes Map<String, String>
         */
        public CommonConfigProperties(String path, String name, String value, Map<String, String> attributes) {
            super(path, name, value, attributes);
        }

        /**
         * Description: 重写方法<br>
         *
         * @param confObj <br>
         * @author sunhao<br>
         * @taskId <br>
         */
        void addOverride(ConfigObject confObj) {
            add(confObj);
        }
    }
}
