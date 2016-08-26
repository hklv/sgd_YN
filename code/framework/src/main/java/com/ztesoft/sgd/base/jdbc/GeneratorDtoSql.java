package com.ztesoft.sgd.base.jdbc;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Id;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.sgd.base.helper.StringHelper;
import com.ztesoft.zsmart.core.jdbc.AbstractDto;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 生成对象的基本sql语句<br>
 *
 * @author sunhao<br>
 * @version 1.0 2015年5月20日 下午4:55:08
 */
public class GeneratorDtoSql {
    /**
     * dtoSqlBean
     */
    private DtoSqlBean dtoSqlBean;

    /**
     * 构造方法
     *
     * @param clazz DTO类
     */
    public GeneratorDtoSql(Class<? extends AbstractDto> clazz) {
        this.dtoSqlBean = new DtoSqlBean(clazz);
    }

    /**
     * Description: build
     *
     * @return DtoSqlBean
     */
    public DtoSqlBean build() {
        Class<? extends AbstractDto> clazz = this.dtoSqlBean.getClazz();

        // 1. 类头上的注解
        Annotation[] anns = clazz.getAnnotations();
        for (Annotation ann : anns) {
            Class<? extends Annotation> type = ann.annotationType();
            if (Table.class.equals(type)) {
                // 是基于注解的dto
                evalTable((Table) ann);
            }
        }

        // 2. 处理字段
        List<Field> fields = new ArrayList<Field>();
        // 获得类中所有申明的字段
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        // 处理父类
        if (clazz.getSuperclass() != null && !Object.class.equals(clazz.getSuperclass())) {
            fields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }

        for (Field f : fields) {
            Annotation[] fanns = f.getDeclaredAnnotations();
            for (Annotation ann : fanns) {
                evalColumn(f, ann);
            }
        }

        this.buildCRUD();

        return this.dtoSqlBean;
    }

    /**
     * Description: 处理表
     *
     * @param table 表
     */
    private void evalTable(Table table) {
        String name = table.name();
        // 设置表名
        this.dtoSqlBean.setTableName(name);

        String sequence = table.sequence();
        if (StringHelper.isNotBlank(sequence)) {
            this.dtoSqlBean.setGenerator(table.sequence());
        }
    }

    /**
     * Description: 处理列
     *
     * @param field 列
     * @param ann   注解
     */
    private void evalColumn(Field field, Annotation ann) {
        Class<? extends Annotation> type = ann.annotationType();
        if (Id.class.equals(type)) {
            // 主键
            String idFieldName = field.getName();
            String idColumnName = JdbcUtil4SGD.underscoreName(idFieldName);

            this.dtoSqlBean.setIdFieldName(idFieldName);
            this.dtoSqlBean.setIdColumnName(idColumnName);
            this.dtoSqlBean.setHasId(true);
        } else if (Column.class.equals(type)) {
            // 普通字段
            String fieldName = field.getName();
            String columnName = JdbcUtil4SGD.underscoreName(fieldName);

            this.dtoSqlBean.addFieldColumnMapping(fieldName, columnName);

            if (((Column) ann).isBlob()) {
                this.dtoSqlBean.addBlobColumn(field.getName());
            }
        }
    }

    /**
     * 生成CRUD语句 <br>
     */
    private void buildCRUD() {
        buildInsertSql();
        buildUpdateSql();
        buildDeleteSql();
        buildSelectSql();
        buildListSql();
    }

    private void buildListSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(buildBaseLoadSql());

        sql.append(" WHERE ").append(" 1 = 1 ");

        this.dtoSqlBean.setListSql(sql.toString());
    }

    /**
     * Description: 构造Insert语句
     */
    private void buildInsertSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(this.dtoSqlBean.getTableName());
        Map<String, String> fieldColumnMapping = this.dtoSqlBean.getFieldColumnMapping();
        List<String> columns = new ArrayList<String>();
        List<String> values = new ArrayList<String>();

        if (this.dtoSqlBean.isHasId()) {
            columns.add(this.dtoSqlBean.getIdColumnName());
            values.add(":" + this.dtoSqlBean.getIdFieldName());
        }
        for (Map.Entry<String, String> entry : fieldColumnMapping.entrySet()) {
            String field = entry.getKey();
            String column = entry.getValue();

            columns.add(column);
            if (this.dtoSqlBean.getBlobColumns().contains(field)) {
                values.add("empty_blob()");
            } else {
                values.add(":" + field);
            }
        }

        sql.append(" (").append(StringUtils.join(columns, ", ")).append(") values (")
                .append(StringUtils.join(values, ", ")).append(")");

        this.dtoSqlBean.setInsertSql(sql.toString());
    }

    /**
     * Description: 构造Update语句
     */
    private void buildUpdateSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(this.dtoSqlBean.getTableName());
        sql.append(" SET ");

        Map<String, String> fieldColumnMapping = this.dtoSqlBean.getFieldColumnMapping();
        List<String> columns = new ArrayList<String>();

        for (Iterator<Map.Entry<String, String>> it = fieldColumnMapping.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            String field = entry.getKey();
            String column = entry.getValue();

            if (this.dtoSqlBean.getBlobColumns().contains(field)) {
                columns.add(column + " = empty_blob()");
            } else {
                columns.add(column + " = :" + field);
            }
        }

        sql.append(StringUtils.join(columns, ", ")).append(" WHERE 1 = 1 ");
        if (this.dtoSqlBean.isHasId()) {
            sql.append(" AND ").append(this.dtoSqlBean.getIdColumnName()).append(" = :")
                    .append(this.dtoSqlBean.getIdFieldName());
        }

        this.dtoSqlBean.setUpdateSql(sql.toString());
    }

    /**
     * Description: 构造Delete语句
     */
    private void buildDeleteSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(this.dtoSqlBean.getTableName());
        sql.append(" WHERE 1 = 1 ");
        if (this.dtoSqlBean.isHasId()) {
            sql.append(" AND ").append(this.dtoSqlBean.getIdColumnName()).append(" = :")
                    .append(this.dtoSqlBean.getIdFieldName());
        }

        this.dtoSqlBean.setDeleteSql(sql.toString());
    }

    /**
     * Description: 构造Select语句
     */
    private void buildSelectSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(buildBaseLoadSql());

        sql.append(" WHERE ").append(this.dtoSqlBean.getIdColumnName()).append(" = :")
                .append(this.dtoSqlBean.getIdFieldName());

        this.dtoSqlBean.setSelectSql(sql.toString());
    }

    private String buildBaseLoadSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");


        Map<String, String> fieldColumnMapping = this.dtoSqlBean.getFieldColumnMapping();
        List<String> columns = new ArrayList<String>();

        if (this.dtoSqlBean.isHasId()) {
            columns.add(this.dtoSqlBean.getIdColumnName());
        }

        for (String column : fieldColumnMapping.values()) {
            columns.add(column);
        }

        sql.append(StringUtils.join(columns, ", "));

        sql.append(" FROM ").append(this.dtoSqlBean.getTableName());

        return sql.toString();
    }
}
