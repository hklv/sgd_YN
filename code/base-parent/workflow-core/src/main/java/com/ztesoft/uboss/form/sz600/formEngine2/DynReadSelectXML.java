package com.ztesoft.uboss.form.sz600.formEngine2;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.form.dao.ISelectDataDAO;
import com.ztesoft.uboss.form.dao.mysqlimpl.SelectDataDAOMySQL;
import com.ztesoft.uboss.form.model.DynSelectDataDto;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DynReadSelectXML {

    private static HashMap hashMapTable = null;

    private ISelectDataDAO selectDataDAO = null;

    private ISelectDataDAO getSelectDataDAO() throws BaseAppException {
        if (selectDataDAO == null) {
            selectDataDAO = SgdDaoFactory.getDaoImpl(SelectDataDAOMySQL.class);
        }
        return selectDataDAO;
    }

    public DynReadSelectXML() {
    }

    public HashMap getTableFromXML() throws BaseAppException {
        Session s = null;
        try {

            if (hashMapTable == null) {
                s = SessionContext.currentSession();
                s.beginTrans();
                this.readXml();
                s.commitTrans();
            }
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }
        return hashMapTable;
    }

    public DynSelectTableInfo getTableInfoFromXML(String sTableName)
            throws BaseAppException {
        Session s = null;
        DynSelectTableInfo bdSelectTableInfo = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();

            if (hashMapTable == null) {
                this.readXml();
            }
            bdSelectTableInfo = (DynSelectTableInfo) hashMapTable
                    .get(new String(sTableName));
            if (bdSelectTableInfo == null) {
                this.updateXML(sTableName);
            }
            bdSelectTableInfo = (DynSelectTableInfo) hashMapTable
                    .get(new String(sTableName));
            s.commitTrans();
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }
        return bdSelectTableInfo;
    }

    public String[] getTableName() throws BaseAppException {
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();
            this.readXml();
            s.commitTrans();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }

        java.util.Set set = hashMapTable.keySet();
        Iterator iter = null;
        int iLen = 0;
        String ss[] = null;
        if (set != null) {
            iLen = set.size();
            iter = set.iterator();
            ss = new String[iLen];
        }
        int i = 0;
        while (iter != null && iter.hasNext()) {
            ss[i] = (String) iter.next();
            i++;
        }
        return ss;
    }

    private void updateXML(String strName) throws BaseAppException {
        if (hashMapTable == null) {
            hashMapTable = new HashMap();
        }
        DynamicDict dict = new DynamicDict();
        dict.set("GLSM", strName);
        DynSelectDataDto dynSelectDataDto = getSelectDataDAO()
                .querySelectDataDetail(dict);
        try {

            Document document = XMLDom4jUtils.createDocument(
                    dynSelectDataDto.getGlcontentStr(), false, null);
            Element eleTable = (Element) document
                    .selectSingleNode("/ROOT/TABLE[@GLSM='" + strName + "']");
            DynSelectTableInfo bdSelectTableInfo = null;
            String GLBM = "";
            String GLSM = "";
            String GLTJ = "";
            String GLLX = "";
            String FieldID = "";
            String FieldName = "";
            String FieldType = "";
            String FieldDesc = "";
            String FieldPopedom = "";
            String GLGLBM = "";
            String GLGLID = "";
            String GLGLMC = "";
            String GLGLTJ = "";
            String GLDLID = "";
            String GLBZ = "";

            String RelateField = "";
            String RelateObject = "";
            String moduleName = "";

            if (eleTable != null) {
                GLBM = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLBM")));
                GLSM = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLSM")));
                GLTJ = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLTJ")));
                GLLX = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLLX")));
                FieldID = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldID")));
                FieldName = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldName")));
                FieldType = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldType")));
                FieldDesc = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldDesc")));
                FieldPopedom = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldPopedom")));
                GLGLBM = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLBM")));
                GLGLID = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLID")));
                GLGLMC = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLMC")));
                GLGLTJ = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLTJ")));
                GLDLID = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLDLID")));
                GLBZ = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLBZ")));
                RelateField = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("RelateField")));
                RelateObject = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("RelateObject")));
                moduleName = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("moduleName")));

                bdSelectTableInfo = new DynSelectTableInfo();
                bdSelectTableInfo.setGLSM(GLSM);
                bdSelectTableInfo.setGLBM(GLBM);
                bdSelectTableInfo.setGLTJ(GLTJ);
                bdSelectTableInfo.setGLLX(GLLX);
                bdSelectTableInfo.setFieldID(FieldID);
                bdSelectTableInfo.setFieldName(FieldName);
                bdSelectTableInfo.setFieldType(FieldType);
                bdSelectTableInfo.setFieldDesc(FieldDesc);
                bdSelectTableInfo.setFieldPopedom(FieldPopedom);
                bdSelectTableInfo.setGLGLBM(GLGLBM);
                bdSelectTableInfo.setGLGLID(GLGLID);
                bdSelectTableInfo.setGLGLMC(GLGLMC);
                bdSelectTableInfo.setGLGLTJ(GLGLTJ);
                bdSelectTableInfo.setGLDLID(GLDLID);
                bdSelectTableInfo.setGLBZ(GLBZ);

                bdSelectTableInfo.setRelateField(RelateField);
                bdSelectTableInfo.setRelateObject(RelateObject);
                bdSelectTableInfo.setModuleName(moduleName);
                hashMapTable.put(new String(GLSM), bdSelectTableInfo);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void readXml() throws BaseAppException {
        if (hashMapTable != null) {
            return;
        }
        hashMapTable = new HashMap();

        try {

            DynamicDict dict = new DynamicDict();
            List<DynSelectDataDto> list = getSelectDataDAO()
                    .querySelectDataAllList(dict);

            Attribute attribute = null;
            DynSelectTableInfo bdSelectTableInfo = null;

            String GLBM = "";
            String GLSM = "";
            String GLTJ = "";
            String GLLX = "";
            String FieldID = "";
            String FieldName = "";
            String FieldType = "";
            String FieldDesc = "";
            String FieldPopedom = "";
            String GLGLBM = "";
            String GLGLID = "";
            String GLGLMC = "";
            String GLGLTJ = "";
            String GLDLID = "";
            String GLBZ = "";

            String RelateField = "";
            String RelateObject = "";
            String moduleName = "";
            for (int i = 0; i < list.size(); i++) {
                DynSelectDataDto item = list.get(i);
                Document document = XMLDom4jUtils.createDocument(
                        item.getGlcontentStr(), false, null);
                Element eleTable = (Element) document
                        .selectSingleNode("/ROOT/TABLE[@GLSM='"
                                + item.getGlsm() + "']");

                if (eleTable == null) {
                    continue;
                }
                GLBM = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLBM")));
                GLSM = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLSM")));
                GLTJ = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLTJ")));
                GLLX = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLLX")));
                FieldID = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldID")));
                FieldName = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldName")));
                FieldType = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldType")));
                FieldDesc = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldDesc")));
                FieldPopedom = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("FieldPopedom")));
                GLGLBM = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLBM")));
                GLGLID = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLID")));
                GLGLMC = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLMC")));
                GLGLTJ = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLGLTJ")));
                GLDLID = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLDLID")));
                GLBZ = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("GLBZ")));
                RelateField = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("RelateField")));
                RelateObject = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("RelateObject")));
                moduleName = SzConvert.toSpaceStr(this.getText(eleTable
                        .attribute("moduleName")));

                bdSelectTableInfo = new DynSelectTableInfo();
                bdSelectTableInfo.setGLSM(GLSM);
                bdSelectTableInfo.setGLBM(GLBM);
                bdSelectTableInfo.setGLTJ(GLTJ);
                bdSelectTableInfo.setGLLX(GLLX);
                bdSelectTableInfo.setFieldID(FieldID);
                bdSelectTableInfo.setFieldName(FieldName);
                bdSelectTableInfo.setFieldType(FieldType);
                bdSelectTableInfo.setFieldDesc(FieldDesc);
                bdSelectTableInfo.setFieldPopedom(FieldPopedom);
                bdSelectTableInfo.setGLGLBM(GLGLBM);
                bdSelectTableInfo.setGLGLID(GLGLID);
                bdSelectTableInfo.setGLGLMC(GLGLMC);
                bdSelectTableInfo.setGLGLTJ(GLGLTJ);
                bdSelectTableInfo.setGLDLID(GLDLID);
                bdSelectTableInfo.setGLBZ(GLBZ);

                bdSelectTableInfo.setRelateField(RelateField);
                bdSelectTableInfo.setRelateObject(RelateObject);
                bdSelectTableInfo.setModuleName(moduleName);

                hashMapTable.put(new String(GLSM), bdSelectTableInfo);

            } //


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getText(Attribute a) {
        if (a != null) {
            return a.getText();
        } else {
            return "";
        }
    }

    public boolean add(HttpServletRequest request) throws BaseAppException {
        String GLBM = SzConvert.toSpaceStr(request.getParameter("tableName"));

        String GLSM = SzConvert.toSpaceStr(request.getParameter("relateName"));
        String GLTJ = SzConvert.strWriteData(request.getParameter("tableCond"));
        String GLLX = SzConvert.toSpaceStr(request.getParameter("relateType"));
        String FieldID = SzConvert
                .toSpaceStr(request.getParameter("tableUser"));
        // String FieldName =
        // SzConvert.toSpaceStr(request.getParameter("fieldName") );
        // String FieldDesc =
        // SzConvert.toSpaceStr(request.getParameter("objectName") );
        String FieldName = SzConvert.arrayToStr(
                request.getParameterValues("fieldName"), ";");
        String FieldDesc = SzConvert.arrayToStr(
                request.getParameterValues("objectName"), ";");
        String FieldType = SzConvert.toSpaceStr(request
                .getParameter("cateRelate"));

        String FieldPopedom = SzConvert.toSpaceStr(request
                .getParameter("tablePopedom"));
        String GLGLBM = SzConvert.toSpaceStr(request.getParameter("cateTable"));
        String GLGLID = SzConvert.toSpaceStr(request.getParameter("cateID"));
        String GLGLMC = SzConvert.toSpaceStr(request.getParameter("cateName"));
        String GLGLTJ = SzConvert
                .strWriteData(request.getParameter("cateCond"));
        String GLDLID = SzConvert.toSpaceStr(request.getParameter("cateUser"));

        String GLBZ = SzConvert
                .strWriteData(request.getParameter("relateDesc"));
        String RelateField = SzConvert.toSpaceStr(request
                .getParameter("RelateField"));
        String RelateObject = SzConvert.toSpaceStr(request
                .getParameter("RelateObject"));
        String moduleName = SzConvert.toSpaceStr(request
                .getParameter("moduleName"));
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();

            Document document = null;
            Element eleTable = null;
            DynamicDict dict = new DynamicDict();
            dict.set("GLSM", GLSM);
            DynSelectDataDto dynSelectDataDto = getSelectDataDAO()
                    .querySelectDataDetail(dict);
            if (dynSelectDataDto != null) { // 修改
                document = XMLDom4jUtils.createDocument(
                        dynSelectDataDto.getGlcontentStr(), false, null);
                eleTable = (Element) document
                        .selectSingleNode("/ROOT/TABLE[@GLSM='" + GLSM + "']");

                dynSelectDataDto.setGlsm(GLSM);
                dynSelectDataDto.setGlbm(GLBM);
                dynSelectDataDto.setGlbz(GLBZ);

                eleTable.attribute("GLSM").setValue(GLSM);
                eleTable.attribute("GLBM").setValue(GLBM);

                eleTable.attribute("GLTJ").setValue(GLTJ);
                eleTable.attribute("GLLX").setValue(GLLX);
                eleTable.attribute("FieldID").setValue(FieldID);
                eleTable.attribute("FieldName").setValue(FieldName);
                eleTable.attribute("FieldType").setValue(FieldType);
                eleTable.attribute("FieldDesc").setValue(FieldDesc);
                eleTable.attribute("FieldPopedom").setValue(FieldPopedom);

                eleTable.attribute("GLGLBM").setValue(GLGLBM);
                eleTable.attribute("GLGLID").setValue(GLGLID);
                eleTable.attribute("GLGLMC").setValue(GLGLMC);
                eleTable.attribute("GLGLTJ").setValue(GLGLTJ);

                SzConvert.setText(eleTable, "GLDLID", GLDLID);
                SzConvert.setText(eleTable, "GLBZ", GLBZ);
                SzConvert.setText(eleTable, "RelateField", RelateField);
                SzConvert.setText(eleTable, "RelateObject", RelateObject);

                dynSelectDataDto.setGlcontentStr(document.asXML());
                getSelectDataDAO().updateSelectData(dynSelectDataDto);

            } else { // 增加
                document = XMLDom4jUtils
                        .createDocument(
                                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT></ROOT>",
                                false, null);
                Element rootElement = document.getRootElement();

                dynSelectDataDto = new DynSelectDataDto();
                dynSelectDataDto.setGlsm(GLSM);
                dynSelectDataDto.setGlbm(GLBM);
                dynSelectDataDto.setGlbz(GLBZ);

                eleTable = rootElement.addElement("TABLE");
                eleTable.addAttribute("GLSM", GLSM);
                eleTable.addAttribute("GLBM", GLBM);
                eleTable.addAttribute("GLTJ", GLTJ);
                eleTable.addAttribute("GLLX", GLLX);
                eleTable.addAttribute("FieldID", FieldID);
                eleTable.addAttribute("FieldName", FieldName);
                eleTable.addAttribute("FieldType", FieldType);
                eleTable.addAttribute("FieldDesc", FieldDesc);
                eleTable.addAttribute("FieldPopedom", FieldPopedom);
                eleTable.addAttribute("GLGLBM", GLGLBM);
                eleTable.addAttribute("GLGLID", GLGLID);
                eleTable.addAttribute("GLGLMC", GLGLMC);
                eleTable.addAttribute("GLGLTJ", GLGLTJ);

                eleTable.addAttribute("GLDLID", GLDLID);
                eleTable.addAttribute("GLBZ", GLBZ);
                eleTable.addAttribute("RelateField", RelateField);
                eleTable.addAttribute("RelateObject", RelateObject);
                eleTable.addAttribute("moduleName", moduleName);
                dynSelectDataDto.setGlcontentStr(document.asXML());

                getSelectDataDAO().addSelectData(dynSelectDataDto);

            }
            // 更新
            this.updateXML(GLSM);
            s.commitTrans();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }

        return true;
    }

    // 删除
    public boolean delete(String strName) throws BaseAppException {
        Session s = null;
        try {
            s = SessionContext.currentSession();
            s.beginTrans();

            DynamicDict dict = new DynamicDict();
            dict.set("GLSM", strName);
            dict.set("STATE", "X");
            getSelectDataDAO().updateSelectDataState(dict);
            s.commitTrans();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                s.releaseTrans();
            }
        }
        // 更新hashMap
        hashMapTable.remove(new String(strName));

        return true;
    }

	/*
     * public void updateXML(String sPath, Document document) { try {
	 * OutputFormat format = OutputFormat.createPrettyPrint();
	 * format.setEncoding("GB2312"); XMLWriter xMLWriter = new
	 * XMLWriter(format); Writer writer = new BufferedWriter(new
	 * FileWriter(sPath)); xMLWriter.setWriter(writer);
	 * xMLWriter.write(document); xMLWriter.flush(); xMLWriter.close(); }
	 * catch(Exception e) { SzOut.print("更新xml文件异常！"); } }
	 */

    public static void main(String[] args) {
        // ReadSelectXML readStockXML = new ReadSelectXML();
        // readStockXML.getTableFromXML();
        // readStockXML.createTable("SZKC_CPXX2");
    }

}
