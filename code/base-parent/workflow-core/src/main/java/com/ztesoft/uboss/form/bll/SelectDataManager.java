package com.ztesoft.uboss.form.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.form.dao.ISelectDataDAO;
import com.ztesoft.uboss.form.dao.mysqlimpl.SelectDataDAOMySQL;
import com.ztesoft.uboss.form.model.DynSelectDataDto;
import com.ztesoft.uboss.form.sz600.tool.SzConvert;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

public class SelectDataManager {

    private ISelectDataDAO selectDataDAO = null;

    private ISelectDataDAO getSelectDataDAO() throws BaseAppException {
        if (selectDataDAO == null) {
            selectDataDAO = SgdDaoFactory.getDaoImpl(SelectDataDAOMySQL.class);
        }
        return selectDataDAO;
    }

    public void addSelectData(DynamicDict dict) throws BaseAppException {
        getSelectDataDAO().addSelectData(dict);
    }

    public void updateSelectData(DynamicDict dict) throws BaseAppException {
        getSelectDataDAO().updateSelectData(dict);
    }

    public void updateSelectDataState(DynamicDict dict) throws BaseAppException {
        getSelectDataDAO().updateSelectDataState(dict);
    }

    public void querySelectDataDetail(DynamicDict dict) throws BaseAppException {
        DynSelectDataDto dynSelectDataDto = getSelectDataDAO().querySelectDataDetail(dict);
        try {

            Document document = XMLDom4jUtils.createDocument(dynSelectDataDto.getGlcontentStr(),
                    false, null);
            Element eleTable = (Element) document.selectSingleNode("/ROOT/TABLE[@GLSM='" + dynSelectDataDto.getGlsm() + "']");
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
                GLBM = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLBM")));
                GLSM = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLSM")));
                GLTJ = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLTJ")));
                GLLX = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLLX")));
                FieldID = SzConvert.toSpaceStr(this.getText(eleTable.attribute("FieldID")));
                FieldName = SzConvert.toSpaceStr(this.getText(eleTable.attribute("FieldName")));
                FieldType = SzConvert.toSpaceStr(this.getText(eleTable.attribute("FieldType")));
                FieldDesc = SzConvert.toSpaceStr(this.getText(eleTable.attribute("FieldDesc")));
                FieldPopedom = SzConvert.toSpaceStr(this.getText(eleTable.attribute("FieldPopedom")));
                GLGLBM = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLGLBM")));
                GLGLID = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLGLID")));
                GLGLMC = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLGLMC")));
                GLGLTJ = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLGLTJ")));
                GLDLID = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLDLID")));
                GLBZ = SzConvert.toSpaceStr(this.getText(eleTable.attribute("GLBZ")));
                RelateField = SzConvert.toSpaceStr(this.getText(eleTable.attribute("RelateField")));
                RelateObject = SzConvert.toSpaceStr(this.getText(eleTable.attribute("RelateObject")));
                moduleName = SzConvert.toSpaceStr(this.getText(eleTable.attribute("moduleName")));
                dict.set("GLSM", GLSM);
                dict.set("GLBM", GLBM);
                dict.set("GLTJ", GLTJ);
                dict.set("GLLX", GLLX);
                dict.set("FieldID", FieldID);
                dict.set("FieldName", FieldName);
                dict.set("FieldType", FieldType);
                dict.set("FieldDesc", FieldDesc);
                dict.set("FieldPopedom", FieldPopedom);
                dict.set("GLGLBM", GLGLBM);
                dict.set("GLGLID", GLGLID);
                dict.set("GLGLMC", GLGLMC);
                dict.set("GLGLTJ", GLGLTJ);
                dict.set("GLDLID", GLDLID);
                dict.set("GLBZ", GLBZ);

                dict.set("RelateField", RelateField);
                dict.set("RelateObject", RelateObject);
                dict.set("ModuleName", moduleName);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void querySelectDataList(DynamicDict dict) throws BaseAppException {
        dict.set("DATA_LIST", getSelectDataDAO().querySelectDataList(dict));
    }

    public String getText(Attribute a) {
        if (a != null) {
            return a.getText();
        } else {
            return "";
        }
    }

}
