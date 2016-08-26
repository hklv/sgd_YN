package com.ztesoft.uboss.form.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.form.dao.IFormDbDAO;
import com.ztesoft.uboss.form.model.DynFormDbDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;

public class FormDbDAO extends BusiBaseDAO implements IFormDbDAO {

    public boolean isExistTableByName(String tableName) throws BaseAppException {

        return false;
    }

    public List<DynFormDbDto> queryTableColumnList(String tableName)
            throws BaseAppException {
        // TODO Auto-generated method stub
        return null;
    }

    public void createTable(DynFormDbDto dynFormDbDto) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void createTable(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void updateTable(DynFormDbDto dynFormDbDto) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void updateTable(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

    }


    public void createTable(String sql) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void dropTable(String sTableName) throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" DROP TABLE " + sTableName + ";");
        System.out.println(sqlbuilder.toString());
        Session ses = null;
        try {
            ses = SessionContext.newSession();
            ses.beginTrans();// 启动事务
            this.executeUpdate(sqlbuilder.toString(), pm);
            ses.commitTrans();// 提交事务
        } finally {

            ses.releaseTrans();// 释放事务
        }


    }

    public void modifyTable(String sql) throws BaseAppException {
        // TODO Auto-generated method stub
        if (sql == null || sql.trim().length() <= 5) {
            System.out.println("excute Sql is null;");
            return;
        }
        ParamMap pm = new ParamMap();
        Session ses = null;
        try {
            ses = SessionContext.newSession();
            ses.beginTrans();// 启动事务
            String sql_str[] = sql.split(";");
            if (sql_str != null) {
                int iLen = sql_str.length;
                for (int i = 0; i < iLen; i++) {
                    if (sql_str[i] != null && !sql_str[i].equals("")) {
                        sql_str[i] = sql_str[i].trim();
                        if (!sql_str[i].startsWith("--") && !sql_str[i].equals("")) {
                            System.out.println("excute Sql:" + sql_str[i]);
                            this.executeUpdate(sql_str[i], pm);
                        }
                    }
                }
            }

            ses.commitTrans();// 提交事务
        } finally {

            ses.releaseTrans();// 释放事务
        }

    }

    public void addColumn(String sTableName, String sField, String sFieldType,
                          String sFieldLen) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void addColumn(String sTableName, String sField, String sFieldType,
                          String sFieldLen, String defaultValue) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void dropColumn(String sTableName, String sField)
            throws BaseAppException {
        // TODO Auto-generated method stub

    }
}
