package com.ztesoft.uboss.form.sz600.formEngine2;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.form.dao.IFormDbDAO;
import com.ztesoft.uboss.form.dao.ISqlConvertDAO;
import com.ztesoft.uboss.form.dao.mysqlimpl.FormDbDAOMySQL;
import com.ztesoft.uboss.form.dao.mysqlimpl.SqlConvertDAOMySQL;
import com.ztesoft.zsmart.core.exception.BaseAppException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class DynFormGenerator {

    private Map detailMap = null;
    private String strMainTable = null;
    Collection collDetail = null;
    Iterator iterDetail = null;
    String s_detail[] = null;

    private DynFormInfo bdFormInfo = null;
    private Collection collTable = null;
    private Collection collField = null;
    private DynFormTableInfo bdFormTableInfo = null;
    private DynFormFieldInfo bdFormFieldInfo = null;

    private IFormDbDAO formDbDao = null;

    private IFormDbDAO getFormDbDao() throws BaseAppException {
        if (formDbDao == null) {
            formDbDao = SgdDaoFactory.getDaoImpl(FormDbDAOMySQL.class);
        }
        return formDbDao;
    }

    private ISqlConvertDAO sqlConvert = null;

    private ISqlConvertDAO getSqlConvertDao() throws BaseAppException {
        if (sqlConvert == null) {
            sqlConvert =  SgdDaoFactory.getDaoImpl(SqlConvertDAOMySQL.class);
        }
        return sqlConvert;
    }

    public DynFormGenerator(long dynFormId) {

        DynFormControl bdFormControl = new DynFormControl();
        bdFormControl.getFormInfo(dynFormId);
        this.detailMap = bdFormControl.detailMap;
        this.strMainTable = bdFormControl.strMainTable;

        Map formMap = bdFormControl.getFormMap();
        this.bdFormInfo = (DynFormInfo) formMap.get(dynFormId + "");

        Map fieldMap = bdFormControl.getFieldMap();
        this.collField = (Collection) fieldMap.get(dynFormId + "");
    }

    /**
     * 生成表单下所有表的创建脚本，可能存在多个。
     *
     * @return
     */
    private String genMainCreateSql() throws BaseAppException {
        return this.genMainCreateSql(0);
    }

    // iDrop为0时不主动删除表，为1时删除表
    private String genMainCreateSql(int iDrop) throws BaseAppException {

        StringBuffer sb = new StringBuffer("");
        String sql = "";
        Iterator iterField = null;
        String tableId = "";
        String tableName = "";
        String sTableType = "";
        int i = 0;
        //
        collDetail = this.detailMap.values();
        if (collDetail == null) {
            return "";
        }
        // System.out.println(collDetail.size());
        iterDetail = collDetail.iterator();
        while (iterDetail != null && iterDetail.hasNext()) {
            s_detail = (String[]) iterDetail.next();
            if (s_detail == null) {
                continue;
            }
            tableName = s_detail[1];

            // 增加drop的脚本
            if (iDrop == 1) {
                // sb.append("-------- drop table sql;");
                // sb.append("\n");

                // sb.append("DROP TABLE IF EXISTS ");
                sb.append("DROP TABLE  ");
                sb.append(tableName);
                sb.append(";\n");

            }

            // sb.append("-------- create table sql");
            // sb.append(";\n");

            sb.append(" CREATE TABLE  ");
            sb.append(tableName);
            sb.append(" ( \n");

            i = 0;
            if (s_detail[6] == "2") {
                sb.append("    dyn_id  " + getSqlConvertDao().getNumber()
                        + " not null");
                i++;
                sb.append(" ,\n");
                sb.append("    dyn_form_id  " + getSqlConvertDao().getNumber()
                        + " ");
                i++;
                sb.append(" ,\n");
                sb.append("    dyn_ver_id  " + getSqlConvertDao().getNumber()
                        + " ");
                i++;
                sb.append(" ,\n");
                sb.append("    order_id  " + getSqlConvertDao().getVarchar()
                        + "(60) ");
                i++;
                sb.append(" ,\n");
                sb.append("    order_type  " + getSqlConvertDao().getVarchar()
                        + "(20) ");
                i++;
                sb.append(" ,\n");
                sb.append("    dyn_create_date " + getSqlConvertDao().getDate()
                        + " ");
                i++;
                sb.append(" ,\n");
                sb.append("    dyn_type  " + getSqlConvertDao().getVarchar()
                        + "(20) ");
                i++;
                sb.append(" ,\n");
                sb.append("    dyn_parent_id  "
                        + getSqlConvertDao().getNumber() + " ");
                i++;
                sb.append(" ,\n");
                sb.append("    user_id " + getSqlConvertDao().getNumber() + " ");
                i++;

                sb.append(" ,\n");
                sb.append("    user_name " + getSqlConvertDao().getVarchar()
                        + "(100) ");
                i++;
                sb.append(" ,\n");
                sb.append("    org_id " + getSqlConvertDao().getNumber() + " ");
                i++;

                // 编辑器字段
                if (bdFormInfo != null && bdFormInfo.getEditor() != null
                        && !bdFormInfo.getEditor().equals("")) {
                    sb.append(" ,\n"); // 2009-11-06增加:目的增加代录人.
                    sb.append("    ");
                    sb.append(bdFormInfo.getEditor());
                    sb.append(" " + getSqlConvertDao().getBlob());
                    i++;
                }

            }

            // 根据表获取字段
            iterField = collField.iterator();
            while (iterField.hasNext()) {
                bdFormFieldInfo = (DynFormFieldInfo) iterField.next();
                if (!bdFormFieldInfo.getTableName().equals(tableName)) {
                    continue;
                }

                if (i > 0) {
                    sb.append(" ,\n");
                }
                i++;

                // 组合字段信息
                sb.append("    " + bdFormFieldInfo.getZDMC());
                if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("int")) {

                    sb.append(" " + getSqlConvertDao().getNumber() + "");

                } else if (bdFormFieldInfo.getZDLX()
                        .equalsIgnoreCase("numeric")) {

                    sb.append(" " + getSqlConvertDao().getNumber() + " ");

                } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase(
                        "nvarchar")) {
                    sb.append(" " + getSqlConvertDao().getVarchar() + "(");
                    sb.append(bdFormFieldInfo.getZDCD());
                    sb.append(")");
                } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("ntext")) {
                    sb.append(" " + getSqlConvertDao().getVarchar() + "(");
                    sb.append((bdFormFieldInfo.getZDCD() == null || Integer
                            .valueOf(bdFormFieldInfo.getZDCD()) <= 0) ? 400
                            : bdFormFieldInfo.getZDCD());
                    sb.append(") ");
                } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("ndate")) {
                    // sb.append(" " + getSqlConvertDao().getDate() );
                    sb.append(" " + getSqlConvertDao().getVarchar() + "(20) ");
                } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("ntime")) {
                    // sb.append(" " + getSqlConvertDao().getTime() );
                    sb.append(" " + getSqlConvertDao().getVarchar() + "(20) ");
                } else {
                    sb.append(" " + getSqlConvertDao().getVarchar() + "(100) ");
                }

                if (bdFormFieldInfo.getZDKK() == 0) {
                    // sb.append(" NULL");
                } else {
                    sb.append(" NOT NULL");
                }
            }// while filed
            sb.append(" \n );\n");
        }

        sql = sb.toString();
        sb = null;

        return sql;

    }

    /**
     * 根据单个数据表的名称，字段属性，组合成SQL生成语句。
     *
     * @param tableId
     * @param tableName
     * @param collField
     * @return
     */
    public String genCreateSql2(String tableId, String tableName,
                                Collection collField) throws BaseAppException {

        StringBuffer sb = new StringBuffer("");
        String sql = "";
        Iterator iterField = null;

        String tableIdTemp = "";

        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append(" ( \n");

        // 根据表获取字段
        iterField = collField.iterator();
        int i = 0;

        sb.append("    dyn_id  " + getSqlConvertDao().getNumber() + " not null");
        i++;
        sb.append(" ,\n");
        sb.append("    dyn_form_id  " + getSqlConvertDao().getNumber() + " ");
        i++;
        sb.append(" ,\n");
        sb.append("    dyn_ver_id  " + getSqlConvertDao().getNumber() + " ");
        i++;
        sb.append(" ,\n");
        sb.append("    order_id  " + getSqlConvertDao().getVarchar() + "(60)");
        i++;
        sb.append(" ,\n");
        sb.append("    order_type  " + getSqlConvertDao().getVarchar()
                + "(20) ");
        i++;
        sb.append(" ,\n");
        sb.append("    dyn_create_date " + getSqlConvertDao().getDate() + " ");
        i++;
        sb.append(" ,\n");
        sb.append("    dyn_type  " + getSqlConvertDao().getVarchar() + "(20) ");
        i++;
        sb.append(" ,\n"); // 2009-10-27增加:目的是为了处理关联.
        sb.append("    dyn_parent_id  " + getSqlConvertDao().getNumber() + " ");
        i++;
        sb.append(" ,\n"); // 2009-11-06增加:目的增加代录人.
        sb.append("    user_id " + getSqlConvertDao().getNumber() + " ");
        i++;
        sb.append(" ,\n"); // 2009-11-06增加:目的增加代录人.
        sb.append("    user_name " + getSqlConvertDao().getVarchar() + "(100) ");
        i++;
        sb.append(" ,\n");
        sb.append("    org_id " + getSqlConvertDao().getNumber() + " ");
        i++;
        while (iterField.hasNext()) {
            bdFormFieldInfo = (DynFormFieldInfo) iterField.next();
            if (!bdFormFieldInfo.getTableId().equals(tableId)) {
                continue;
            }

            if (i > 0) {
                sb.append(" ,\n");
            }
            i++;

            // 组合字段信息
            sb.append("    " + bdFormFieldInfo.getZDMC());
            if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("int")) {

                sb.append(" " + getSqlConvertDao().getNumber() + "");

            } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("numeric")) {

                sb.append(" " + getSqlConvertDao().getNumber() + " ");

            } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("nvarchar")) {
                // access 只有varchar

                sb.append(" " + getSqlConvertDao().getVarchar() + "(");

                sb.append(bdFormFieldInfo.getZDCD());
                sb.append(")");
            } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("ntext")) {
                sb.append(" " + getSqlConvertDao().getVarchar() + "(");
                sb.append((bdFormFieldInfo.getZDCD() == null || Integer
                        .valueOf(bdFormFieldInfo.getZDCD()) <= 0) ? 400
                        : bdFormFieldInfo.getZDCD());
                sb.append(") ");
            } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("ndate")) {
                // sb.append(" " + getSqlConvertDao().getDate() );
                sb.append(" " + getSqlConvertDao().getVarchar() + "(20) ");
            } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("ntime")) {
                // sb.append(" " + getSqlConvertDao().getTime() );
                sb.append(" " + getSqlConvertDao().getVarchar() + "(20) ");
            }
            /*
			 * if (bdFormFieldInfo.getZDKK() == 0) { sb.append(" NULL"); } else
			 * { sb.append(" NOT NULL"); }
			 */

        } // while (iterTable.hasNext())

        sb.append(" \n );\n");
        sql = sb.toString();
        sb = null;

        return sql;

    }

    /**
     * 创建数据库，如果存在，则删除后再创建
     */
    public void genTable() throws BaseAppException {

        // 判断是否存在，存在先删除

        this.deleteTable();

        String sql = this.genMainCreateSql();
        getFormDbDao().modifyTable(sql);

    }

    /**
     * 删除表单下的所有数据表
     */
    public void deleteTable() throws BaseAppException {

        // 判断是否存在，存在先删除
        String tableName = "";

        collDetail = this.detailMap.values();
        if (collDetail == null) {
            return;
        }
        iterDetail = collDetail.iterator();
        while (iterDetail != null && iterDetail.hasNext()) {
            s_detail = (String[]) iterDetail.next();
            if (s_detail == null) {
                continue;
            }
            tableName = s_detail[1];
            if (tableName.equals("")) {
                continue;
            }

            if (getFormDbDao().isExistTableByName(tableName)) {
                getFormDbDao().dropTable(tableName);
            }
        }
    }

    public void modifyTable(Collection collTable1, Collection collField1,
                            Collection collTable2, Collection collField2)
            throws BaseAppException {

        // 2009-10-05 ken 修改,增加size==0的判断.
        if (collTable1 == null || collTable1.size() == 0) {
            // 新创建的
            this.genTable();
            return;
        }

        // 检查已经删除的表格

        Iterator iterTable1 = null;
        String tableName = "";
        String tableId = "";
        String sql = "";
        iterTable1 = collTable1.iterator();
        while (iterTable1 != null && iterTable1.hasNext()) {
            bdFormTableInfo = (DynFormTableInfo) iterTable1.next();
            tableName = bdFormTableInfo.getGLBM();
            tableId = bdFormTableInfo.getGLID();
            // 不在新的表格中，说明已经删除：表这样判断是否相等是可以的。
            if (!this.tableEquals(tableName, collTable2)
                    && tableName.trim().length() > 0) {
                // 删除表
                if (getFormDbDao().isExistTableByName(tableName))
                    getFormDbDao().dropTable(tableName);
                // getFormDbDao().modifyTable(" DROP TABLE "+tableName+";");
            }
        }

        // 检查新增加的表格
        Iterator iterTable2 = collTable2.iterator();
        while (iterTable2 != null && iterTable2.hasNext()) {
            bdFormTableInfo = (DynFormTableInfo) iterTable2.next();
            tableName = bdFormTableInfo.getGLBM();
            tableId = bdFormTableInfo.getGLID();
            if (!this.tableEquals(tableName, collTable1)) {
                // 增加表
                sql = this.genCreateSql2(tableId, tableName, collField2);
                getFormDbDao().modifyTable(sql);
            }
        }

        // 检查已经修改的表
        iterTable2 = collTable2.iterator();
        Iterator iterField1 = null;
        Iterator iterField2 = null;
        StringBuffer sb = new StringBuffer("");
        StringBuffer sb2 = new StringBuffer("");

        int i = 0;
        while (iterTable2 != null && iterTable2.hasNext()) {
            bdFormTableInfo = (DynFormTableInfo) iterTable2.next();
            tableName = bdFormTableInfo.getGLBM();
            tableId = bdFormTableInfo.getGLID();
            // 表已经存在
            if (this.tableEquals(tableName, collTable2)) {
                // 已经删除的字段
                iterField1 = collField1.iterator();
                i = 0;
                while (iterField1 != null && iterField1.hasNext()) {
                    bdFormFieldInfo = (DynFormFieldInfo) iterField1.next();
                    if (!bdFormFieldInfo.getTableId().equals(tableId)) {
                        continue;
                    }
                    // 不存在的字段，已经删除
                    if (!this.fieldEquals(bdFormFieldInfo, collField2)) {

                        sb.append(" \n ");
                        sb.append(" alter table ");
                        sb.append(tableName);

                        sb.append(" drop column ");

                        sb.append(bdFormFieldInfo.getZDMC());
                        sb.append(";");
                        i++;
                    } // if (!collField2.equals(bdFormFieldInfo))

                }// while iterField1

                // 检查字段
                iterField2 = collField2.iterator();
                i = 0;
                while (iterField2 != null && iterField2.hasNext()) {
                    bdFormFieldInfo = (DynFormFieldInfo) iterField2.next();
                    if (!bdFormFieldInfo.getTableId().equals(tableId)) {
                        continue;
                    }
                    // 新增加的字段
                    if (!this.fieldEquals(bdFormFieldInfo, collField1)) {

                        sb.append(" \n ");
                        sb.append(" alter table ");
                        sb.append(tableName);
                        sb.append(" add \n");
                        sb2.append(" update ");
                        sb2.append(tableName);
                        sb2.append(" set \n");

                        sb.append(" " + bdFormFieldInfo.getZDMC());
                        sb2.append(" " + bdFormFieldInfo.getZDMC());
                        if (bdFormFieldInfo.getZDLX().equalsIgnoreCase("int")) {

                            sb.append(" " + getSqlConvertDao().getNumber()
                                    + " default 0");

                            sb2.append(" =0");
                        } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase(
                                "numeric")) {
                            sb2.append(" =0");

                            sb.append(" " + getSqlConvertDao().getNumber()
                                    + " ");

                        } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase(
                                "nvarchar")) {
                            sb2.append(" =''");

                            sb.append(" " + getSqlConvertDao().getVarchar()
                                    + "(");

                            sb.append(bdFormFieldInfo.getZDCD());
                            sb.append(" ) default ''");
                        } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase(
                                "ntext")) {
                            sb2.append(" =''");

                            sb.append(" " + getSqlConvertDao().getVarchar()
                                    + "(");
                            sb.append((bdFormFieldInfo.getZDCD() == null || Integer
                                    .valueOf(bdFormFieldInfo.getZDCD()) <= 0) ? 400
                                    : bdFormFieldInfo.getZDCD());
                            sb.append(") default ''");
                        } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase(
                                "ndate")) {
                            // sb.append(" " + getSqlConvertDao().getDate() );
                            sb.append(" " + getSqlConvertDao().getVarchar()
                                    + "(20) ");
                        } else if (bdFormFieldInfo.getZDLX().equalsIgnoreCase(
                                "ntime")) {
                            // sb.append(" " + getSqlConvertDao().getTime() );
                            sb.append(" " + getSqlConvertDao().getVarchar()
                                    + "(20) ");
                        } else {
                            sb.append(" " + getSqlConvertDao().getVarchar()
                                    + "(100) default ''");
                        }
                        sb.append(";");
                        sb2.append(";");
                        i++;
                    }
                }// while iterField1

            }// if collTable1 ends
        }// while iterTable2 ends
        sql = sb.toString();
        sb = null;
        // System.out.println("sql1===" + sql);
        if (!sql.equals("")) {
            getFormDbDao().modifyTable(sql);
        }

        sql = sb2.toString();
        sb2 = null;
        // System.out.println("sql2===" + sql);
        if (!sql.equals("")) {
            getFormDbDao().modifyTable(sql);
        }

    }

    public boolean tableEquals(String tableName, Collection collTable) {
        boolean flag = false;
        Iterator iterTable = collTable.iterator();
        String tableName2 = "";
        while (iterTable != null && iterTable.hasNext()) {
            bdFormTableInfo = (DynFormTableInfo) iterTable.next();
            tableName2 = bdFormTableInfo.getGLBM();
            if (tableName2.equals(tableName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean fieldEquals(DynFormFieldInfo bdFormFieldInfo,
                               Collection collField) {
        boolean flag = false;
        Iterator iterField = collField.iterator();
        String tableName2 = "";
        DynFormFieldInfo bdFormFieldInfo2 = null;
        while (iterField != null && iterField.hasNext()) {
            bdFormFieldInfo2 = (DynFormFieldInfo) iterField.next();
            // 字段名称/类型/长度都一致
            if (bdFormFieldInfo.getZDMC().equals(bdFormFieldInfo2.getZDMC())
                    && bdFormFieldInfo.getZDLX().equals(
                    bdFormFieldInfo2.getZDLX())
                    && bdFormFieldInfo.getZDCD().equals(
                    bdFormFieldInfo2.getZDCD())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}