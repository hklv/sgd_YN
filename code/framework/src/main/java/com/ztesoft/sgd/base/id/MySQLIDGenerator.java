package com.ztesoft.sgd.base.id;

import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.sgd.base.sql.SqlHelperFactory;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * mysql 主键自增长.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/17 下午3:09
 */
public class MySQLIDGenerator implements IDGenerator {
    private static final String SEQUENCE_TABLE = "t_sequence";

    private static final String CREATE_SEQUENCE_TABLE = "CREATE TABLE t_sequence (name varchar(32) NOT NULL,value bigint(5) NOT NULL,PRIMARY KEY (name))";
    private static final String GET_NEXT_ID_SQL = "SELECT value FROM t_sequence WHERE name = :sequenceName";
    private static final String UPDATE_NEXT_ID_SQL = "UPDATE t_sequence SET value = :value WHERE name = :sequenceName";
    private static final String INSERT_NEXT_ID_SQL = "INSERT INTO t_sequence (name, value) VALUES (:sequenceName, :value)";

    /**
     * cache for each sequence's next id
     **/
    private static final Map<String, Long> NEXT_ID_CACHE = new HashMap<String, Long>();
    /**
     * cache for each sequence's max id
     **/
    private static final Map<String, Long> MAX_ID_CACHE = new HashMap<String, Long>();
    /**
     * The number of keys buffered in a cache
     */
    private static final int CACHE_SIZE = 20;

    public MySQLIDGenerator() throws BaseAppException {
        if (isExistTable()) {
            return;
        }

        // 初始化这张表
        boolean ok = JdbcUtil4SGD.executeSQL(CREATE_SEQUENCE_TABLE, null) == 1;
        Assert.isTrue(ok, String.format("执行sql语句[%s]发送错误,请检查!", CREATE_SEQUENCE_TABLE));
    }

    @Override
    public synchronized Long nextId(String sequenceName) throws BaseAppException {
        Assert.hasText(sequenceName);

        // 缓存中的
        Long nextId = NEXT_ID_CACHE.get(sequenceName);
        Long maxId = MAX_ID_CACHE.get(sequenceName);

        if (nextId != null && nextId <= maxId) {
            NEXT_ID_CACHE.put(sequenceName, nextId + 1);
            return nextId;
        }

        return findNextIdFromDatabase(sequenceName);
    }

    private boolean isExistTable() {
        try {
            return SqlHelperFactory.getSqlHelper().existTable(SEQUENCE_TABLE);
        } catch (Exception e) {
            return false;
        }
    }

    private Long findNextIdFromDatabase(String sequenceName) throws BaseAppException {
        Long nextId;

        ParamMap paramMap = new ParamMap();
        paramMap.set("sequenceName", sequenceName);
        ResultSet rs = JdbcUtil4SGD.executeQuery(GET_NEXT_ID_SQL, paramMap);

        Assert.notNull(rs);
        try {
            String sql;
            if (rs.next()) {
                nextId = rs.getLong("value");
                sql = UPDATE_NEXT_ID_SQL;
            } else {
                nextId = 1L;
                sql = INSERT_NEXT_ID_SQL;
            }

            // 更新数据库中的值 nextId+CACHE_SIZE+1
            ParamMap paramMap2 = new ParamMap();
            paramMap2.set("sequenceName", sequenceName);
            paramMap2.set("value", nextId + CACHE_SIZE + 1);
            JdbcUtil4SGD.executeSQL(sql, paramMap2);

            // 缓存最大值
            MAX_ID_CACHE.put(sequenceName, nextId + CACHE_SIZE);
            NEXT_ID_CACHE.put(sequenceName, nextId + 1);

            return nextId;
        } catch (SQLException e) {
            throw new BaseAppException("", e.getMessage());
        }
    }
}
