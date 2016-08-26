package com.ztesoft.uboss.bpm.setup.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.setup.dao.ITaskFormDAO;
import com.ztesoft.uboss.bpm.setup.model.BpmFormDto;
import com.ztesoft.uboss.bpm.setup.model.BpmFormQueryDto;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.utils.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TaskFormDAO extends BusiBaseDAO implements ITaskFormDAO {

    public int addTaskForm(BpmFormDto bpmFormDto) throws BaseAppException {

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("INSERT INTO BPM_FORM (FORM_ID, FORM_NAME, FORM_CODE,FORM_TYPE,APPLY_TYPE,DYN_FORM_ID,PAGE_URL, STATE, STATE_DATE, COMMENTS, CATG_ID)" +
                " VALUES (?, ?, ?,?,?,?, ?, 'A', ? ,?,?)");

        ParamArray pa = new ParamArray();
        pa.set("", bpmFormDto.getFormId());
        pa.set("", bpmFormDto.getFormName());
        pa.set("", bpmFormDto.getFormCode());

        pa.set("", bpmFormDto.getFormType());
        pa.set("", bpmFormDto.getApplyType());
        pa.set("", bpmFormDto.getDynamicFormId());
        pa.set("", bpmFormDto.getPageUrl());
        pa.set("", bpmFormDto.getStateDate());
        pa.set("", bpmFormDto.getComments());
        pa.set("", bpmFormDto.getCatgId());

        return this.executeUpdate(sqlBuffer.toString(), pa);
    }

    public int delTaskForm(long formId) throws BaseAppException {

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("UPDATE BPM_FORM SET STATE = 'X', STATE_DATE = NOW() WHERE FORM_ID = ?");

        ParamArray pa = new ParamArray();
        pa.set("", formId);

        return this.executeUpdate(sqlBuffer.toString(), pa);
    }

    public int modTaskForm(BpmFormDto bpmFormDto) throws BaseAppException {

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("UPDATE BPM_FORM SET FORM_NAME = ?,PAGE_URL = ?,COMMENTS=?,FORM_CODE=?,FORM_TYPE=?,APPLY_TYPE=?,DYN_FORM_ID=?,STATE_DATE=? WHERE FORM_ID = ?");

        ParamArray pa = new ParamArray();
        pa.set("", bpmFormDto.getFormName());
        pa.set("", bpmFormDto.getPageUrl());
        pa.set("", bpmFormDto.getComments());

        pa.set("", bpmFormDto.getFormCode());
        pa.set("", bpmFormDto.getFormType());
        pa.set("", bpmFormDto.getApplyType());
        pa.set("", bpmFormDto.getDynamicFormId());
        pa.set("", bpmFormDto.getStateDate());
        pa.set("", bpmFormDto.getFormId());

        return this.executeUpdate(sqlBuffer.toString(), pa);
    }

    private String getQryTaskFormListSQL(BpmFormQueryDto bpmFormDto, ParamMap pm) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("SELECT FORM_ID,A.FORM_NAME,A.FORM_CODE,PAGE_URL,A.STATE_DATE,A.COMMENTS,APPLY_TYPE,A.FORM_TYPE,b.form_name dyn_form_name,b.dyn_form_id  FROM BPM_FORM A " +
                "LEFT JOIN bfm_dyn_form b on a.dyn_form_id=b.dyn_form_id  WHERE A.STATE = 'A' ");

        if (StringUtil.isNotEmpty(bpmFormDto.getFormName())) {
            sqlBuffer.append(" AND A.FORM_NAME LIKE :FORM_NAME");
            pm.set("FORM_NAME", "%" + bpmFormDto.getFormName() + "%");
        }
        if (StringUtil.isNotEmpty(bpmFormDto.getCatgId())) {
            sqlBuffer.append(" AND A.CATG_ID = :CATG_ID");
            pm.set("CATG_ID", Integer.parseInt(bpmFormDto.getCatgId()));
        }

        if (StringUtil.isNotEmpty(bpmFormDto.getFormType())) {
            sqlBuffer.append(" AND A.FORM_TYPE = :FORM_TYPE");
            pm.set("FORM_TYPE", bpmFormDto.getFormType());
        }

        if (StringUtil.isNotEmpty(bpmFormDto.getApplyType())) {
            sqlBuffer.append(" AND A.APPLY_TYPE = :getApplyType");
            pm.set("getApplyType", bpmFormDto.getApplyType());
        }

        return sqlBuffer.toString();
    }

    public List<BpmFormDto> qryTaskFormList(BpmFormQueryDto bpmFormDto) throws BaseAppException {

        ParamMap pm = new ParamMap();

        return (List<BpmFormDto>) this.query(getQryTaskFormListSQL(bpmFormDto, pm), pm, bpmFormDto.getRowSetFormatter(),
                null, new RowSetMapper<List<BpmFormDto>>() {
                    public List<BpmFormDto> mapRows(RowSetOperator op, ResultSet rs,
                                                    int colNum, Object para) throws SQLException,
                            BaseAppException {
                        List<BpmFormDto> bpmFormList = new ArrayList<BpmFormDto>();
                        while (rs.next()) {
                            BpmFormDto temp = new BpmFormDto();
                            temp.setFormId(op.getLong(rs, "FORM_ID"));
                            temp.setFormName(op.getString(rs, "FORM_NAME"));
                            temp.setFormCode(op.getString(rs, "FORM_CODE"));
                            temp.setPageUrl(op.getString(rs, "PAGE_URL"));
                            temp.setStateDate(op.getDate(rs, "STATE_DATE"));
                            temp.setComments(op.getString(rs, "COMMENTS"));
                            temp.setApplyType(op.getString(rs, "APPLY_TYPE"));
                            temp.setFormType(op.getString(rs, "FORM_TYPE"));
                            temp.setDynamicFormName(op.getString(rs, "DYN_FORM_NAME"));
                            String dynFormId = op.getString(rs, "DYN_FORM_ID");
                            if (StringUtil.isNotEmpty(dynFormId)) {
                                temp.setDynamicFormId(Long.parseLong(dynFormId));
                            }

                            bpmFormList.add(temp);
                        }
                        return bpmFormList;
                    }
                });
    }

    @Override
    public BpmFormDto qryLinkFormDetail(long formId) throws BaseAppException {
        String sqlStr = ("SELECT FORM_ID,A.FORM_NAME,A.FORM_CODE,PAGE_URL,A.STATE_DATE,A.COMMENTS,APPLY_TYPE,A.FORM_TYPE FROM BPM_FORM A " +
                " WHERE A.STATE = 'A' ") +
                " AND A.FORM_ID = :FORM_ID";
        ParamMap pm = new ParamMap();
        pm.set("FORM_ID", formId);
        return this.selectObject(sqlStr, BpmFormDto.class, pm);

    }

    public long qryTaskFormListCount(BpmFormQueryDto bpmFormDto)
            throws BaseAppException {

        ParamMap pm = new ParamMap();

        String sql = new StringBuilder().append("select count(*) cnt from (")
                .append(getQryTaskFormListSQL(bpmFormDto, pm)).append(") a")
                .toString();

        return (Long) this.query(sql, pm, null,
                null, new RowSetMapper<Long>() {
                    public Long mapRows(RowSetOperator op, ResultSet rs,
                                        int colNum, Object para) throws SQLException,
                            BaseAppException {

                        long count = 0;

                        if (rs.next()) {
                            count = op.getLong(rs, 1);
                        }
                        return count;
                    }
                });

    }

}
