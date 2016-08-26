package com.ztesoft.zsmartcity.sgd.items.dao.impl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.sgd.base.helper.PageHelper;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;
import com.ztesoft.zsmartcity.sgd.items.dao.ItemDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao 2016-5-25.
 * sgd
 * 模糊查询与分页。
 */
public class ItemDAOImpl extends BusiBaseDAO implements ItemDAO {


    @Override
    public List<DynamicDict> qryItemsByParams(DynamicDict dict) throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append(" select s.*,b.ORG_NAME ORG_NAME  " +
                " from  sgd_items s   " +
                " LEFT JOIN bfm_org b " +
                " on s.ORG_ID = b.ORG_ID " +
                " where 1=1 ");
        sql.append(" and s.STATE  = 'A' ");
        sql.append("[ and  s.NAME  LIKE :NAME ]");
        sql.append("[ and  s.ITEM_CODE  like :ITEM_CODE]");
        sql.append("[ and  s.LEGAL_PERIOD = :LEGAL_PERIOD ]");
        sql.append("[ and (UNIX_TIMESTAMP(s.CREATE_DATE) - UNIX_TIMESTAMP(:CREATE_DATE_BEGIN) >= 0) ]");
        sql.append("[ AND ( UNIX_TIMESTAMP(s.CREATE_DATE ) - UNIX_TIMESTAMP(:CREATE_DATE_END)) <= 0  ]");
        sql.append("[ and  s.ORG_ID = :ORG_ID ]");
        sql.append("[ and  s.SP_TYPE_ID = :SP_TYPE_ID ]");
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "NAME", dict.getString("NAME"));
        ParamMapHelper.setValue(pm, "ITEM_CODE", dict.getString("ITEM_CODE"));
        ParamMapHelper.setValue(pm, "LEGAL_PERIOD", dict.getString("LEGAL_PERIOD"));
        ParamMapHelper.setValue(pm, "CREATE_DATE_BEGIN", dict.getString("CREATE_DATE_BEGIN"));
        ParamMapHelper.setValue(pm, "CREATE_DATE_END", dict.getString("CREATE_DATE_END"));
        ParamMapHelper.setValue(pm, "ORG_ID", dict.getString("ORG_ID"));
        ParamMapHelper.setValue(pm, "SP_TYPE_ID", dict.getString("SP_TYPE_ID"));
        // 增加分页逻辑
        RowSetFormatter rf = PageHelper.getRowSetFormat(dict);
        return this.query(sql.toString(), pm, rf, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                    BaseAppException {
                List<DynamicDict> attrDefDtoList = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    int flag = 1;
                    op.getLong(rs, flag++);
                    DynamicDict dict = new DynamicDict();
                    dict.set("ID", op.getLong(rs, "ID"));
                    dict.set("PROC_DEF_ID",op.getString(rs,"PROC_DEF_ID"));
                    dict.set("NAME", op.getString(rs, "NAME"));
                    dict.set("ITEM_CODE", op.getString(rs, "ITEM_CODE"));
                    dict.set("SP_TYPE_ID", op.getLong(rs, "SP_TYPE_ID"));
                    dict.set("ORG_ID", op.getLong(rs, "ORG_ID"));
                    dict.set("SP_OBJECT", op.getString(rs, "SP_OBJECT"));
                    dict.set("SET_GIST", op.getString(rs, "SET_GIST"));
                    dict.set("DEAL_CONDITION", op.getString(rs, "DEAL_CONDITION"));
                    dict.set("LEGAL_PERIOD", op.getLong(rs, "LEGAL_PERIOD"));
                    dict.set("PROMISE_PERIOD", op.getLong(rs, "PROMISE_PERIOD"));
                    dict.set("FEE", op.getString(rs, "FEE"));
                    dict.set("STATE", op.getString(rs, "STATE"));
                    dict.set("CREATE_USER", op.getString(rs, "CREATE_USER"));
                    dict.set("CREATE_DATE", op.getString(rs, "CREATE_DATE"));
                    dict.set("UPDATE_USER", op.getString(rs, "UPDATE_USER"));
                    dict.set("UPDATE_DATE", op.getString(rs, "UPDATE_DATE"));
                    dict.set("COMMENTS", op.getString(rs, "COMMENTS"));
                    dict.set("ORG_NAME", op.getString(rs, "ORG_NAME"));
                    attrDefDtoList.add(dict);
                }
                return attrDefDtoList;
            }
        });


    }

    @Override
    public int qryItemsCountByParams(DynamicDict dict) throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(1)  " +
                " from  sgd_items s   " +
                " LEFT JOIN bfm_org b " +
                " on s.ORG_ID = b.ORG_ID " +
                " where 1=1 ");
        sql.append(" and s.STATE  = 'A' ");
        sql.append("[ and  s.NAME  LIKE :NAME ]");
        sql.append("[ and  s.ITEM_CODE  like :ITEM_CODE]");
        sql.append("[ and  s.LEGAL_PERIOD = :LEGAL_PERIOD ]");
        sql.append("[ and (UNIX_TIMESTAMP(s.CREATE_DATE) - UNIX_TIMESTAMP(:CREATE_DATE_BEGIN) >= 0) ]");
        sql.append("[ AND ( UNIX_TIMESTAMP(s.CREATE_DATE ) - UNIX_TIMESTAMP(:CREATE_DATE_END)) <= 0  ]");
        sql.append("[ and  s.ORG_ID = :ORG_ID ]");
        sql.append("[ and  s.SP_TYPE_ID = :SP_TYPE_ID ]");
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "NAME", dict.getString("NAME"));
        ParamMapHelper.setValue(pm, "ITEM_CODE", dict.getString("ITEM_CODE"));
        ParamMapHelper.setValue(pm, "LEGAL_PERIOD", dict.getString("LEGAL_PERIOD"));
        ParamMapHelper.setValue(pm, "CREATE_DATE_BEGIN", dict.getString("CREATE_DATE_BEGIN"));
        ParamMapHelper.setValue(pm, "CREATE_DATE_END", dict.getString("CREATE_DATE_END"));
        ParamMapHelper.setValue(pm, "ORG_ID", dict.getString("ORG_ID"));
        ParamMapHelper.setValue(pm, "SP_TYPE_ID", dict.getString("SP_TYPE_ID"));
        return (int) selectCount(sql.toString(),pm);
    }

    @Override
    public int qryLawsCount(DynamicDict dict) throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(1) from sgd_laws s where 1=1 " +
                " and state ='A'" +
                "[ and name like :NAME]" +
                "[ and type_id =:TYPE_ID ]" +
                "[ and (UNIX_TIMESTAMP(s.EXE_DATE) - UNIX_TIMESTAMP(:EXE_DATE_BEGIN) )>= 0 ]" +
                "[ and ( UNIX_TIMESTAMP(s.EXE_DATE ) - UNIX_TIMESTAMP(:EXE_DATE_END)) <= 0 ]" +
                "[ and file_no like :FILE_NO]");
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "NAME", dict.getString("NAME"));
        ParamMapHelper.setValue(pm, "TYPE_ID", dict.getLong("TYPE_ID"));
        ParamMapHelper.setValue(pm, "EXE_DATE_BEGIN", dict.getDate("EXE_DATE_BEGIN"));
        ParamMapHelper.setValue(pm, "EXE_DATE_END", dict.getDate("EXE_DATE_END"));
        ParamMapHelper.setValue(pm, "FILE_NO", dict.getString("FILE_NO"));

        return (int) selectCount(sql.toString(),pm);
    }

    @Override
    public List<DynamicDict> qryLaws(DynamicDict dict) throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append(" select s.*, b.value_name  VALUE_NAME from sgd_laws s  LEFT JOIN " +
                "  bfm_dictionary_data b  on  b.id = s.type_id " +
                " where 1=1 " +
                " and s.state ='A'" +
                "[ and name like :NAME]" +
                "[ and type_id =:TYPE_ID ]" +
                "[ and (UNIX_TIMESTAMP(s.EXE_DATE) - UNIX_TIMESTAMP(:EXE_DATE_BEGIN) )>= 0 ]" +
                "[ and ( UNIX_TIMESTAMP(s.EXE_DATE ) - UNIX_TIMESTAMP(:EXE_DATE_END)) <= 0 ]" +
                "[ and file_no like :FILE_NO]");
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "NAME", dict.getString("NAME"));
        ParamMapHelper.setValue(pm, "TYPE_ID", dict.getLong("TYPE_ID"));
        ParamMapHelper.setValue(pm, "EXE_DATE_BEGIN", dict.getDate("EXE_DATE_BEGIN"));
        ParamMapHelper.setValue(pm, "EXE_DATE_END", dict.getDate("EXE_DATE_END"));
        ParamMapHelper.setValue(pm, "FILE_NO", dict.getString("FILE_NO"));
        // 增加分页逻辑
        RowSetFormatter rf =PageHelper.getRowSetFormat(dict);
        return this.query(sql.toString(), pm, rf, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                    BaseAppException {
                List<DynamicDict> attrDefDtoList = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    int flag = 1;
                    op.getLong(rs, flag++);
                    DynamicDict dict = new DynamicDict();
                    dict.set("ID", op.getLong(rs, "ID"));
                    dict.set("NAME", op.getString(rs, "NAME"));
                    dict.set("TYPE_ID", op.getLong(rs, "TYPE_ID"));
                    dict.set("FILE_NO", op.getString(rs, "FILE_NO"));
                    dict.set("SCOPE", op.getString(rs, "SCOPE"));
                    dict.set("EXE_DATE", op.getString(rs, "EXE_DATE"));
                    dict.set("PUB_DEPARTMENT", op.getString(rs, "PUB_DEPARTMENT"));
                    dict.set("PUB_DATE", op.getDate(rs, "PUB_DATE"));
                    dict.set("PUB_SUMMARY", op.getString(rs, "PUB_SUMMARY"));
                    dict.set("FILE", op.getString(rs, "FILE"));
                    dict.set("STATE", op.getString(rs, "STATE"));
                    dict.set("CREATE_USER", op.getLong(rs, "CREATE_USER"));
                    dict.set("CREATE_DATE", op.getString(rs, "CREATE_DATE"));
                    dict.set("UPDATE_USER", op.getLong(rs, "UPDATE_USER"));
                    dict.set("UPDATE_DATE", op.getString(rs, "UPDATE_DATE"));
                    dict.set("COMMENTS", op.getString(rs, "COMMENTS"));
                    dict.set("TYPE_NAME", op.getString(rs, "VALUE_NAME"));
                    attrDefDtoList.add(dict);
                }
                return attrDefDtoList;
            }
        });
    }
}
