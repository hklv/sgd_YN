package com.ztesoft.uboss.form.sz600.formEngine2;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.form.dao.ISqlConvertDAO;
import com.ztesoft.uboss.form.dao.mysqlimpl.SqlConvertDAOMySQL;
import com.ztesoft.uboss.form.sz600.tool.SzConvert;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;

import java.sql.ResultSet;
import java.util.ArrayList;

public class DynSelectControl {

    public String strTable = ""; // 表名
    public String strField = ""; // 字段组合
    public String strValue = "";

    public String value_str[] = null; // 值
    public int iFieldCount = 0; // 字段的数量
    public String strCond = "";
    public ArrayList arrayList = null; // 列出的字段序号

    private ResultSet rs = null;
    private String strSQL = "";
    private boolean flag = false;
    private DynSelectTableInfo tableInfo = new DynSelectTableInfo();
    private final int iMaxCount = 15;
    private String obj_str[] = new String[iMaxCount - 1];
    private boolean isType = false;

    private ISqlConvertDAO sqlConvert = null;

    private ISqlConvertDAO getSqlConvertDao() throws BaseAppException {
        if (sqlConvert == null) {
            sqlConvert = SgdDaoFactory.getDaoImpl(SqlConvertDAOMySQL.class);
        }
        return sqlConvert;
    }

