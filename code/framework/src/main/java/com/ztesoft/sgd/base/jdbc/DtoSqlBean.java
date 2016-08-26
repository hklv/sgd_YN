package com.ztesoft.sgd.base.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ztesoft.zsmart.core.jdbc.AbstractDto;

/**
 * 对dto的sql语句描述对象
 *
 * @author sunhao
 * @version 1.0 2015年5月20日 下午4:58:57
 */
public class DtoSqlBean {
    /**
     * 对应的类
     */
    private Class<? extends AbstractDto> clazz;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 是否有主键
     */
    private boolean hasId = false;
    /**
     * id字段在对象中的字段名称
     */
    private String idFieldName;
    /**
     * id字段在数据库中的列名
     */
    private String idColumnName;
    /**
     * 序列名
     */
    private String generator;
    /**
     * 类中的字段名与数据库列名的映射关系,不包含主键字段
     */
    private Map<String, String> fieldColumnMapping = new HashMap<String, String>();
    /**
     * blob类型的字段
     */
    private List<String> blobColumns = new ArrayList<String>();
    /**
     * 向数据库插入对象的sql
     */
    private String insertSql;
    /**
     * 更新对象的sql
     */
    private String updateSql;
    /**
     * 删除对象的sql
     */
    private String deleteSql;
    /**
     * 查询对象的sql
     */
    private String selectSql;
    /**
     * 查询列表的sql
     */
    private String listSql;

    /**
     * Description: 构造方法
     *
     * @param clazz DTO
     */
    public DtoSqlBean(Class<? extends AbstractDto> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends AbstractDto> getClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends AbstractDto> clazz) {
        this.clazz = clazz;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isHasId() {
        return hasId;
    }

    public void setHasId(boolean hasId) {
        this.hasId = hasId;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Map<String, String> getFieldColumnMapping() {
        return fieldColumnMapping;
    }

    public void setFieldColumnMapping(Map<String, String> fieldColumnMapping) {
        this.fieldColumnMapping = fieldColumnMapping;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public List<String> getBlobColumns() {
        return blobColumns;
    }

    public void setBlobColumns(List<String> blobColumns) {
        this.blobColumns = blobColumns;
    }

    /**
     * Description: 增加blob列<br>
     *
     * @param field 列<br>
     */
    public void addBlobColumn(String field) {
        this.blobColumns.add(field);
    }

    /**
     * Description: 增加字段列映射<br>
     *
     * @param field  字段<br>
     * @param column 列 <br>
     */
    public void addFieldColumnMapping(String field, String column) {
        this.fieldColumnMapping.put(field, column);
    }

    public String getListSql() {
        return listSql;
    }

    public void setListSql(String listSql) {
        this.listSql = listSql;
    }
}
