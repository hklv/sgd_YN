package com.ztesoft.sgd.base.jdbc;

import com.ztesoft.sgd.base.beans.BaseDto;
import com.ztesoft.sgd.base.enums.DbTable;
import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.sgd.base.helper.LoginUserHolder;
import com.ztesoft.sgd.base.id.IDGenerator;
import com.ztesoft.sgd.base.sql.SqlHelper;
import com.ztesoft.sgd.base.sql.SqlHelperFactory;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.AbstractDto;
import com.ztesoft.zsmart.core.jdbc.JdbcTemplate;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.ParamObject;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共的DAO,可直接在service引用实例化<br>
 *
 * @author sunhao<br>
 * @version 1.0 2015年5月20日 下午1:28:47
 */
public class GenericJdbcDAO extends BusiBaseDAO {
    /**
     * 实例对象
     */
    private static GenericJdbcDAO instance = null;

    /**
     * Description:
     *
     * @return GenericJdbcDAO
     * @throws BaseAppException <br>
     */
    public static GenericJdbcDAO getInstance() throws BaseAppException {
        if (instance == null) {
            instance = SgdDaoFactory.getDaoImpl(GenericJdbcDAO.class);
        }

        return instance;
    }

    /**
     * 根据主键删除 <br>
     *
     * @param table   表的枚举
     * @param idValue 主键值
     * @return int
     * @throws BaseAppException <br>
     */
    public int commDelete(DbTable table, Long idValue) throws BaseAppException {
        AssertUtil.isNotNull(table, "table can't be null!");
        AssertUtil.isNotNull(idValue, "idValue can't be null!");

        return this.commDelete(table, Collections.singletonMap(table.getIdColumnName(), idValue));
    }

    /**
     * 根据其他条件删除 <br>
     *
     * @param table       表的枚举
     * @param whereParams 其他条件
     * @return int
     * @throws BaseAppException <br>
     */
    public int commDelete(DbTable table, Map<String, ?> whereParams) throws BaseAppException {
        AssertUtil.isNotNull(table, "table can't be null!");

        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(table.getTableName());

        ParamMap params = new ParamMap();
        if (whereParams != null && !whereParams.isEmpty()) {
            sql.append(" WHERE 1 = 1 ");

            buildInSQL(sql, whereParams, params);
        }

        return executeUpdate(sql.toString(), params);
    }

    /**
     * 根据条件更新 <br>
     *
     * @param table        表的枚举
     * @param columnParams 需要更新的字段及值
     * @param whereParams  更新的条件
     * @return int
     * @throws BaseAppException <br>
     * @author sunhao<br>
     * @taskId 675504<br>
     */
    public int commUpdate(DbTable table, Map<String, ?> columnParams, Map<String, ?> whereParams)
            throws BaseAppException {
        AssertUtil.isNotNull(table, "table can't be null!");
        AssertUtil.isNotNull(columnParams, "columnParams can't be null!");

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(table.getTableName()).append(" T ").append(" \n SET ");

        ParamMap param = new ParamMap();
        List<String> columns = new ArrayList<String>();
        for (String key : columnParams.keySet()) {
            Object value = columnParams.get(key);

            columns.add(" T." + key + " = :" + key);
            param.set(key, JdbcUtil4SGD.getType(value), value);
        }

        sql.append(StringUtils.join(columns, ", "));

        if (!whereParams.isEmpty()) {
            sql.append(" \n WHERE 1 = 1 ");
            Iterator<String> it2 = whereParams.keySet().iterator();
            List<String> whereColumns = new ArrayList<String>();
            while (it2.hasNext()) {
                String key = it2.next();
                Object value = whereParams.get(key);

                if (value instanceof List) {
                    whereColumns.add(" AND T." + key + " IN " + this.handleSqlIn(value));
                } else {
                    whereColumns.add(" AND T." + key + " = :" + key);
                }

                param.set(key, JdbcUtil4SGD.getType(value), value);
            }
            sql.append(StringUtils.join(whereColumns, "  "));
        }

        return executeUpdate(sql.toString(), param);
    }