    public DynSelectControl(String strName) throws BaseAppException {
        // System.out.println("============构造函数");
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            DynReadSelectXML readSelectXML = new DynReadSelectXML();
            // 获取已经设置的详细信息
            tableInfo = (DynSelectTableInfo) readSelectXML
                    .getTableInfoFromXML(strName);
            // System.out.println("tableInfo===" + tableInfo.getGLBM());
            if (tableInfo == null) {
                tableInfo = new DynSelectTableInfo();
            }
            // System.out.println("tableInfo===" + tableInfo.getGLBM());
            this.initialObj();
            s.commitTrans();
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }
    }

    /**
     * 取得设置的信息
     *
     * @return
     */
    public DynSelectTableInfo getTableInfo() {
        return this.tableInfo;
    }

    public int getFieldLen() {
        return iFieldCount;
    }

    public String[] getObjStr() {
        return this.obj_str;
    }

    private void initialObj() {
        if (tableInfo == null) {
            return;
        }
        String strObject = tableInfo.getFieldDesc();
        // 初始化
        for (int i = 0; i < iMaxCount - 1; i++) {
            obj_str[i] = "";
        }

        String s_option[] = strObject.split(";");
        int iLen = 0;
        String sTemp = "";
        if (s_option != null) {
            iLen = s_option.length;
            for (int i = 0; i < iLen && i < iMaxCount - 1; i++) {
                sTemp = s_option[i];
                if (sTemp.equals("")) {
                    continue;
                }
                obj_str[i] = sTemp;
            }
        }

    }

    private ArrayList getTypeList(int iDLID, boolean isManage)
            throws BaseAppException {
        return this.getTypeList(iDLID, isManage, "0");
    }

    private ArrayList getTypeList(int iDLID, boolean isManage, String COMPANY)
            throws BaseAppException {
        strCond = "1=1";
        if (tableInfo.getFieldPopedom().equals("2")) {
            if (isManage) {
                strCond = "1=1";
            } else {
                strCond = " GLDLID=" + iDLID;
            }
        } else if (tableInfo.getFieldPopedom().equals("1")) {
            strCond = " GLDLID=" + iDLID;
        }

        // 组合sql
        StringBuffer sb = new StringBuffer("SZOA_BDGSID=");
        sb.append(COMPANY);
        sb.append(" AND ");
        sb.append(strCond);

        String sCond = strCond;// sb.toString();
        sb = null;

        strTable = tableInfo.getGLBM();
        strField = tableInfo.getFieldName();
        strField = strField.replaceAll(";", ",");
        if (strTable.equals("") || strField.equals("")) {
            return null;
        }
        iFieldCount = 2;
        strSQL = getSqlConvertDao().genSql(this.strTable, this.strField, sCond);
        isType = true;
        arrayList = this.getArrayListBySql(strSQL);
        isType = false;
        return arrayList;
    }

    public ArrayList getArrayList(int iDLID, boolean isManage)
            throws BaseAppException {
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            ArrayList list = this.getArrayList(iDLID, isManage, "0");
            s.commitTrans();
            return list;
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }
    }

    private ArrayList getArrayList(int iDLID, boolean isManage, String COMPANY)
            throws BaseAppException {
        return this.getArrayList(iDLID, isManage, COMPANY, "");
    }

    private ArrayList getArrayList(int iDLID, boolean isManage, String COMPANY,
                                   String sRelateValue) throws BaseAppException {
        // 0列出所有 1列出个人所有 2列出个人所有，管理员列出全部
        strCond = "1=1";

        if (tableInfo.getFieldPopedom().equals("2")) {
            if (isManage) {
                strCond = "1=1";
            } else {
                strCond = " FieldID=" + iDLID;
            }
        } else if (tableInfo.getFieldPopedom().equals("1")) {
            strCond = " FieldID=" + iDLID;
        }

        // 组合sql
        // 组合sql
        /*
		 * StringBuffer sb = new StringBuffer("SZOA_BDGSID=");
		 * sb.append(COMPANY); sb.append(" AND "); sb.append(strCond);
		 */
        StringBuffer sb = new StringBuffer(strCond);
        if (!tableInfo.getRelateField().equals("") && !sRelateValue.equals("")) {
            sb.append(" AND ");
            sb.append(tableInfo.getRelateField());
            sb.append("='");
            sb.append(sRelateValue);
            sb.append("'");
        }

        // 增加条件2010-06-18
        if (!tableInfo.getGLTJ().equals("")) {
            sb.append(" AND ");
            sb.append(SzConvert.toHTMLString(tableInfo.getGLTJ()));
        }

        String sCond = sb.toString();
        sb = null;

        strTable = tableInfo.getGLBM();
        sb = new StringBuffer("");
        if (!tableInfo.getFieldType().equals("")) {
            sb.append(",");
            sb.append(tableInfo.getFieldType());
        }
        if (!tableInfo.getRelateField().equals("")) {
            sb.append(",");
            sb.append(tableInfo.getRelateField());
        }
        sb.append(",");
        sb.append(tableInfo.getFieldName());

        strField = sb.toString();
        strField = strField.substring(1);

        strField = strField.replaceAll(";", ",");
        this.getFieldLen(strField);
        sCond = sCond.replaceAll("&gt;", ">").replaceAll("&lt;", "<");

//		System.out.println("==strTable=" + strTable);
//		System.out.println("==strField=" + strField);
//		System.out.println("==sCond=" + sCond);

        strSQL = getSqlConvertDao().genSql(this.strTable, this.strField, sCond);

//		System.out.println("==strSQL=" + strSQL);
        arrayList = this.getArrayListBySql(strSQL);

        return arrayList;
    }

    private void getFieldLen(String strField) {
        iFieldCount = 0;
        // System.out.println(strField + "==" + iFieldCount);
        String s_option[] = strField.split(",");
        int iLen = 0;
        String sTemp = "";
        if (s_option != null) {
            iLen = s_option.length;
            for (int i = 0; i < iLen; i++) {
                sTemp = s_option[i];
                if (sTemp.equals("")) {
                    continue;
                }
                iFieldCount++;
            }
        }
        if (iFieldCount > iMaxCount) {
            iFieldCount = iMaxCount;
        }
    }

    private ArrayList getArrayListByRs(ResultSet rs) {
        arrayList = null;
        // System.out.println("isType==" + isType);
        try {
            if (rs == null) {
                return null;
            }
            arrayList = new ArrayList();
            if (this.iFieldCount <= 0) {
                return arrayList;
            }
            int i = 0;
            int j = 0;
            int k = 0;
            // System.out.println("==strTable=" + strTable);
            // System.out.println("==strField=" + strField);
            // System.out.println("==sCond=" + sCond);
            // System.out.println("iMaxCount=" + iMaxCount);
            // System.out.println("iFieldCount=" + iFieldCount);
            // System.out.println("tableInfo.getFieldType()=" +
            // tableInfo.getFieldType());
            // 分类
            if (isType) {
                while (rs.next()) {
                    this.value_str = new String[iMaxCount];
                    SzConvert.initialArray(value_str, "");

                    for (i = 1; i <= iFieldCount; i++) {
                        // 2012-01-10增加转码
                        value_str[i + 1] = SzConvert.strReadData(rs
                                .getString(i));
                    }
                    arrayList.add(value_str);
                }
            } else {
                j = 0;
                k = 2;
                while (rs.next()) {
                    j = 0;
                    k = 2;
                    this.value_str = new String[iMaxCount];
                    SzConvert.initialArray(value_str, "");
                    if (!tableInfo.getFieldType().equals("")) {
                        j++;
                        value_str[0] = SzConvert.strReadData(rs.getString(j));
                    }
                    if (!tableInfo.getRelateField().equals("")) {
                        j++;
                        value_str[1] = SzConvert.strReadData(rs.getString(j));
                    }
                    // 字段从j开始。
                    for (i = j; i < iFieldCount; i++, k++) {
                        value_str[k] = SzConvert.strReadData(rs
                                .getString(i + 1));
                        // System.out.println("i=" + i + "==" + value_str[i]);
                    }
                    // SzOut.printArray(value_str);
                    arrayList.add(value_str);
                }
            }
			/*
			 * while (rs.next()) {
			 * 
			 * this.value_str = new String[iMaxCount];
			 * 
			 * if (tableInfo.getFieldType().equals("") || isType) { value_str[0]
			 * = ""; //没有分类，第一个value为空用空 j = 0; for (i=1; i<=iFieldCount; i++) {
			 * value_str[i] = rs.getString(i); } } else { ////如果指定了分类字段
			 * //value_str[0] = ""; for (i=0; i<iFieldCount; i++) { value_str[i]
			 * = rs.getString(i + 1); } }
			 * 
			 * for (i=iFieldCount + 1 ; i<iMaxCount; i++) { value_str[i] = ""; }
			 * arrayList.add(value_str); }
			 */
            if (rs != null) {
                rs.close();
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    // 通过sql语句获取ArrayList，其他方法都调用本方法
    private ArrayList getArrayListBySql(String sql) throws BaseAppException {

        try {
            return getSqlConvertDao().getArrayListBySql(sql);
			/*
			 * dataReader = new SzDataReader();
			 * 
			 * //rs = dataReader.readInfo(strSQL); rs =
			 * dataReader.readInfo(sql); if (rs == null) { return null; }
			 * //System.out.println("hello" + rs); arrayList =
			 * this.getArrayListByRs(rs); //关闭所有的连接 dataReader.closeAll();
			 */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}