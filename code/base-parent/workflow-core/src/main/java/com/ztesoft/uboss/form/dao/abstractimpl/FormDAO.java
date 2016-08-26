package com.ztesoft.uboss.form.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.uboss.form.dao.IFormDAO;
import com.ztesoft.uboss.form.model.*;
import com.ztesoft.uboss.form.sz600.formEngine2.DynFormFieldInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamArray;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.ParamObject;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FormDAO extends BusiBaseDAO implements IFormDAO {

    public long addForm(DynFormDto dynFormDto) throws BaseAppException {
        // TODO Auto-generated method stub
        long formId = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM",
                "DYN_FORM_ID");
        dynFormDto.setDynFormId(formId);
        dynFormDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormDto.setModifyTime(dynFormDto.getCreateTime());
        dynFormDto.setFormState("A");

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM(");
        sqlbuilder
                .append("         DYN_FORM_ID, FORM_NAME, FORM_CODE, FORM_TABLE, FORM_TYPE, FORM_DOMAIN, USER_ID, CREATE_TIME, MODIFY_TIME, FORM_STATE, FORM_DESC, CAT_ID)");
        sqlbuilder
                .append(" VALUES (:DYN_FORM_ID,:FORM_NAME,:FORM_CODE,:FORM_TABLE,:FORM_TYPE,:FORM_DOMAIN,:USER_ID,:CREATE_TIME,:MODIFY_TIME,:FORM_STATE,:FORM_DESC,:CAT_ID)");

        this.updateObject(sqlbuilder.toString(), dynFormDto);
        return formId;
    }

    public void addForm(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

        DynFormDto dynFormDto = new DynFormDto();
        long formId = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM",
                "DYN_FORM_ID");
        dynFormDto.setDynFormId(formId);
        dynFormDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormDto.setModifyTime(dynFormDto.getCreateTime());
        dynFormDto.setFormState("A");
        dynFormDto.setFormCode(dict.getString(""));

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM(");
        sqlbuilder
                .append("         DYN_FORM_ID, FORM_NAME, FORM_CODE, FORM_TABLE, FORM_TYPE, FORM_DOMAIN, USER_ID, CREATE_TIME, MODIFY_TIME, FORM_STATE, FORM_DESC, CAT_ID)");
        sqlbuilder
                .append(" VALUES (:DYN_FORM_ID,:FORM_NAME,:FORM_CODE,:FORM_TABLE,:FORM_TYPE,:FORM_DOMAIN,:USER_ID,:CREATE_TIME,:MODIFY_TIME,:FORM_STATE,:FORM_DESC,:CAT_ID)");

        this.updateObject(sqlbuilder.toString(), dynFormDto);

    }

    public void updateForm(DynFormDto dynFormDto) throws BaseAppException {
        // TODO Auto-generated method stub

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" UPDATE BFM_DYN_FORM SET ");
        sqlbuilder.append(" MODIFY_TIME=:MODIFY_TIME ");
        if (dynFormDto.getFormName() != null) {
            sqlbuilder.append(" ,FORM_NAME=:FORM_NAME ");
        }
        if (dynFormDto.getFormCode() != null) {
            sqlbuilder.append(" ,FORM_CODE=:FORM_CODE ");
        }
        if (dynFormDto.getFormTable() != null) {
            sqlbuilder.append(" ,FORM_TABLE=:FORM_TABLE ");
        }
        if (dynFormDto.getFormType() != null) {
            sqlbuilder.append(" ,FORM_TYPE=:FORM_TYPE ");
        }
        if (dynFormDto.getFormDomain() != null) {
            sqlbuilder.append(" ,FORM_DOMAIN=:FORM_DOMAIN ");
        }
        if (dynFormDto.getFormState() != null) {
            sqlbuilder.append(" ,FORM_STATE=:FORM_STATE ");
        }
        if (dynFormDto.getFormDesc() != null) {
            sqlbuilder.append(" ,FORM_DESC=:FORM_DESC ");
        }
        if (dynFormDto.getCatId() != null) {
            sqlbuilder.append(" ,CAT_ID=:CAT_ID ");
        }
        sqlbuilder.append(" WHERE DYN_FORM_ID=:DYN_FORM_ID ");
        this.updateObject(sqlbuilder.toString(), dynFormDto);
    }

    public void updateForm(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void addFormVer(DynFormVerDto dynFormVerDto) throws BaseAppException {
        // TODO Auto-generated method stub

        long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM_VER", "ID");
        dynFormVerDto.setId(id);
        dynFormVerDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormVerDto.setVerState("A");

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM_VER(");
        sqlbuilder
                .append("         ID, DYN_FORM_ID, DESIGN_XML, DESIGN_HTML, VERSION, VER_STATE, CREATE_TIME, USER_ID)");
        sqlbuilder
                .append(" VALUES (:ID,:DYN_FORM_ID,:DESIGN_XML,:DESIGN_HTML,:VERSION,:VER_STATE,:CREATE_TIME,:USER_ID)");

        this.updateObject(sqlbuilder.toString(), dynFormVerDto);
    }

    public void addFormVer(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void addFormVer(String dynFormId, String xmlStr, String htmlStr,
                           String version, String userId) throws BaseAppException {
        // TODO Auto-generated method stub
        DynFormVerDto dynFormVerDto = new DynFormVerDto();
        long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM_VER", "ID");
        dynFormVerDto.setId(id);
        dynFormVerDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormVerDto.setVerState("A");
        dynFormVerDto.setDesignHtmlStr(htmlStr);
        dynFormVerDto.setDesignXmlStr(xmlStr);
        dynFormVerDto.setDynFormId(Long.valueOf(dynFormId));
        dynFormVerDto.setVersion(version);
        dynFormVerDto.setUserId(Long.valueOf(userId));

        try {
            if (dynFormVerDto.getDesignXml() == null) {
                dynFormVerDto.setDesignXml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (dynFormVerDto.getDesignHtml() == null) {
                dynFormVerDto.setDesignHtml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM_VER(");
        sqlbuilder
                .append("         ID, DYN_FORM_ID, DESIGN_XML, DESIGN_HTML, VERSION, VER_STATE, CREATE_TIME, USER_ID)");
        sqlbuilder
                .append(" VALUES (:ID,:DYN_FORM_ID,:DESIGN_XML,:DESIGN_HTML,:VERSION,:VER_STATE,:CREATE_TIME,:USER_ID)");


        this.updateObject(sqlbuilder.toString(), dynFormVerDto);

    }

    public void updateFormVer(DynFormVerDto dynFormVerDto)
            throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void updateFormVer(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

    }

    public void updateFormState(long dynFormId) throws BaseAppException {
        // TODO Auto-generated method stub

        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" UPDATE BFM_DYN_FORM_VER ")
                .append(" SET VER_STATE = 'X'  ")
                .append(" WHERE VER_STATE = 'A' AND DYN_FORM_ID=" + dynFormId);

        ParamArray pa = new ParamArray();
        executeUpdate(sqlStr.toString(), pa);

    }

    public void updateFormVerState(long dynFormId) throws BaseAppException {
        // TODO Auto-generated method stub

        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE BFM_DYN_FORM_VER ")
                .append("SET VER_STATE = 'X'  ")
                .append("WHERE VER_STATE = 'A' AND DYN_FORM_ID=? ");

        ParamArray pa = new ParamArray();
        pa.set("", dynFormId);


        executeUpdate(sqlStr.toString(), pa);

    }

    public void updateFormVerStateById(long dynFormVerId, String state)
            throws BaseAppException {
        // TODO Auto-generated method stub

        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("UPDATE BFM_DYN_FORM_VER ").append("SET VER_STATE = ?  ")
                .append("WHERE  ID=? ");

        ParamArray pa = new ParamArray();
        pa.set("", state);
        pa.set("", dynFormVerId);
        executeUpdate(sqlStr.toString(), pa);
    }

    public List<DynFormDto> queryFormList(DynFormDto dynFormDto)
            throws BaseAppException {
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder
                .append(" SELECT a.DYN_FORM_ID,a.FORM_NAME,a.FORM_CODE,a.FORM_TABLE,a.FORM_TYPE");
        sqlbuilder
                .append(",a.FORM_DOMAIN,USER_ID,a.CREATE_TIME,a.MODIFY_TIME,a.FORM_STATE,a.FORM_DESC,a.CAT_ID ");
        sqlbuilder.append(",b.CAT_NAME ");
        sqlbuilder.append(" FROM BFM_DYN_FORM a ");
        sqlbuilder.append(" LEFT JOIN bfm_dyn_form_cat b on a.cat_id=b.id ");
        sqlbuilder.append(" WHERE 1=1 ");

        if (dynFormDto.getFormType() != null
                && dynFormDto.getFormType().length() > 0) {
            sqlbuilder.append(" and  a.FORM_TYPE =:FORM_TYPE ");
            pm.set("FORM_TYPE", dynFormDto.getFormType());
        }

        if (dynFormDto.getFormState() != null
                && dynFormDto.getFormState().length() > 0) {
            sqlbuilder.append(" and  a.FORM_STATE =:FORM_STATE ");
            pm.set("FORM_STATE", dynFormDto.getFormState());
        } else {
            sqlbuilder.append(" and  a.FORM_STATE ='A' ");
        }
        if (dynFormDto.getFormName() != null
                && dynFormDto.getFormName().trim().length() > 0) {
            sqlbuilder.append(" AND lower(a.FORM_NAME) like '%"
                    + dynFormDto.getFormName().trim().toLowerCase().trim()
                    + "%' ");
        }
        if (dynFormDto.getCatId() > 0) {
            sqlbuilder.append(" AND a.CAT_ID =:CAT_ID ");
            pm.set("CAT_ID", dynFormDto.getCatId());

        }
        sqlbuilder.append(" order by DYN_FORM_ID desc ");
        return this.selectList(sqlbuilder.toString(), DynFormDto.class, pm);

    }

    public int queryFormCount(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" SELECT COUNT(1) COUNT FROM BFM_DYN_FORM  WHERE 1=1  ");

        ParamArray pa = new ParamArray();
        if (dict.getString("FORM_NAME") != null
                && dict.getString("FORM_NAME").trim().length() > 0) {
            sqlStr.append(" AND lower(FORM_NAME) like '%"
                    + dict.getString("FORM_NAME").trim().toLowerCase().trim()
                    + "%' ");
        }
        if (dict.getString("FORM_TYPE") != null) {
            sqlStr.append(" AND FORM_TYPE ='" + dict.getString("FORM_TYPE")
                    + "'");
        }
        if (dict.getString("FORM_STATE") != null) {
            sqlStr.append(" AND FORM_STATE='" + dict.getString("FORM_STATE")
                    + "'");

        }
        if (dict.getLong("CAT_ID") != null) {
            sqlStr.append(" AND CAT_ID =" + dict.getLong("CAT_ID")
            );
        }
        return (Integer) this.query(sqlStr.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        String count = null;
                        if (rs.next()) {
                            count = rs.getString("COUNT");
                        }
                        return count == null ? 0 : Integer.parseInt(count);
                    }
                });
    }

    public List<DynamicDict> queryFormStepList(DynamicDict dict)
            throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" SELECT a.* ");
        sqlStr.append(" ,b.CAT_NAME ");
        sqlStr.append(" FROM BFM_DYN_FORM a ");

        sqlStr.append(" LEFT JOIN bfm_dyn_form_cat b on a.cat_id=b.id ");
        sqlStr.append("  WHERE 1=1  ");
        ParamMap pm = new ParamMap();
        if (dict.getString("FORM_NAME") != null
                && dict.getString("FORM_NAME").trim().length() > 0) {
            sqlStr.append(" AND lower(a.FORM_NAME) like '%"
                    + dict.getString("FORM_NAME").trim().toLowerCase().trim()
                    + "%' ");
        }
        if (dict.getString("FORM_TYPE") != null) {
            sqlStr.append(" AND a.FORM_TYPE ='" + dict.getString("FORM_TYPE")
                    + "'");
        }
        if (dict.getString("FORM_STATE") != null) {

            sqlStr.append(" AND a.FORM_STATE='" + dict.getString("FORM_STATE")
                    + "'");
        }
        if (dict.getLong("CAT_ID") != null) {
            sqlStr.append(" AND a.CAT_ID =" + dict.getLong("CAT_ID")
            );
        }
        sqlStr.append(" order by form_name,modify_time desc,dyn_form_id desc ");
        BfmCommonStepDto bfmCommonStepDto = (BfmCommonStepDto) BoHelper.boToDto(dict, BfmCommonStepDto.class);
        return (List<DynamicDict>) this.query(sqlStr.toString(), pm, bfmCommonStepDto.getRowSetFormatter(),
                null, new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {
                        List<DynamicDict> list = new ArrayList<DynamicDict>();

                        String colNames[] = op.getColNames();
                        while (rs.next()) {
                            DynamicDict temp = new DynamicDict();
                            for (int i = 0; i < colNames.length; i++) {
                                temp.set(colNames[i],
                                        op.getValue(rs, colNames[i]));

                            }
                            if (temp.getString("FORM_TYPE") != null
                                    && temp.getString("FORM_TYPE").equals("1")) {
                                temp.set("FORM_TYPE_NAME", "流程表单");
                            } else if (temp.getString("FORM_TYPE") != null
                                    && temp.getString("FORM_TYPE").equals("2")) {
                                temp.set("FORM_TYPE_NAME", "环节表单");
                            } else if (temp.getString("FORM_TYPE") != null
                                    && temp.getString("FORM_TYPE").equals("3")) {
                                temp.set("FORM_TYPE_NAME", "非流程表单");
                            } else {
                                temp.set("FORM_TYPE_NAME",
                                        temp.getString("FORM_TYPE"));
                            }

                            if (temp.getString("FORM_STATE") != null
                                    && temp.getString("FORM_STATE").equals("A")) {
                                temp.set("FORM_STATE_NAME", "有效");
                            } else {
                                temp.set("FORM_STATE_NAME", "无效");
                            }
                            list.add(temp);
                        }
                        return list;
                    }
                });
    }

    public List<DynFormVerDto> queryFormVerList(long dynFormId)
            throws BaseAppException {
        // TODO Auto-generated method stub
        return null;
    }

    public DynFormDto queryForm(long dynFormId) throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" SELECT * ");
        sqlbuilder.append(" FROM BFM_DYN_FORM ");
        sqlbuilder.append(" WHERE FORM_STATE = 'A' AND DYN_FORM_ID=:DYN_FORM_ID ");
        pm.set("DYN_FORM_ID", dynFormId);
        return this.selectObject(sqlbuilder.toString(), DynFormDto.class, pm);
    }

    public DynFormVerDto queryFormVer(long verId) throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder
                .append(" SELECT ID, DYN_FORM_ID, DESIGN_XML, DESIGN_HTML, VERSION, VER_STATE, CREATE_TIME, USER_ID ");
        sqlbuilder.append(" FROM BFM_DYN_FORM_VER ");
        sqlbuilder.append(" WHERE ID=:ID  ORDER BY ID DESC ");
        pm.set("ID", verId);
        return this
                .selectObject(sqlbuilder.toString(), DynFormVerDto.class, pm);

    }

    public DynFormVerDto queryActiveFormVer(long dynFormId)
            throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder
                .append(" SELECT ID, DYN_FORM_ID, DESIGN_XML, DESIGN_HTML, VERSION, VER_STATE, CREATE_TIME, USER_ID ");
        sqlbuilder.append(" FROM BFM_DYN_FORM_VER ");
        sqlbuilder
                .append(" WHERE VER_STATE = 'A' AND DYN_FORM_ID=:DYN_FORM_ID  ORDER BY ID DESC ");
        pm.set("DYN_FORM_ID", dynFormId);
        return this
                .selectObject(sqlbuilder.toString(), DynFormVerDto.class, pm);

    }

    private String inputStream2String(InputStream is) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is,
                    "GBK"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private InputStream String2InputStream(String str) {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    public void addFormVer(String dynFormId, String version, String userId)
            throws BaseAppException {
        // TODO Auto-generated method stub
        DynFormVerDto dynFormVerDto = new DynFormVerDto();
        long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM_VER", "ID");
        dynFormVerDto.setId(id);
        dynFormVerDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormVerDto.setVerState("A");
        dynFormVerDto.setDynFormId(Long.valueOf(dynFormId));
        dynFormVerDto.setVersion(version);
        dynFormVerDto.setUserId(Long.valueOf(userId));

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM_VER(");
        sqlbuilder
                .append("         ID, DYN_FORM_ID,  VERSION, VER_STATE, CREATE_TIME, USER_ID)");
        sqlbuilder
                .append(" VALUES (:ID,:DYN_FORM_ID,:VERSION,:VER_STATE,:CREATE_TIME,:USER_ID)");

        this.updateObject(sqlbuilder.toString(), dynFormVerDto);

    }

    public void addFormVer(String dynFormId, String xmlStr, String version,
                           String userId) throws BaseAppException {
        // TODO Auto-generated method stub
        DynFormVerDto dynFormVerDto = new DynFormVerDto();
        long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM_VER", "ID");
        dynFormVerDto.setId(id);
        dynFormVerDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormVerDto.setVerState("A");
        dynFormVerDto.setDesignXmlStr(xmlStr);
        dynFormVerDto.setDynFormId(Long.valueOf(dynFormId));
        dynFormVerDto.setVersion(version);
        dynFormVerDto.setUserId(Long.valueOf(userId));

        try {
            if (dynFormVerDto.getDesignXml() == null) {
                dynFormVerDto.setDesignXml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM_VER(");
        sqlbuilder
                .append("         ID, DYN_FORM_ID, DESIGN_XML, VERSION, VER_STATE, CREATE_TIME, USER_ID)");
        sqlbuilder
                .append(" VALUES (:ID,:DYN_FORM_ID,:DESIGN_XML,:VERSION,:VER_STATE,:CREATE_TIME,:USER_ID)");


        this.updateObject(sqlbuilder.toString(), dynFormVerDto);

    }

    public void updateFormVerXML(String dynFormId, String xml)
            throws BaseAppException {
        // TODO Auto-generated method stub
        DynFormVerDto dynFormVerDto = new DynFormVerDto();
        dynFormVerDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormVerDto.setVerState("A");
        dynFormVerDto.setDesignXmlStr(xml);
        dynFormVerDto.setDynFormId(Long.valueOf(dynFormId));

        try {
            if (dynFormVerDto.getDesignXml() == null) {
                dynFormVerDto.setDesignXml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (dynFormVerDto.getDesignHtml() == null) {
                dynFormVerDto.setDesignHtml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("UPDATE BFM_DYN_FORM_VER SET ");
        sqlbuilder.append("   DESIGN_XML=:DESIGN_XML ");
        sqlbuilder.append(" WHERE DYN_FORM_ID=:DYN_FORM_ID AND VER_STATE='A' ");

        this.updateObject(sqlbuilder.toString(), dynFormVerDto);

    }

    public void updateFormVerHTML(String dynFormId, String html)
            throws BaseAppException {
        // TODO Auto-generated method stub
        DynFormVerDto dynFormVerDto = new DynFormVerDto();
        dynFormVerDto.setCreateTime(DateUtil.GetDBDateTime());
        dynFormVerDto.setVerState("A");
        dynFormVerDto.setDesignHtmlStr(html);
        dynFormVerDto.setDynFormId(Long.valueOf(dynFormId));

        try {
            if (dynFormVerDto.getDesignXml() == null) {
                dynFormVerDto.setDesignXml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (dynFormVerDto.getDesignHtml() == null) {
                dynFormVerDto.setDesignHtml((new String("")).getBytes("UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("UPDATE BFM_DYN_FORM_VER SET ");
        sqlbuilder.append("   DESIGN_HTML=:DESIGN_HTML ");
        sqlbuilder.append(" WHERE DYN_FORM_ID=:DYN_FORM_ID AND VER_STATE='A' ");

        this.updateObject(sqlbuilder.toString(), dynFormVerDto);

    }

    public String queryFormHTML(String dynFormId) throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" SELECT DESIGN_HTML ");
        sqlbuilder.append(" FROM BFM_DYN_FORM_VER ");
        sqlbuilder
                .append(" WHERE VER_STATE = 'A' AND DYN_FORM_ID=:DYN_FORM_ID  ORDER BY ID DESC ");
        pm.set("DYN_FORM_ID", Long.valueOf(dynFormId));
        DynFormVerDto ver = this.selectObject(sqlbuilder.toString(),
                DynFormVerDto.class, pm);
        if (ver != null && ver.getDesignHtmlStr().length() > 0) {
            return ver.getDesignHtmlStr();
        } else
            return "";

    }

    public String queryFormXML(String dynFormId) throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" SELECT DESIGN_XML ");
        sqlbuilder.append(" FROM BFM_DYN_FORM_VER ");
        sqlbuilder
                .append(" WHERE VER_STATE = 'A' AND DYN_FORM_ID=:DYN_FORM_ID  ORDER BY ID DESC ");
        pm.set("DYN_FORM_ID", Long.valueOf(dynFormId));
        DynFormVerDto ver = this.selectObject(sqlbuilder.toString(),
                DynFormVerDto.class, pm);
        if (ver != null && ver.getDesignXmlStr().length() > 0) {
            return ver.getDesignXmlStr();
        } else
            return "";
    }

    public long queryMaxVerId(String dynFormId, String state)
            throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" select max(ID) as ID   ");
        sqlbuilder.append(" from BFM_DYN_FORM_VER  ");
        sqlbuilder
                .append(" WHERE VER_STATE =:VER_STATE  AND DYN_FORM_ID=:DYN_FORM_ID  ");
        pm.set("VER_STATE", state);
        pm.set("DYN_FORM_ID", Long.valueOf(dynFormId));

        return (long) this.query(sqlbuilder.toString(), pm, null, null,
                new RowSetMapper<Long>() {
                    public Long mapRows(RowSetOperator op, ResultSet rs,
                                        int colNum, Object para) throws SQLException,
                            BaseAppException {

                        long count = 0;

                        if (rs.next()) {
                            if (op.getLong(rs, 1) != null)
                                count = op.getLong(rs, 1);
                        }
                        return count;
                    }
                });
    }

    public int saveFormData(String sql, DynamicDict varMap,
                            ArrayList<DynFormFieldInfo> formFieldList) throws BaseAppException {
        ParamArray pa = new ParamArray();
        long dynId = SeqUtil.getBackServiceDBUtil().getMaxValue(
                formFieldList.get(0).getTableName(), "DYN_ID");
        pa.set("", dynId);
        if (varMap.get("ORDER_ID") != null) {
            String orderId = varMap.getString("ORDER_ID");
            pa.set("", orderId);
        } else {
            pa.set("", 0);
        }
        //liuhao  作用是流程起始表单，保存一个USER_ID进表里。
        pa.set("", varMap.getString("USER_ID"));
        for (DynFormFieldInfo field : formFieldList) {
            setListVal(field.getZDID(), field.getZDLX(), varMap, pa);
        }
        this.executeUpdate(sql, pa);
        return 0;
    }

    private void setListVal(String key, String type, DynamicDict varMap,
                            ParamArray pa) throws BaseAppException {
        if (type.contains("int")) {
            pa.set("", varMap.getLong(key));
        } else if (type.contains("varchar")) {
            pa.set("", varMap.getString(key));
        } else if (type.contains("text")) {
            pa.set("", varMap.getString(key));
        } else if (type.contains("num")) {
            pa.set("", varMap.getLong(key));
        } else {
            pa.set("", varMap.getString(key));
        }
    }

    public List<Map> getHtmlIdToTableColumn(String dynFormId,
                                            String dynFormVerId) throws BaseAppException {
        // TODO Auto-generated method stub
        return null;
    }

    public DynamicDict qryDynFormData(String tableName, String holderNo)
            throws BaseAppException {
        String sql = "select * from " + tableName + " where order_id=?";
        ParamArray pa = new ParamArray();
        pa.set("", holderNo);

        return (DynamicDict) this.query(sql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        DynamicDict temp = new DynamicDict();
                        String[] colNames = op.getColNames();
                        if (rs.next()) {
                            for (int colIndex = 0; colIndex < colNum; colIndex++) {
                                Object value = op.getValue(rs, colIndex + 1);

                                if (value instanceof java.sql.Timestamp) {
                                    value = new java.sql.Date(
                                            ((java.sql.Timestamp) value)
                                                    .getTime());
                                } else if (value instanceof java.util.Date) {
                                    value = new java.sql.Date(
                                            ((java.util.Date) value).getTime());
                                }
                                temp.setValueByName(colNames[colIndex], value);
                            }
                        }
                        return temp;
                    }
                });
    }

    public int[] saveTaskFormData(String taskListId, List<DynamicDict> fieldList)
            throws BaseAppException {
        String sqlStr = "INSERT INTO BPM_DYN_TASK_RESULT(ID,TASK_LIST_ID,KEY_,VALUE_,VALUE_EXT) VALUES (:ID,:TASK_LIST_ID,:KEY_,:VALUE_,:VALUE_EXT)";
        ParamObject[] paraList = ParamObject.newParamObjectList(5,
                fieldList.size());
        int i = 0;
        for (DynamicDict field : fieldList) {
            paraList[0].setBatchElement("ID", UUID.randomUUID().toString(), i);
            paraList[1].setBatchElement("TASK_LIST_ID", taskListId, i);
            paraList[2].setBatchElement("KEY_", field.getString("KEY_"), i);
            paraList[3].setBatchElement("VALUE_", field.getString("VALUE_"), i);
            paraList[4].setBatchElement("VALUE_EXT",
                    field.getString("VALUE_EXT"), i);
            i++;
        }
        return executeBatch(sqlStr.toString(), ParamMap.wrap(paraList));
    }

    public List<DynTaskResult> queryDynTaskResult(String taskListId)
            throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" SELECT ID,TASK_LIST_ID,KEY_,VALUE_,VALUE_EXT  ");
        sqlbuilder.append(" FROM BPM_DYN_TASK_RESULT ");
        sqlbuilder.append(" WHERE TASK_LIST_ID = :TASK_LIST_ID ");
        pm.set("TASK_LIST_ID", taskListId);
        List<DynTaskResult> result = this.selectList(sqlbuilder.toString(),
                DynTaskResult.class, pm);
        return result;
    }

    public List<DynamicDict> queryDynFormDataList(String sql, ParamMap pm)
            throws BaseAppException {
        // TODO Auto-generated method stub
        if (sql == null || sql.length() <= 0)
            return null;

        return (List<DynamicDict>) this.query(sql, pm, null, null,
                new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {
                        List<DynamicDict> list = new ArrayList<DynamicDict>();
                        String colNames[] = op.getColNames();
                        while (rs.next()) {
                            DynamicDict temp = new DynamicDict();
                            for (int i = 0; i < colNames.length; i++) {
                                Object value = op.getValue(rs, colNames[i]);

                                if (value instanceof java.sql.Timestamp) {
                                    value = new java.sql.Date(
                                            ((java.sql.Timestamp) value)
                                                    .getTime());
                                } else if (value instanceof java.util.Date) {
                                    value = new java.sql.Date(
                                            ((java.util.Date) value).getTime());
                                }
                                temp.setValueByName(colNames[i], value);
                            }
                            list.add(temp);
                        }
                        return list;
                    }
                });
    }


    public List<DynamicDict> queryDynFormDataStepList(String sql, ParamMap pm,
                                                      RowSetFormatter rowSetFormatter) throws BaseAppException {
        // TODO Auto-generated method stub
        if (sql == null || sql.length() <= 0)
            return null;
        return (List<DynamicDict>) this.query(sql, pm, rowSetFormatter, null,
                new RowSetMapper<List<DynamicDict>>() {
                    public List<DynamicDict> mapRows(RowSetOperator op,
                                                     ResultSet rs, int colNum, Object para)
                            throws SQLException, BaseAppException {
                        List<DynamicDict> list = new ArrayList<DynamicDict>();

                        String colNames[] = op.getColNames();
                        while (rs.next()) {
                            DynamicDict temp = new DynamicDict();
                            for (int i = 0; i < colNames.length; i++) {
                                Object value = op.getValue(rs, colNames[i]);

                                if (value instanceof java.sql.Timestamp) {
                                    value = new java.sql.Date(
                                            ((java.sql.Timestamp) value)
                                                    .getTime());
                                } else if (value instanceof java.util.Date) {
                                    value = new java.sql.Date(
                                            ((java.util.Date) value).getTime());
                                }
                                temp.setValueByName(colNames[i], value);
                            }
                            list.add(temp);
                        }
                        return list;
                    }
                });
    }

    public int queryDynFormDataCount(String sql, ParamMap pm)
            throws BaseAppException {
        // TODO Auto-generated method stub
        if (sql == null || sql.length() <= 0)
            return 0;
        return (Integer) this.query(sql, pm, null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum,
                                  Object para) throws SQLException, BaseAppException {
                String count = null;
                if (rs.next()) {
                    count = rs.getString("COUNT");
                }
                return count == null ? 0 : Integer.parseInt(count);
            }
        });
    }

    public DynamicDict qryDynFormDataByDynId(String tableName, long dynId)
            throws BaseAppException {
        String sql = "select * from " + tableName + " where dyn_id=?";
        ParamArray pa = new ParamArray();
        pa.set("", dynId);

        return (DynamicDict) this.query(sql.toString(), pa, null,
                new RowSetMapper() {
                    public Object mapRows(RowSetOperator op, ResultSet rs,
                                          int colNum, Object para) throws SQLException,
                            BaseAppException {
                        DynamicDict temp = new DynamicDict();
                        String[] colNames = op.getColNames();
                        if (rs.next()) {
                            for (int colIndex = 0; colIndex < colNum; colIndex++) {
                                Object value = op.getValue(rs, colIndex + 1);

                                if (value instanceof java.sql.Timestamp) {
                                    value = new java.sql.Date(
                                            ((java.sql.Timestamp) value)
                                                    .getTime());
                                } else if (value instanceof java.util.Date) {
                                    value = new java.sql.Date(
                                            ((java.util.Date) value).getTime());
                                }
                                temp.setValueByName(colNames[colIndex], value);
                            }
                        }
                        return temp;
                    }
                });
    }

    @Override
    public int delDynTaskResult(String taskListId) throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" DELETE FROM BPM_DYN_TASK_RESULT WHERE TASK_LIST_ID = ?");
        return this.updateObject(sqlbuilder.toString(), taskListId);
    }

    @Override
    public long addCat(DynFormCatDto dynFormCatDto) throws BaseAppException {
        // TODO Auto-generated method stub
        if (dynFormCatDto.getId() <= 0) {
            long id = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM_CAT",
                    "ID");
            dynFormCatDto.setId(id);

        }

        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append("INSERT INTO BFM_DYN_FORM_CAT(");
        sqlbuilder
                .append("  ID, PARENT_ID, CAT_NAME, COMMENTS)");
        sqlbuilder
                .append(" VALUES (:ID,:PARENT_ID,:CAT_NAME,:COMMENTS)");

        this.updateObject(sqlbuilder.toString(), dynFormCatDto);
        return dynFormCatDto.getId();
    }

    @Override
    public void updateCat(DynFormCatDto dynFormCatDto) throws BaseAppException {
        // TODO Auto-generated method stub
        if (dynFormCatDto.getId() <= 0) return;
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" UPDATE BFM_DYN_FORM_CAT SET ID=ID ");

        if (dynFormCatDto.getParentId() >= 0)
            sqlbuilder.append(" ,PARENT_ID=:PARENT_ID ");
        if (dynFormCatDto.getCatName() != null)
            sqlbuilder.append(" ,CAT_NAME=:CAT_NAME ");
        if (dynFormCatDto.getComments() != null)
            sqlbuilder.append(" ,COMMENTS=:COMMENTS ");

        sqlbuilder.append(" WHERE ID=:ID ");
        this.updateObject(sqlbuilder.toString(), dynFormCatDto);
    }

    @Override
    public void deleteCat(DynFormCatDto dynFormCatDto) throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuilder sqlbuilder = new StringBuilder();
        sqlbuilder.append(" delete from BFM_DYN_FORM_CAT  ");
        sqlbuilder.append(" WHERE ID=:ID ");
        this.updateObject(sqlbuilder.toString(), dynFormCatDto);
    }

    @Override
    public long queryCatCount(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub

        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("  SELECT COUNT(1) COUNT FROM BFM_DYN_FORM_CAT  where 1=1 ");

        ParamMap pm = new ParamMap();
        if (dict.getString("CAT_NAME") != null
                && dict.getString("CAT_NAME").trim().length() > 0) {
            sqlStr.append(" and lower(CAT_NAME) like '%"
                    + dict.getString("CAT_NAME").trim().toLowerCase().trim()
                    + "%' ");
        }
        if (dict.getString("WHERE_SQL") != null
                && dict.getString("WHERE_SQL").trim().length() > 0) {
            sqlStr.append(" and " + dict.getString("WHERE_SQL").trim());
        }
        return this.selectCount(sqlStr.toString(), pm);

    }


    @Override
    public long isExist(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("  SELECT COUNT(1) COUNT FROM BFM_DYN_FORM_CAT  where ID!=" + dict.getLong("ID"));

        ParamMap pm = new ParamMap();
        if (dict.getString("CAT_NAME") != null
                && dict.getString("CAT_NAME").trim().length() > 0) {
            sqlStr.append(" and lower(CAT_NAME) = '"
                    + dict.getString("CAT_NAME").trim().toLowerCase().trim()
                    + "' ");
        }

        return this.selectCount(sqlStr.toString(), pm);
    }

    @Override
    public List<DynFormCatDto> queryCatList(DynamicDict dict)
            throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("  SELECT * FROM BFM_DYN_FORM_CAT  where 1=1 ");
        if (dict.getString("CAT_NAME") != null
                && dict.getString("CAT_NAME").trim().length() > 0) {
            sqlStr.append(" and lower(CAT_NAME) like '%"
                    + dict.getString("CAT_NAME").trim().toLowerCase().trim()
                    + "%' ");
        }
        if (dict.getString("WHERE_SQL") != null
                && dict.getString("WHERE_SQL").trim().length() > 0) {
            sqlStr.append(" and " + dict.getString("WHERE_SQL").trim());
        }
        sqlStr.append(" ORDER BY  ID  ");
        ParamMap pm = new ParamMap();

        return this.selectList(sqlStr.toString(), DynFormCatDto.class, pm);
    }

    @Override
    public List<DynFormCatDto> queryCatStepList(DynamicDict dict)
            throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("  SELECT * FROM BFM_DYN_FORM_CAT where 1=1 ");
        if (dict.getString("CAT_NAME") != null
                && dict.getString("CAT_NAME").trim().length() > 0) {
            sqlStr.append(" and lower(CAT_NAME) like '%"
                    + dict.getString("CAT_NAME").trim().toLowerCase().trim()
                    + "%' ");
        }
        sqlStr.append(" ORDER BY ID ");

        ParamMap pm = new ParamMap();
        BfmCommonStepDto brmCommonStepDto = (BfmCommonStepDto) BoHelper.boToDto(dict, BfmCommonStepDto.class);
        return this.selectList(sqlStr.toString(), DynFormCatDto.class, pm, brmCommonStepDto.getRowSetFormatter());
    }

    @Override
    public DynFormCatDto queryCat(DynamicDict dict) throws BaseAppException {
        // TODO Auto-generated method stub
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" SELECT * FROM BFM_DYN_FORM_CAT  WHERE ID =:ID   ");

        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "ID", dict.getLong("ID"));

        return this.selectObject(sqlStr.toString(), DynFormCatDto.class, pm);
    }

    @Override
    public void executeUpdate(String sql) throws BaseAppException {
        // TODO Auto-generated method stub
        ParamMap pm = new ParamMap();
        this.executeUpdate(sql, pm);
    }

    //825 liuhao
    @Override
    public int updateDynTableValue(DynamicDict bo) throws BaseAppException {
        // TODO Auto-generated method stub
        String sql = " update " + bo.getString("tablename") + " set " + bo.getString("COLUMN") + " = :VALUE"
                + " where ORDER_ID =" + "'" + bo.getString("ORDER_ID") + "'";
        ParamMap pm = new ParamMap();
        ParamMapHelper.setValue(pm, "VALUE", bo.getString("Value"));

        this.executeUpdate(sql, pm);
        return 0;
    }


}
