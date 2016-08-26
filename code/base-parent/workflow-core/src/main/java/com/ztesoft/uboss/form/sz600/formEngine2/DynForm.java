package com.ztesoft.uboss.form.sz600.formEngine2;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.uboss.form.dao.IFormDAO;
import com.ztesoft.uboss.form.dao.mysqlimpl.FormDAOMySQL;
import com.ztesoft.uboss.form.model.DynFormDto;
import com.ztesoft.uboss.form.model.DynFormVerDto;
import com.ztesoft.uboss.form.service.FormService;
import com.ztesoft.uboss.form.sz600.tool.SzConvert;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.Session;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */

public class DynForm {

    public DynForm() {
    }

    private IFormDAO formDao = null;

    private IFormDAO getFormDao() throws BaseAppException {
        if (formDao == null) {
            formDao = SgdDaoFactory.getDaoImpl(FormDAOMySQL.class);
        }
        return formDao;
    }

    private String[] getXMLModuleList(int iType) {

        return null;
    }

    public void updateDynTableValue(DynamicDict bo) throws BaseAppException {
        getFormDao().updateDynTableValue(bo);
    }

    private boolean createXMLDynForm(String dynFormId,
                                     HttpServletRequest request) throws BaseAppException {

        // 保存字段信息,在一下方法有存历史记录

        DynFormCreate bdFormCreate = new DynFormCreate();
        getFormDao().updateFormVerState(Long.valueOf(dynFormId));
        getFormDao().addFormVer(dynFormId, "1",
                request.getSession().getAttribute("USER_ID").toString());
        String formHtml = SzConvert.strWriteDataHTML(request
                .getParameter("formHtml"));
        getFormDao().updateFormVerHTML(dynFormId, formHtml);
        bdFormCreate.createXMLField(dynFormId,
                getFormDao().queryForm(Long.valueOf(dynFormId)), request, 0);
        refresh(dynFormId);
        return true;
    }