    /**
     * 根据给定的参数(where条件、查询的字段)获取结果（Map<字段,结果>）,key都是字段名大写 <br>
     *
     * @param table         表
     * @param whereParams   where条件
     * @param selectColumns 查询的字段(如果为空,即查询所有字段)
     * @return <br>
     * @throws BaseAppException <br>
     * @author sunhao<br>
     * @taskId <br>
     */
    public List<Map<String, Object>> commQuery(DbTable table, Map<String, ?> whereParams, String... selectColumns)
            throws BaseAppException {
        AssertUtil.isNotNull(table, "table can't be null!");
        AssertUtil.isNotNull(whereParams, "whereParams can't be null!");

        StringBuilder sql = new StringBuilder("SELECT ");
        if (selectColumns == null || selectColumns.length == 0) {
            sql.append(" * ");
        } else {
            sql.append(StringUtils.join(Arrays.asList(selectColumns), ", "));
        }

        sql.append(" FROM ").append(table.getTableName()).append(" WHERE 1 = 1 ");

        ParamMap params = new ParamMap();
        Iterator<String> it = whereParams.keySet().iterator();
        List<String> whereColumns = new ArrayList<String>();
        while (it.hasNext()) {
            String key = it.next();
            String column = key;
            Object value = whereParams.get(key);

            if (StringUtils.endsWith(key, "<>")) {
                column = StringUtils.removeEnd(key, "<>");
            }

            if (value instanceof List) {
                whereColumns.add(" AND " + column + " IN " + this.handleSqlIn(value));
            } else {
                if (!StringUtils.equals(key, column)) {
                    whereColumns.add(" AND " + column + " <> :" + column);
                } else {
                    if (value != null) {
                        whereColumns.add(" AND " + column + " = :" + column);
                    } else {
                        // mod:如果value的值为null，修改where语句判断为is null
                        whereColumns.add(" AND " + column + " IS NULL ");
                    }
                }
            }

            params.set(column, JdbcUtil4SGD.getType(value), value);

        }
        sql.append(StringUtils.join(whereColumns, " "));

        return query(sql.toString(), params, null, null, new RowSetMapper<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object params)
                    throws SQLException, BaseAppException {

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

                while (rs.next()) {
                    Map<String, Object> result = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsmd.getColumnName(i).toUpperCase();
                        // 不理会带$的字段
                        if (columnName.contains("$") || columnName.contains("rowno__")) {
                            continue;
                        }

                        String column = columnName;
                        if (columnName.contains("#")) {
                            String[] columnNames = columnName.split("#");
                            column = columnNames[1];
                        }
                        result.put(column, rs.getString(i));
                    }

                    results.add(result);
                }

                return results;
            }
        });
    }

    /**
     * 根据ID获取单条记录 <br>
     *
     * @param table   表的枚举
     * @param clazz   需要的类型
     * @param idValue ID值
     * @param <T>     <br>
     * @return <T>
     * @throws BaseAppException <br>
     * @author sunhao<br>
     * @taskId 675504<br>
     */
    public <T> T loadEntity(DbTable table, final Class<T> clazz, Long idValue) throws BaseAppException {
        AssertUtil.isNotNull(table, "table is required!");

        List<T> ts = loadEntities(table, clazz, Collections.singletonMap(table.getIdColumnName(), idValue));
        if (!ts.isEmpty()) {
            return ts.get(0);
        }

        return null;
    }

    /**
     * 获取单条记录 <br>
     *
     * @param table       表的枚举
     * @param clazz       需要的类型
     * @param whereParams 条件
     * @param <T>         <br>
     * @return <T> T
     * @throws BaseAppException <br>
     * @author sunhao<br>
     * @taskId 675504<br>
     */
    public <T> T loadEntity(DbTable table, final Class<T> clazz, Map<String, ?> whereParams) throws BaseAppException {
        List<T> ts = loadEntities(table, clazz, whereParams);
        if (!ts.isEmpty()) {
            return ts.get(0);
        }

        return null;
    }

    /**
     * 获取多条记录 <br>
     *
     * @param table       表的枚举
     * @param clazz       需要的类型
     * @param whereParams 条件
     * @param <T>         <br>
     * @return List
     * @throws BaseAppException <br>
     */
    public <T> List<T> loadEntities(DbTable table, final Class<T> clazz, Map<String, ?> whereParams) throws BaseAppException {
        AssertUtil.isNotNull(table, "table is required!");
        AssertUtil.isNotNull(clazz, "clazz is required!");

        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean((Class<? extends AbstractDto>) clazz);
        List<String> columns = new ArrayList<String>();

        if (sqlBean.isHasId()) {
            columns.add(sqlBean.getIdColumnName());
        }

        for (String column : sqlBean.getFieldColumnMapping().values()) {
            columns.add(column);
        }

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(StringUtils.join(columns, ", "));
        sql.append(" FROM ");
        sql.append(table.getTableName()).append(" WHERE 1 = 1");

        ParamMap params = new ParamMap();
        buildInSQL(sql, whereParams, params);

        return queryListToSimpObj3(sql.toString(), params, clazz);
    }

    /**
     * 获取多条记录[分页] <br>
     *
     * @param table       表的枚举
     * @param clazz       需要的类型
     * @param pageable    分页信息
     * @param whereParams 条件
     * @param <T>         <br>
     * @return List
     * @throws BaseAppException <br>
     */
    public <T> Page<T> loadEntities(DbTable table, final Class<T> clazz, Pageable pageable, Map<String, ?> whereParams) throws BaseAppException, SQLException {
        AssertUtil.isNotNull(table, "table is required!");
        AssertUtil.isNotNull(clazz, "clazz is required!");
        AssertUtil.isNotNull(pageable, "pageable is required!");

        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean((Class<? extends AbstractDto>) clazz);
        List<String> columns = new ArrayList<String>();

        if (sqlBean.isHasId()) {
            columns.add(sqlBean.getIdColumnName());
        }

        for (String column : sqlBean.getFieldColumnMapping().values()) {
            columns.add(column);
        }

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(StringUtils.join(columns, ", "));
        sql.append(" FROM ");
        sql.append(table.getTableName()).append(" WHERE 1 = 1");

        ParamMap params = new ParamMap();
        buildInSQL(sql, whereParams, params);

        SqlHelper sqlHelper = SqlHelperFactory.getSqlHelper();
        String countSql = sqlHelper.getCountSql(sql.toString());
        ResultSet rs = JdbcUtil4SGD.executeQuery(countSql, params);
        int count = 0;
        if (rs.next()) {
            count = rs.getInt("tmp_count");
        }

        String pageSql = sqlHelper.getPageSql(sql.toString(), pageable.getOffset(), pageable.getPageSize());
        List<T> pageObjects = queryListToSimpObj3(pageSql, params, clazz);

        return new PageImpl<T>(pageObjects, pageable, count);
    }

    /**
     * 获取对象的主键集合 <br>
     *
     * @param table       表
     * @param whereParams where语句
     * @return List
     * @throws BaseAppException <br>
     * @author sunhao<br>
     * @taskId <br>
     */
    public List<Long> loadEntityIds(DbTable table, Map<String, ?> whereParams) throws BaseAppException {
        AssertUtil.isNotNull(table, "table is required!");

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(table.getIdColumnName()).append(" FROM ");
        sql.append(table.getTableName()).append(" WHERE 1 = 1");

        ParamMap params = new ParamMap();
        buildInSQL(sql, whereParams, params);

        return queryListToSimpObj3(sql.toString(), params, Long.class);
    }

    private void buildInSQL(StringBuilder sql, Map<String, ?> whereParams, ParamMap params) {
        if (whereParams != null && !whereParams.isEmpty()) {
            for (String key : whereParams.keySet()) {
                Object value = whereParams.get(key);

                sql.append(" AND ").append(key);
                if (value instanceof List) {
                    sql.append(" IN ").append(this.handleSqlIn(value));
                } else {
                    sql.append(" = :").append(key);
                }

                params.set(key, JdbcUtil4SGD.getType(value), value);
            }
        }
    }

    /**
     * 插入对象 <br>
     *
     * @param entity 对象
     * @param <T>    <br>
     * @throws BaseAppException <br>
     */
    public <T extends BaseDto> boolean genericInsert(T entity) throws BaseAppException {
        AssertUtil.isNotNull(entity, "given entity can't be null!");
        AssertUtil.isTrue(AbstractDto.class.isAssignableFrom(entity.getClass()),
                "entity's parent class must be AbstractDto.class!");

        // 设置公共字段
        entity.setCreateDate(new java.util.Date());
        entity.setUpdateDate(new java.util.Date());
        entity.setCreateUser(LoginUserHolder.get());
        entity.setUpdateUser(LoginUserHolder.get());

        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean(entity.getClass());
        BeanWrapperImpl bw = new BeanWrapperImpl(entity);

        if (sqlBean.getIdFieldName() != null && StringUtil.isNotEmpty(sqlBean.getIdFieldName())) {
            Object id = bw.getPropertyValue(sqlBean.getIdFieldName());

            String sequence = sqlBean.getGenerator();
            if (id == null) {
                id = nextId(sequence);

                bw.setPropertyValue(sqlBean.getIdFieldName(), id);
            }
        }

        int i = executeUpdate(sqlBean.getInsertSql(), JdbcUtil4SGD.entity2ParamMap(entity));
        return i == 1;
    }

    /**
     * 批量插入
     *
     * @param entities List<T>
     * @param <T>      <br>
     * @throws BaseAppException <br>
     */
    public <T extends BaseDto> int[] genericBatchInsert(List<T> entities) throws BaseAppException {
        AssertUtil.isTrue(CollectionUtils.isNotEmpty(entities), "given entities can't be null!");

        Class<? extends BaseDto> clazz = entities.get(0).getClass();
        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean(clazz);
        String insertSql = sqlBean.getInsertSql();
        String idField = sqlBean.getIdFieldName();

        List<String> columns = this.getColumnCount(insertSql);
        int columnCounts = columns.size();
        ParamObject[] paraList = ParamObject.newParamObjectList(columnCounts, entities.size());
        for (int i = 0; i < entities.size(); i++) {
            T entity = entities.get(i);

            // 设置公共字段
            entity.setCreateDate(new java.util.Date());
            entity.setUpdateDate(new java.util.Date());
            entity.setCreateUser(LoginUserHolder.get());
            entity.setUpdateUser(LoginUserHolder.get());

            BeanWrapperImpl bw = new BeanWrapperImpl(entity);
            for (int j = 0; j < columnCounts; j++) {
                String fieldName = StringUtils.removeStart(columns.get(j), ":");
                String columnName = JdbcUtil4SGD.underscoreName(fieldName);

                if (StringUtils.isNotEmpty(idField) && StringUtils.equals(idField, fieldName)) {
                    // 存在主键
                    Object id = bw.getPropertyValue(idField);
                    if (id == null) {
                        String sequence = sqlBean.getGenerator();
                        id = nextId(sequence);

                        bw.setPropertyValue(sqlBean.getIdFieldName(), id);
                    }
                }

                Object value = bw.getPropertyValue(fieldName);
                // int type = JdbcUtil4DAP.getBatchType(value);
                if (!(value instanceof Date)) {
                    String valueStr = (value == null) ? "" : value.toString();
                    paraList[j].setBatchElement(columnName, valueStr, i);
                }
                //时间类型的字段（Date）不能转换成String，否则插入数据库时异常
                else {
                    Date date = (Date) value;
                    paraList[j].setBatchElement(columnName, date, i);
                }

            }
        }

        // 替换insertSql
        return JdbcTemplate.executeBatch(JdbcUtil4SGD.getDefaultDbService(), insertSql.replaceAll(":[\\w&&[^,)]]+", "?"),
                ParamArray.wrap(paraList));
    }

    /**
     * 获取sql语句中的字段占位符，以参数为参考值 <br>
     *
     * @param sql INSERT INTO T_DEMO (DS_FIELD_ID, MODEL_FIELD_ID, DATASET_ID) VALUES(:dsFieldId, :modelFieldId,
     *            :datasetId)
     * @return 字段占位符 [:dsFieldId, :modelFieldId, :datasetId]<br>
     */
    private List<String> getColumnCount(String sql) {
        Pattern pattern = Pattern.compile(":[\\w&&[^,)]]+");
        Matcher matcher = pattern.matcher(sql);
        List<String> columns = new ArrayList<String>();
        while (matcher.find()) {
            columns.add(matcher.group());
        }

        return columns;
    }

    /**
     * 更新对象 <br>
     *
     * @param entity T
     * @param <T>    <br>
     * @throws BaseAppException <br>
     */
    public <T extends BaseDto> boolean genericUpdate(T entity) throws BaseAppException {
        AssertUtil.isNotNull(entity, "given entity can't be null!");
        AssertUtil.isTrue(AbstractDto.class.isAssignableFrom(entity.getClass()),
                "entity's parent class must be AbstractDto.class!");

        // 设置公共字段
        entity.setUpdateDate(new java.util.Date());
        entity.setUpdateUser(LoginUserHolder.get());

        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean(entity.getClass());

        int i = executeUpdate(sqlBean.getUpdateSql(), JdbcUtil4SGD.entity2ParamMap(entity));
        return i == 1;
    }

    /**
     * 查询[分页],不需要分页,请传入null[pageable]
     *
     * @param pageable    分页信息
     * @param whereParams 查询条件
     * @param clazz       类型
     * @param <T>         类型
     * @return
     * @throws BaseAppException
     */
    public <T extends AbstractDto> Page<T> genericList(Pageable pageable, Map<String, Object> whereParams, Class<T> clazz) throws Exception {
        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean(clazz);
        String listSql = sqlBean.getListSql();
        ParamMap params = new ParamMap();

        // 组装查询语句
        if (MapUtils.isNotEmpty(whereParams)) {
            Iterator<Map.Entry<String, Object>> it = whereParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();

                String key = entry.getKey();
                String column = key;
                Object value = entry.getValue();

                if (StringUtils.endsWith(key, "<>")) {
                    column = StringUtils.removeEnd(key, "<>");
                }

                if (value instanceof List) {
                    listSql += " AND " + column + " IN " + this.handleSqlIn(value);
                } else {
                    if (!StringUtils.equals(key, column)) {
                        listSql += " AND " + column + " <> :" + column;
                    } else {
                        if (value != null) {
                            listSql += " AND " + column + " = :" + column;
                        } else {
                            // mod:如果value的值为null，修改where语句判断为is null
                            listSql += " AND " + column + " IS NULL ";
                        }
                    }
                }

                params.set(column, JdbcUtil4SGD.getType(value), value);
            }
        }

        if (pageable == null) {
            // 不需要分页
            List<T> objs = queryListToSimpObj3(listSql, params, clazz);
            return new PageImpl<T>(objs, new PageRequest(0, 0), objs.size());
        }

        SqlHelper sqlHelper = SqlHelperFactory.getSqlHelper();
        String countSql = sqlHelper.getCountSql(listSql);
        ResultSet rs = JdbcUtil4SGD.executeQuery(countSql, params);
        int count = 0;
        if (rs.next()) {
            count = rs.getInt("tmp_count");
        }

        String pageSql = sqlHelper.getPageSql(listSql, pageable.getOffset(), pageable.getPageSize());
        List<T> pageObjects = queryListToSimpObj3(pageSql, params, clazz);

        return new PageImpl<T>(pageObjects, pageable, count);
    }

    /**
     * 根据ID删除对象 <br>
     *
     * @param clazz 对象的类型
     * @param id    主键值
     * @throws BaseAppException <br>
     */
    public boolean genericDelete(Class<? extends AbstractDto> clazz, Serializable id) throws BaseAppException {
        AssertUtil.isNotNull(clazz, "given clazz can't be null!");
        AssertUtil.isNotNull(id, "given id can't be null!");
        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean(clazz);

        ParamMap params = new ParamMap();
        params.set(sqlBean.getIdFieldName(), JdbcUtil4SGD.getType(id), id);

        int i = executeUpdate(sqlBean.getDeleteSql(), params);
        return i == 1;
    }

    /**
     * 通过注解的方式直接获取一个对象 <br>
     *
     * @param clazz 对象的类型
     * @param id    主键值
     * @param <T>   <br>
     * @return 对象
     * @throws BaseAppException <br>
     */
    public <T extends AbstractDto> T genericLoad(final Class<T> clazz, Serializable id) throws BaseAppException {
        AssertUtil.isNotNull(clazz, "given clazz can't be null!");
        AssertUtil.isNotNull(id, "given id can't be null!");

        DtoSqlBean sqlBean = DynamicGenerateSql.getDtoSqlBean(clazz);

        ParamMap params = new ParamMap();
        params.set(sqlBean.getIdFieldName(), JdbcUtil4SGD.getType(id), id);

        String selectSql = sqlBean.getSelectSql();

        return queryToSimpObj(selectSql, params, clazz);
    }

    /**
     * 处理sql语句中含有in的情况 <br>
     *
     * @param value <br>
     * @return List
     */
    private String handleSqlIn(Object value) {
        if (!(value instanceof List)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sql = new StringBuilder();
        List value_ = (List) value;
        sql.append("(");

        for (int i = 0; i < value_.size(); i++) {
            Object v = value_.get(i);
            if (v instanceof String) {
                sql.append("'").append(v).append("'");
            } else {
                sql.append(v);
            }

            if (i != value_.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");

        return sql.toString();
    }

    private Long nextId(String sequenceName) throws BaseAppException {
        IDGenerator idGenerator = SqlHelperFactory.getSqlHelper().getIDGenerator();
        return idGenerator.nextId(sequenceName);
    }
}
