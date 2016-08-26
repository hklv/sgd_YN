package com.ztesoft.uboss.form.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.uboss.form.dao.IFormDAO;
import com.ztesoft.uboss.form.dao.IFormDbDAO;
import com.ztesoft.uboss.form.dao.ISqlConvertDAO;
import com.ztesoft.uboss.form.dao.mysqlimpl.FormDAOMySQL;
import com.ztesoft.uboss.form.dao.mysqlimpl.FormDbDAOMySQL;
import com.ztesoft.uboss.form.dao.mysqlimpl.SqlConvertDAOMySQL;
import com.ztesoft.uboss.form.model.*;
import com.ztesoft.uboss.form.sz600.formEngine2.DynForm;
import com.ztesoft.uboss.form.sz600.formEngine2.DynFormFieldInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.DBUtilDecorator;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

public class FormManager {
    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());

    private IFormDAO formDao = null;

    private IFormDbDAO formDbDao = null;

    private ISqlConvertDAO sqlConvert = null;

    private IFormDAO getFormDao() throws BaseAppException {
        if (formDao == null) {
            formDao = SgdDaoFactory.getDaoImpl(FormDAOMySQL.class);
        }
        return formDao;
    }

    private IFormDbDAO getFormDbDao() throws BaseAppException {
        if (formDbDao == null) {
            formDbDao = SgdDaoFactory.getDaoImpl(FormDbDAOMySQL.class);
        }
        return formDbDao;
    }

    private ISqlConvertDAO getSqlConvertDao() throws BaseAppException {
        if (sqlConvert == null) {
            sqlConvert = SgdDaoFactory.getDaoImpl(SqlConvertDAOMySQL.class);
        }
        return sqlConvert;
    }

    /**
     * 新增表单
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int addForm(DynamicDict dict) throws BaseAppException {
        DynFormDto dynFormDto = (DynFormDto) BoHelper.boToDto(dict, DynFormDto.class);
        long id = getFormDao().addForm(dynFormDto);
        dict.set("id", id);
        return 0;
    }

    public int addFormVer(DynamicDict dict) throws BaseAppException {

        getFormDao().updateFormVerState(dict.getLong("DYN_FORM_ID"));
        getFormDao().addFormVer(dict);
        return 0;
    }

    public int updateForm(DynamicDict dict) throws BaseAppException {
        DynFormDto dynFormDto = (DynFormDto) BoHelper.boToDto(dict, DynFormDto.class);
        dynFormDto.setModifyTime(DateUtil.GetDBDateTime());
        getFormDao().updateForm(dynFormDto);
        return 0;
    }

    public int updateFormVer(DynamicDict dict) throws BaseAppException {

        DynFormVerDto dynFormVerDto = (DynFormVerDto) BoHelper.boToDto(dict,
                DynFormVerDto.class);
        getFormDao().updateFormVer(dynFormVerDto);
        return 0;
    }

    public int copyForm(DynamicDict dict) throws BaseAppException {
        long oriId = dict.getLong("FORM_ID").longValue();
        DynFormDto dynFormDto = getFormDao().queryForm(oriId);
        String oriFormName = dynFormDto.getFormName();
        if (dict.getString("USER_ID") != null) {
            dynFormDto.setUserId(dict.getLong("USER_ID"));
        }
        long formId = SeqUtil.getBackServiceDBUtil().getMaxValue("BFM_DYN_FORM",
                "DYN_FORM_ID");
        if (dict.getString("FORM_NAME") != null) {
            dynFormDto.setFormName(dict.getString("FORM_NAME"));
        } else {
            dynFormDto.setFormName(dynFormDto.getFormName() + "_C" + formId);
        }
        if (dict.getString("FORM_CODE") != null) {
            dynFormDto.setFormCode(dict.getString("FORM_CODE"));
        } else {
            dynFormDto.setFormCode(dynFormDto.getFormCode() + "_C" + formId);
        }
        long id = getFormDao().addForm(dynFormDto);
        if (dynFormDto.getFormTable() != null
                && dynFormDto.getFormTable().trim().length() > 0) {
            DynFormDto dynFormDto2 = new DynFormDto();
            dynFormDto2.setDynFormId(id);
            dynFormDto2.setModifyTime(DateUtil.GetDBDateTime());
            if (dict.getString("FORM_TABLE") != null) {
                dynFormDto2.setFormTable(dict.getString("FORM_TABLE"));
            } else {
                dynFormDto2.setFormTable(dynFormDto.getFormTable() + "_" + id);
            }
            getFormDao().updateForm(dynFormDto2);
            // getFormDao().executeUpdate(" select * into "+dynFormDto2.getFormTable()+" from "+dynFormDto.getFormTable()+" where 1=2 ");
            getFormDao().executeUpdate(
                    " create table  " + dynFormDto2.getFormTable()
                            + " as select  t.* from "
                            + dynFormDto.getFormTable() + " t where 1=2 ");
        }
        DynFormVerDto dynFormVerDto = getFormDao().queryActiveFormVer(oriId);
        if (dynFormVerDto != null && dynFormVerDto.getId() > 0) {
            dynFormVerDto.setDynFormId(id);
            if (dict.getString("USER_ID") != null) {
                dynFormVerDto.setUserId(dict.getLong("USER_ID"));
            }
            String xml = dynFormVerDto.getDesignXmlStr();
            xml = xml.replaceAll(" formId=\"" + oriId + "\" ", " formId=\""
                    + id + "\" ");
            xml = xml.replaceAll(" formName=\"" + oriFormName + "\" ",
                    " formName=\"" + dynFormDto.getFormName() + "\" ");
            xml = xml.replaceAll(
                    " GLBM=\"" + dynFormDto.getFormTable() + "\" ", " GLBM=\""
                            + dynFormDto.getFormTable() + "_" + id + "\" ");
            dynFormVerDto.setDesignXmlStr(xml);

            getFormDao().addFormVer(dynFormVerDto);
        }

        dict.set("id", id);

        return 0;
    }

    public List<DynFormDto> queryFormList(DynamicDict dict)
            throws BaseAppException {
        return getFormDao().queryFormList(
                (DynFormDto) BoHelper.boToDto(dict, DynFormDto.class));
    }

    public int queryFormCount(DynamicDict dict) throws BaseAppException {
        return getFormDao().queryFormCount(dict);
    }

    public List<DynamicDict> queryFormStepList(DynamicDict dict)
            throws BaseAppException {
        return getFormDao().queryFormStepList(dict);
    }

    public DynFormDto queryFormDetail(DynamicDict dict) throws BaseAppException {
        return getFormDao().queryForm(dict.getLong("DYN_FORM_ID"));
    }

    public DynFormDto queryFormDetail(String formId) throws BaseAppException {
        return getFormDao().queryForm(Long.valueOf(formId));
    }

    public DynFormVerDto queryFormVerDetail(DynamicDict dict)
            throws BaseAppException {
        return getFormDao().queryActiveFormVer(dict.getLong("DYN_FORM_ID"));
    }

    public DynamicDict saveFormData(DynamicDict dict) throws BaseAppException {
        long dynFormId = dict.getLong("DYN_FORM_ID");
        long versionId = dict.getLong("VERSION_ID");

        ArrayList<DynFormFieldInfo> formFieldList = new DynForm().getFieldList(
                dynFormId, versionId);
        if (formFieldList == null || formFieldList.size() == 0) {
            ExceptionHandler.publish("Generate sql fail !",
                    ExceptionHandler.BUSS_ERROR);
        }
        String tableName = formFieldList.get(0).getTableName();
        AssertUtil.isNotEmpty(tableName);
        String sql = generInsertSql(formFieldList);
        DynamicDict varMap = dict.getBO("VAR_MAP");
        getFormDao().saveFormData(sql, varMap, formFieldList);
        return dict;
    }

    public DynamicDict saveTaskFormData(DynamicDict dict)
            throws BaseAppException {
        String taskListId = dict.getString("TASK_LIST_ID");
        List<DynamicDict> fieldList = dict.getList("fieldList");
        getFormDao().saveTaskFormData(taskListId, fieldList);
        return dict;
    }

    public DynamicDict modTaskFormData(DynamicDict dict)
            throws BaseAppException {
        String taskListId = dict.getString("TASK_LIST_ID");
        List<DynamicDict> fieldList = dict.getList("fieldList");
        getFormDao().delDynTaskResult(taskListId);
        getFormDao().saveTaskFormData(taskListId, fieldList);
        return dict;
    }

    /**
     * 查询环节处理结果(动态表单内容)
     *
     * @param dict
     * @throws BaseAppException
     */
    public void queryDynTaskResult(DynamicDict dict) throws BaseAppException {
        String taskListId = dict.getString("TASK_LIST_ID");
        List<DynTaskResult> result = getFormDao()
                .queryDynTaskResult(taskListId);
        BoHelper.listDtoToBO("RESULT_LIST", result, DynTaskResult.class, dict);
    }

    public DynamicDict qryDynFormData(DynamicDict dict) throws BaseAppException {
        String holderNo = dict.getString("HOLDER_NO");
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        if (formFieldList == null || formFieldList.size() == 0) {
            // ExceptionHandler.publish("Query form fail!",ExceptionHandler.BUSS_ERROR);
            return dict;
        }
        String tableName = formFieldList.get(0).getTableName();
        DynamicDict data = getFormDao().qryDynFormData(tableName, holderNo);
        for (DynFormFieldInfo field : formFieldList) {
            dict.set(field.getZDID(), data.get(field.getZDMC()));
        }
        return dict;
    }

    /**
     * 根据动态表单结构生成sql
     *
     * @param formFieldList
     * @return
     * @throws BaseAppException
     */
    public String generInsertSql(ArrayList<DynFormFieldInfo> formFieldList)
            throws BaseAppException {
        StringBuffer sqlInsert = new StringBuffer();
        String tableName = formFieldList.get(0).getTableName();
        sqlInsert.append("INSERT INTO ");
        sqlInsert.append(tableName);
        sqlInsert.append(" (DYN_ID,ORDER_ID,");
        //liuhao
        sqlInsert.append("user_id,");
        //end
        StringBuffer sqlVal = new StringBuffer();
        sqlVal.append(" VALUES(?,?");
        //liuhao
        sqlVal.append(",?");
        for (DynFormFieldInfo field : formFieldList) {
            sqlInsert.append(field.getZDMC());
            sqlInsert.append(",");
            sqlVal.append(",?");
        }
        sqlInsert.deleteCharAt(sqlInsert.length() - 1);
        sqlInsert.append(")");
        sqlVal.append(")");
        sqlInsert.append(sqlVal);
        logger.debug("insert sql == " + sqlInsert);
        return sqlInsert.toString();
    }

    /**
     * 生成与sql对应的变量顺序
     *
     * @return
     */
    public List<String> getVarSeq(ArrayList<DynFormFieldInfo> formFieldList) {
        List<String> list = new ArrayList<String>();
        for (DynFormFieldInfo field : formFieldList) {
            list.add(field.getZDID());
        }
        return list;
    }

    public DynFormVerDto queryFormVerDetail(String formId)
            throws BaseAppException {
        return getFormDao().queryActiveFormVer(Long.valueOf(formId));

    }

    public DynFormVerDto queryFormVerDetailByVerId(long verId)
            throws BaseAppException {
        return getFormDao().queryFormVer(verId);
    }

    public DynFormVerDto queryFormVerDetailByVerId(String verId)
            throws BaseAppException {
        return getFormDao().queryFormVer(Long.valueOf(verId));
    }

    public boolean isExistTableByName(String tableName) throws BaseAppException {
        return getFormDbDao().isExistTableByName(tableName);
    }

    public Map getHtmlIdToTableColumn2Map(long dynFormId, long verId)
            throws BaseAppException {
        try {
            Map bdFormFields = new HashMap();
            Map bdFormFieldMap = null;
            Document document = null;
            DynFormVerDto dynFormVerDto = null;
            if (verId > 0) {
                dynFormVerDto = getFormDao().queryFormVer(verId);
            } else {
                dynFormVerDto = getFormDao().queryActiveFormVer(
                        Long.valueOf(dynFormId));
            }
            document = XMLDom4jUtils.createDocument(
                    dynFormVerDto.getDesignXmlStr(), false, null);
            Iterator iter = document.selectNodes(
                    "/ROOT/FORM[@formId='" + dynFormId + "']/TABLE/FIELD")
                    .iterator();
            Element eleField = null;
            Element eleTable = null;
            while (iter.hasNext()) {
                eleField = (Element) iter.next();
                if (eleField == null) {
                    continue;
                }

                bdFormFieldMap = new HashMap();
                bdFormFieldMap.put("ZDID", getText(eleField.attribute("ZDID")));
                bdFormFieldMap.put("ZDMC", getText(eleField.attribute("ZDMC")));
                bdFormFieldMap.put("ZDLX", getText(eleField.attribute("ZDLX")));
                bdFormFieldMap.put("ZDCD", getText(eleField.attribute("ZDCD")));
                bdFormFieldMap.put("ZDKK", Integer
                        .parseInt(getTextZero(eleField.attribute("ZDKK"))));

                // 是否流程字段
                bdFormFieldMap.put("ZDLC", getText(eleField.attribute("ZDLC")));
                // 保存其它表表名
                bdFormFieldMap.put("ZDQTB",
                        getText(eleField.attribute("ZDQTB")));

                bdFormFieldMap.put("Maxlen",
                        getText(eleField.attribute("Maxlen")));

                eleTable = eleField.getParent(); // 属于哪个表的
                if (eleTable != null) {
                    bdFormFieldMap.put("TableName",
                            this.getText(eleTable.attribute("GLBM")));
                }
                bdFormFields.put(getText(eleField.attribute("ZDID")),
                        bdFormFieldMap);
            }
            return bdFormFields;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public String getText(Attribute a) {
        if (a != null) {
            return a.getText();
        } else {
            return "";
        }
    }

    public String getTextZero(Attribute a) {
        if (a != null) {
            return a.getText();
        } else {
            return "0";
        }
    }

    public String queryFormHTML(String dynFormId) throws BaseAppException {
        return getFormDao().queryFormHTML(dynFormId);
    }

    public String queryFormXML(String dynFormId) throws BaseAppException {
        return getFormDao().queryFormXML(dynFormId);
    }

    /**
     * 根据动态表单结构生成sql
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    private ParamMap getQueryParam(DynamicDict dict) throws BaseAppException {
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        StringBuffer sql = new StringBuffer();
        String tableName = formFieldList.get(0).getTableName();
        sql.append("SELECT  DYN_ID,DYN_FORM_ID,DYN_VER_ID,ORDER_ID,ORDER_TYPE,DYN_CREATE_DATE,DYN_TYPE,DYN_PARENT_ID,USER_ID,USER_NAME,ORG_ID ");
        for (int i = 0; i < formFieldList.size(); i++) {
            DynFormFieldInfo dff = formFieldList.get(i);
            if (dff.getZDLB() > 0)
                sql.append("," + dff.getZDMC().toUpperCase());
        }
        sql.append(" FROM " + tableName);
        sql.append(" WHERE 1=1 ");
        ParamMap pm = new ParamMap();

        for (int i = 0; i < formFieldList.size(); i++) {
            DynFormFieldInfo dff = formFieldList.get(i);
            if (dff.getZDCX() > 0)
                if (dict.get(dff.getZDMC().toUpperCase()) != null) {
                    sql.append(" AND " + dff.getZDMC().toUpperCase());
                    if (dict.get(dff.getZDMC().toUpperCase() + "_OP") != null) {
                        if (dict.getString(dff.getZDMC().toUpperCase() + "_OP")
                                .trim().equalsIgnoreCase("like")) {
                            sql.append(" like '%"
                                    + dict.getString(dff.getZDMC()
                                    .toUpperCase()) + "%'  ");
                        } else {
                            sql.append(" "
                                    + dict.getString(dff.getZDMC()
                                    .toUpperCase() + "_OP"));
                            if (dff.getZDLX().equalsIgnoreCase("nvarchar")
                                    || dff.getZDLX().equalsIgnoreCase("ntext")) {
                                sql.append("'");
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                                sql.append("'");
                            } else if (dff.getZDLX().equalsIgnoreCase("ndate")) {
                                // sql.append(getSqlConvertDao().getToDate(dict.getString(dff.getZDMC().toUpperCase())));
                                sql.append("'");
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                                sql.append("'");
                            } else if (dff.getZDLX().equalsIgnoreCase("ntime")) {
                                // sql.append(getSqlConvertDao().getToTime(dict.getString(dff.getZDMC().toUpperCase())));
                                sql.append("'");
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                                sql.append("'");
                            } else {
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                            }

                        }
                        sql.append("  ");

                    } else {
                        sql.append("=");
                        if (dff.getZDLX().equalsIgnoreCase("nvarchar")
                                || dff.getZDLX().equalsIgnoreCase("ntext")) {
                            sql.append("'");
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                            sql.append("'");
                        } else if (dff.getZDLX().equalsIgnoreCase("ndate")) {
                            // sql.append(getSqlConvertDao().getToDate(dict.getString(dff.getZDMC().toUpperCase())));
                            sql.append("'");
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                            sql.append("'");
                        } else if (dff.getZDLX().equalsIgnoreCase("ntime")) {
                            // sql.append(getSqlConvertDao().getToTime(dict.getString(dff.getZDMC().toUpperCase())));
                            sql.append("'");
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                            sql.append("'");
                        } else {
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                        }
                        sql.append("  ");
                    }
                    pm.set(dff.getZDMC().toUpperCase(),
                            dict.get(dff.getZDMC().toUpperCase()));
                }
        }

        logger.debug("last sql == " + sql);
        dict.set("querySql", sql);
        return pm;
    }

    private ParamMap generQueryCntParam(DynamicDict dict)
            throws BaseAppException {
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        StringBuffer sql = new StringBuffer();
        String tableName = formFieldList.get(0).getTableName();
        sql.append("SELECT  count(*) as COUNT");
        sql.append(" FROM " + tableName);
        sql.append(" WHERE 1=1 ");
        ParamMap pm = new ParamMap();

        for (int i = 0; i < formFieldList.size(); i++) {
            DynFormFieldInfo dff = formFieldList.get(i);
            if (dff.getZDCX() > 0)
                if (dict.get(dff.getZDMC().toUpperCase()) != null) {
                    sql.append(" AND " + dff.getZDMC().toUpperCase());
                    if (dict.get(dff.getZDMC().toUpperCase() + "_OP") != null) {
                        if (dict.getString(dff.getZDMC().toUpperCase() + "_OP")
                                .trim().equalsIgnoreCase("like")) {
                            sql.append(" like '%"
                                    + dict.getString(dff.getZDMC()
                                    .toUpperCase()) + "%'  ");
                        } else {
                            sql.append(" "
                                    + dict.getString(dff.getZDMC()
                                    .toUpperCase() + "_OP"));
                            if (dff.getZDLX().equalsIgnoreCase("nvarchar")
                                    || dff.getZDLX().equalsIgnoreCase("ntext")) {
                                sql.append("'");
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                                sql.append("'");
                            } else if (dff.getZDLX().equalsIgnoreCase("ndate")) {
                                // sql.append(getSqlConvertDao().getToDate(dict.getString(dff.getZDMC().toUpperCase())));
                                sql.append("'");
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                                sql.append("'");
                            } else if (dff.getZDLX().equalsIgnoreCase("ntime")) {
                                // sql.append(getSqlConvertDao().getToTime(dict.getString(dff.getZDMC().toUpperCase())));
                                sql.append("'");
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                                sql.append("'");
                            } else {
                                sql.append(dict.getString(dff.getZDMC()
                                        .toUpperCase()));
                            }

                        }
                        sql.append("  ");

                    } else {
                        sql.append("=");
                        if (dff.getZDLX().equalsIgnoreCase("nvarchar")
                                || dff.getZDLX().equalsIgnoreCase("ntext")) {
                            sql.append("'");
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                            sql.append("'");
                        } else if (dff.getZDLX().equalsIgnoreCase("ndate")) {
                            // sql.append(getSqlConvertDao().getToDate(dict.getString(dff.getZDMC().toUpperCase())));
                            sql.append("'");
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                            sql.append("'");
                        } else if (dff.getZDLX().equalsIgnoreCase("ntime")) {
                            // sql.append(getSqlConvertDao().getToTime(dict.getString(dff.getZDMC().toUpperCase())));
                            sql.append("'");
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                            sql.append("'");
                        } else {
                            sql.append(dict.getString(dff.getZDMC()
                                    .toUpperCase()));
                        }
                        sql.append("  ");
                    }
                    pm.set(dff.getZDMC().toUpperCase(),
                            dict.get(dff.getZDMC().toUpperCase()));
                }
        }

        logger.debug("last COUNT sql == " + sql);
        dict.set("querySql", sql);
        return pm;
    }

    public List<DynamicDict> queryDynFormDataList(DynamicDict dict)
            throws BaseAppException {
        long dynFormId = dict.getLong("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        if (formFieldList == null || formFieldList.size() == 0) {
            ExceptionHandler.publish("Generate sql fail !",
                    ExceptionHandler.BUSS_ERROR);
        }
        String tableName = formFieldList.get(0).getTableName();
        AssertUtil.isNotEmpty(tableName);

        ParamMap pm = getQueryParam(dict);
        return getFormDao()
                .queryDynFormDataList(dict.getString("querySql"), pm);
    }

    public List<DynamicDict> queryDynFormDataStepList(DynamicDict dict)
            throws BaseAppException {
        long dynFormId = dict.getLong("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        if (formFieldList == null || formFieldList.size() == 0) {
            ExceptionHandler.publish("Generate sql fail !",
                    ExceptionHandler.BUSS_ERROR);
        }
        String tableName = formFieldList.get(0).getTableName();
        AssertUtil.isNotEmpty(tableName);

        ParamMap pm = getQueryParam(dict);
        BfmCommonStepDto bfmCommonStepDto = (BfmCommonStepDto) BoHelper.boToDto(dict, BfmCommonStepDto.class);
        return getFormDao().queryDynFormDataStepList(dict.getString("querySql"), pm, bfmCommonStepDto.getRowSetFormatter());
    }

    public int queryDynFormDataCount(DynamicDict dict) throws BaseAppException {
        long dynFormId = dict.getLong("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        if (formFieldList == null || formFieldList.size() == 0) {
            ExceptionHandler.publish("Generate sql fail !",
                    ExceptionHandler.BUSS_ERROR);
        }
        String tableName = formFieldList.get(0).getTableName();
        AssertUtil.isNotEmpty(tableName);

        ParamMap pm = generQueryCntParam(dict);
        return getFormDao().queryDynFormDataCount(dict.getString("querySql"),
                pm);
    }

    public DynamicDict qryDynFormDataByDynId(DynamicDict dict)
            throws BaseAppException {
        long holderNo = dict.getLong("DYN_ID");
        ArrayList<DynFormFieldInfo> formFieldList = (ArrayList<DynFormFieldInfo>) dict
                .getList("formFieldList");
        if (formFieldList == null || formFieldList.size() == 0) {
            ExceptionHandler.publish("Query form fail!",
                    ExceptionHandler.BUSS_ERROR);
        }
        String tableName = formFieldList.get(0).getTableName();
        DynamicDict data = getFormDao().qryDynFormDataByDynId(tableName,
                holderNo);
        for (DynFormFieldInfo field : formFieldList) {
            dict.set(field.getZDID(), data.get(field.getZDMC()));
        }
        dict.remove("formFieldList");
        return dict;
    }

    /**
     * 通过查询列，从界面设计的html里找出相应的元素，这样找的目的是查询条件的效果跟表单一样。 缺点是查询麻烦，查找一次就缓存起来。
     *
     * @param dict
     * @param formFieldList
     * @return
     * @throws BaseAppException
     */
    public String getDynFormDataListQryPageInfo(DynamicDict dict,
                                                ArrayList<DynFormFieldInfo> formFieldList) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        if (formFieldList == null)
            return null;

        DynFormFieldInfo dffi = null;
        ArrayList<DynFormFieldInfo> formConditionFieldList = new ArrayList<DynFormFieldInfo>();
        for (int i = 0; i < formFieldList.size(); i++) {
            dffi = formFieldList.get(i);
            if (dffi.getZDCX() != 0)
                formConditionFieldList.add(dffi);
        }
        if (formConditionFieldList.size() <= 0)
            return null;

        String html = getFormDao().queryFormHTML(dynFormId);
        if (html == null)
            return null;

        Document document = XMLDom4jUtils.createDocument(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT>" + html
                        + "</ROOT>", false, null);

        Iterator iter = document.selectNodes("/ROOT/table/tbody/tr/td")
                .iterator();
        Element eleTd = null;

        while (iter.hasNext()) {
            eleTd = (Element) iter.next();
            for (int i = 0; i < formConditionFieldList.size(); i++) {
                dffi = formConditionFieldList.get(i);

            }
        }

        return null;
    }

    public void addCat(DynamicDict dict) throws BaseAppException {
        DynFormCatDto dynFormCatDto = (DynFormCatDto) BoHelper.boToDto(dict,
                DynFormCatDto.class);
        if (DBUtilDecorator.isSameValueIgnoreCase("BFM_DYN_FORM_CAT",
                "CAT_NAME", dynFormCatDto.getCatName())) {
            dict.set("IS_EXIST", "true");
        } else {
            long id = getFormDao().addCat(dynFormCatDto);
            dict.set("CAT_ID", id);
        }
    }

    public void updateCat(DynamicDict dict) throws BaseAppException {
        DynFormCatDto dynFormCatDto = (DynFormCatDto) BoHelper.boToDto(dict,
                DynFormCatDto.class);
        dict.set("WHERE_SQL", " ID!=" + dynFormCatDto.getId());
        if (getFormDao().isExist(dict) > 0) {
            dict.set("IS_EXIST", "true");
        } else {
            getFormDao().updateCat(dynFormCatDto);
        }
    }

    public void deleteCat(DynamicDict dict) throws BaseAppException {
        DynFormCatDto dynFormCatDto = (DynFormCatDto) BoHelper.boToDto(dict,
                DynFormCatDto.class);
        getFormDao().deleteCat(dynFormCatDto);
    }

    public void queryCatList(DynamicDict dict) throws BaseAppException {
        BoHelper.listDtoToBO("DATA_LIST", getFormDao().queryCatList(dict),
                DynFormCatDto.class, dict);
    }

    public void queryDataDefStepList(DynamicDict dict) throws BaseAppException {
        long cnt = getFormDao().queryCatCount(dict);
        dict.set("DATA_COUNT", cnt);
        if (cnt > 0) {
            BoHelper.listDtoToBO("DATA_LIST",
                    getFormDao().queryCatStepList(dict), DynFormCatDto.class,
                    dict);
        }

    }

    public void queryCat(DynamicDict dict) throws BaseAppException {
        DynFormCatDto rdto = getFormDao().queryCat(dict);

        dict.set("DATA_OBJ", BoHelper.dtoToBO(rdto, new DynamicDict()));
    }
}