    public boolean updateXMLDynForm(String dynFormId, HttpServletRequest request)
            throws BaseAppException {

        // 保存字段信息,在一下方法有存历史记录
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            DynFormCreate bdFormCreate = new DynFormCreate();
            getFormDao().updateFormVerState(Long.valueOf(dynFormId));
            getFormDao().addFormVer(dynFormId, "1",
                    request.getSession().getAttribute("USER_ID").toString());
            String formHtml = SzConvert.strWriteDataHTML(request
                    .getParameter("formHtml"));
            getFormDao().updateFormVerHTML(dynFormId, formHtml);

            String tableCount = SzConvert.toZeroStr(request
                    .getParameter("tableCount"));
            int iCountTable = Integer.parseInt(tableCount);
            String tableNames = "";
            for (int i = 0; i <= iCountTable; i++) {
                String tName = SzConvert.strWriteData(
                        request.getParameter("table" + i)).trim();
                if (tName.length() > 0) {
                    tableNames += "," + tName;
                }
            }
            if (tableNames.length() > 0) {
                DynFormDto dfd = new DynFormDto();
                dfd.setModifyTime(DateUtil.GetDBDateTime());
                dfd.setDynFormId(Long.valueOf(dynFormId));
                dfd.setFormTable(tableNames.substring(1));
                getFormDao().updateForm(dfd);
            }
            long lastVerId = getFormDao().queryMaxVerId(dynFormId, "X");
            bdFormCreate.updateXMLField(dynFormId,
                    getFormDao().queryForm(Long.valueOf(dynFormId)), request,
                    0, lastVerId);

            refresh(dynFormId);

            s.commitTrans();
        } finally {
            s.releaseTrans();
        }
        return true;
    }

    void saveDynXml(String dynFormId, String xmlStr) throws BaseAppException {
        getFormDao().updateFormVerXML(dynFormId, xmlStr);
    }

    public boolean updateXMLField(String dynFormId, HttpServletRequest request)
            throws BaseAppException {
        Session s = null;
        boolean rt = false;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            rt = this.updateXMLField(dynFormId, request, 1);
            s.commitTrans();
            return rt;
        } finally {
            s.releaseTrans();
        }

    }

    private boolean updateXMLField_ONLY(String dynFormId,
                                        HttpServletRequest request) throws BaseAppException {
        return this.updateXMLField(dynFormId, request, 2);
    }

    /**
     * 修改字段的属性
     *
     * @param dynFormId
     * @param request
     * @param request
     * @param iType     ：0编辑界面；1修改属性，2修改属性，不重新创建表
     * @return
     */
    private boolean updateXMLField(String dynFormId,
                                   HttpServletRequest request, int iType) throws BaseAppException {
        boolean flag;
        DynFormCreate bdFormCreate = new DynFormCreate();
        flag = bdFormCreate
                .updateXMLField(dynFormId,
                        getFormDao().queryForm(Long.valueOf(dynFormId)),
                        request, iType);

        refresh(dynFormId);
        return true;

    }

    public String getHTML(String dynFormId) {
        String html = "";
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            html = getFormDao().queryFormHTML(dynFormId);
            s.commitTrans();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                s.releaseTrans();
            } catch (BaseAppException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return html;
    }

    private String getXML(String dynFormId) {
        String html = "";
        try {
            html = getFormDao().queryFormXML(dynFormId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return html;
    }

    public DynFormInfo getFormInfo(String dynFormId) {
        return getFormInfo(Long.valueOf(dynFormId));
    }

    DynFormInfo getFormInfo(long dynFormId) {
        return getFormInfo(dynFormId, -1);
    }

    DynFormInfo getFormInfo(long dynFormId, long verId) {
        DynFormInfo bdFormInfo = new DynFormInfo();
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
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
            Element eleForm = (Element) document
                    .selectSingleNode("/ROOT/FORM[@formId='" + dynFormId + "']");
            if (eleForm != null) {
                bdFormInfo.setFormId(this.getText(eleForm.attribute("formId")));
                bdFormInfo.setFormName(this.getText(eleForm
                        .attribute("formName")));
                bdFormInfo.setFormDesc(this.getText(eleForm
                        .attribute("formDesc")));
                bdFormInfo.setFormPackage(this.getText(eleForm
                        .attribute("formPackage")));
                bdFormInfo.setTableCount(SzConvert.toInt(this.getText(eleForm
                        .attribute("tableCount"))));
                bdFormInfo.setCellCount(SzConvert.toInt(this.getText(eleForm
                        .attribute("cellCount"))));
                bdFormInfo.setRowCount(SzConvert.toInt(this.getText(eleForm
                        .attribute("rowCount"))));
                bdFormInfo.setObjectCount(SzConvert.toInt(this.getText(eleForm
                        .attribute("objectCount"))));

                bdFormInfo.setEditor(this.getText(eleForm.attribute("editor")));
                bdFormInfo.setHasAnnex(this.getText(eleForm
                        .attribute("hasAnnex")));
                bdFormInfo.setHasDepartLimit(this.getText(eleForm
                        .attribute("hasDepartLimit")));
                bdFormInfo
                        .setHasFlow(this.getText(eleForm.attribute("hasFlow")));
                bdFormInfo.setHasFlowSms(this.getText(eleForm
                        .attribute("hasFlowSms")));
                bdFormInfo.setPrintTemp(this.getText(eleForm
                        .attribute("printTemp")));
                bdFormInfo.setPrintTempRelate(this.getText(eleForm
                        .attribute("printTempRelate")));
                bdFormInfo.setRelateFormID(SzConvert.getText(eleForm
                        .attribute("relateFormID")));
                bdFormInfo.setTriggerName(SzConvert.getText(eleForm
                        .attribute("triggerName")));
                bdFormInfo.setSelectName(SzConvert.getText(eleForm
                        .attribute("selectName")));
                bdFormInfo.setAllograph(SzConvert.getText(eleForm
                        .attribute("allograph")));
                bdFormInfo.setHasReadRecord(this.getText(eleForm
                        .attribute("hasReadRecord")));
                bdFormInfo.setHasUpdateRecord(this.getText(eleForm
                        .attribute("hasUpdateRecord")));
                bdFormInfo.setHasUserLimit(this.getText(eleForm
                        .attribute("hasUserLimit")));
                bdFormInfo.setModulePopedom(this.getText(eleForm
                        .attribute("modulePopedom")));
                bdFormInfo.setLock(this.getText(eleForm.attribute("lock")));

                bdFormInfo.setDesktop(SzConvert.toInt(this.getTextZero(eleForm
                        .attribute("desktop"))));
                bdFormInfo.setToolbar(SzConvert.toSpaceStr(this.getText(eleForm
                        .attribute("toolbar"))));
                bdFormInfo.setWeboffice(SzConvert.toSpaceStr(this
                        .getText(eleForm.attribute("weboffice"))));
                bdFormInfo.setTemplateSelect(SzConvert.toSpaceStr(this
                        .getText(eleForm.attribute("templateSelect"))));
                // 2009-02-17发布
                bdFormInfo.setDefaultCond(SzConvert.getText(eleForm
                        .attribute("defaultCond")));
                bdFormInfo.setPrefix(SzConvert.getText(eleForm
                        .attribute("prefix")));
                bdFormInfo.setAssistant(SzConvert.getText(eleForm
                        .attribute("assistant")));
                bdFormInfo.setFormType(SzConvert.getText(eleForm
                        .attribute("formType")));
            }
            s.commitTrans();
        } catch (Exception e) {
        } finally {
            try {
                s.releaseTrans();
            } catch (BaseAppException e) {
                e.printStackTrace();
            }
        }
        return bdFormInfo;
    }

    public String getTableStr(String dynFormId) {
        return getTableStr(Long.valueOf(dynFormId));
    }

    private String getTableStr(long dynFormId) {
        String strName = "";
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();

            Document document = null;
            DynFormVerDto dynFormVerDto = getFormDao().queryActiveFormVer(
                    Long.valueOf(dynFormId));
            document = XMLDom4jUtils.createDocument(
                    dynFormVerDto.getDesignXmlStr(), false, null);
            // 先读出指定的form
            Element eleForm = (Element) document
                    .selectSingleNode("/ROOT/FORM[@formId='" + dynFormId + "']");
            // 读出form下的table
            Iterator iter = eleForm.elementIterator("TABLE");
            Element eleTable = null;
            String name = "";
            while (iter.hasNext()) {
                eleTable = (Element) iter.next();
                name = getText(eleTable.attribute("GLBM"));
                strName += name + " ";
            }

            s.commitTrans();
        } catch (Exception e) {
        } finally {
            try {
                s.releaseTrans();
            } catch (BaseAppException e) {
                e.printStackTrace();
            }
        }
        return strName;
    }

    public ArrayList<DynFormTableInfo> getTableList(String dynFormId) {
        return getTableList(Long.valueOf(dynFormId), -1);
    }

    private ArrayList<DynFormTableInfo> getTableList(long dynFormId) {
        return getTableList(dynFormId, -1);
    }

    ArrayList<DynFormTableInfo> getTableList(long dynFormId, long verId) {
        ArrayList<DynFormTableInfo> arrayList = new ArrayList<DynFormTableInfo>();
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();

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
            // 先读出指定的form
            // 读出form下的table
            Iterator iter = document.selectNodes(
                    "/ROOT/FORM[@formId='" + dynFormId + "']/TABLE").iterator();
            Element eleTable = null;

            DynFormTableInfo bdFormTableInfo = null;
            while (iter.hasNext()) {
                bdFormTableInfo = new DynFormTableInfo();
                eleTable = (Element) iter.next();

                bdFormTableInfo
                        .setGLID(this.getText(eleTable.attribute("GLID")));
                bdFormTableInfo
                        .setGLBM(this.getText(eleTable.attribute("GLBM")));
                bdFormTableInfo
                        .setGLSM(this.getText(eleTable.attribute("GLSM")));
                bdFormTableInfo
                        .setGLLM(this.getText(eleTable.attribute("GLLM")));
                bdFormTableInfo.setGLLX(this.getTextZero(eleTable
                        .attribute("GLLX")));
                bdFormTableInfo.setGLZB(this.getTextZero(eleTable
                        .attribute("GLZB")));
                arrayList.add(bdFormTableInfo);
            }
            s.commitTrans();
        } catch (Exception e) {
        } finally {
            try {
                s.releaseTrans();
            } catch (BaseAppException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public ArrayList<DynFormFieldInfo> getFieldList(String dynFormId) {
        return getFieldList(Long.valueOf(dynFormId), -1);
    }

    ArrayList<DynFormFieldInfo> getFieldList(long dynFormId) {
        return getFieldList(dynFormId, -1);
    }

    public ArrayList<DynFormFieldInfo> getFieldList(long dynFormId, long verId) {
        ArrayList<DynFormFieldInfo> arrayList = new ArrayList<DynFormFieldInfo>();
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            DynFormFieldInfo bdFormFieldInfo = null;
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

                bdFormFieldInfo = new DynFormFieldInfo();
                bdFormFieldInfo.setZDID(getText(eleField.attribute("ZDID")));
                bdFormFieldInfo.setZDMC(getText(eleField.attribute("ZDMC")));
                bdFormFieldInfo.setZDSM(getText(eleField.attribute("ZDSM")));
                bdFormFieldInfo.setZDLX(getText(eleField.attribute("ZDLX")));
                bdFormFieldInfo.setZDCD(getText(eleField.attribute("ZDCD")));
                bdFormFieldInfo.setZDZT(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDZT"))));
                bdFormFieldInfo.setZDLB(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDLB"))));
                bdFormFieldInfo.setZDBJ(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDBJ"))));
                bdFormFieldInfo.setZDKK(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDKK"))));
                bdFormFieldInfo.setZDBY(getText(eleField.attribute("ZDBY")));
                bdFormFieldInfo
                        .setZDGLBM(getText(eleField.attribute("ZDGLBM")));
                bdFormFieldInfo
                        .setZDGLID(getText(eleField.attribute("ZDGLID")));
                bdFormFieldInfo
                        .setZDGLMC(getText(eleField.attribute("ZDGLMC")));
                bdFormFieldInfo.setZDKZ(getText(eleField.attribute("ZDKZ")));
                bdFormFieldInfo.setZDBQ(getText(eleField.attribute("ZDBQ")));
                bdFormFieldInfo.setZDCSZ(getText(eleField.attribute("ZDCSZ")));
                bdFormFieldInfo.setZDHS(getText(eleField.attribute("ZDHS")));

                // 是否流程字段
                bdFormFieldInfo.setZDLC(getText(eleField.attribute("ZDLC")));
                // 保存其它表表名
                bdFormFieldInfo.setZDQTB(getText(eleField.attribute("ZDQTB")));

                // 20080902，增加查询、导入、导出
                bdFormFieldInfo.setZDCX(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDCX"))));
                bdFormFieldInfo.setZDDR(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDDR"))));
                bdFormFieldInfo.setZDDC(Integer.parseInt(getTextZero(eleField
                        .attribute("ZDDC"))));

                // 2011-06-07增加
                bdFormFieldInfo.setAlign(getText(eleField.attribute("Align")));
                bdFormFieldInfo.setBold(getText(eleField.attribute("Bold")));
                bdFormFieldInfo.setColor(getText(eleField.attribute("Color")));
                bdFormFieldInfo
                        .setMaxlen(getText(eleField.attribute("Maxlen")));

                eleTable = eleField.getParent(); // 属于哪个表的
                if (eleTable != null) {
                    bdFormFieldInfo.setTableId(this.getText(eleTable
                            .attribute("GLID")));
                    bdFormFieldInfo.setTableName(this.getText(eleTable
                            .attribute("GLBM")));
                }
                arrayList.add(bdFormFieldInfo);
                // System.out.println(name);
            }
            s.commitTrans();
        } catch (Exception e) {
        } finally {
            try {
                s.releaseTrans();
            } catch (BaseAppException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private String getText(Attribute a) {
        if (a != null) {
            return a.getText();
        } else {
            return "";
        }
    }

    private String getTextZero(Attribute a) {
        if (a != null) {
            return a.getText();
        } else {
            return "0";
        }
    }

    private void setText(Element ele, String name, String value) {
        // 如果attribute不存在，就创建新的
        Attribute attribute = ele.attribute(name);
        if (attribute == null) {
            ele.addAttribute(name, value);
        } else {
            attribute.setValue(value);
        }
    }

    public void refresh(String dynFormId){
        try {
            DynamicDict dict = new DynamicDict();
            dict.set("DYN_FORM_ID", dynFormId);
            FormService.refreshFieldList(dict);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}