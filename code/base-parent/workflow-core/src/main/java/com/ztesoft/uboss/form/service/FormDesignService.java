package com.ztesoft.uboss.form.service;


import com.ztesoft.uboss.form.bll.FormManager;
import com.ztesoft.uboss.form.bll.SelectDataManager;
import com.ztesoft.uboss.form.model.DynFormDto;
import com.ztesoft.uboss.form.sz600.formEngine2.DynForm;
import com.ztesoft.uboss.form.sz600.formEngine2.DynFormFieldInfo;
import com.ztesoft.uboss.form.sz600.formEngine2.DynFormTableInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import utils.UbossActionSupport;

public class FormDesignService extends UbossActionSupport {

    public void addForm(DynamicDict dict) throws BaseAppException {
        new FormManager().addForm(dict);

    }

    public void updateForm(DynamicDict dict) throws BaseAppException {
        new FormManager().updateForm(dict);

    }

    public void copyForm(DynamicDict dict) throws BaseAppException {
        new FormManager().copyForm(dict);
    }

    public void addFormVer(DynamicDict dict) throws BaseAppException {

        new FormManager().addFormVer(dict);

    }

    public void quryFormList(DynamicDict dict) throws BaseAppException {
        BoHelper.listDtoToBO("DATA_LIST", new FormManager().queryFormList(dict), DynFormDto.class, dict);

    }


    public int quryFormCount(DynamicDict dict) throws BaseAppException {
        return 0;

    }

    public void quryFormStepList(DynamicDict dict) throws BaseAppException {
        dict.set("DATA_LIST", new FormManager().queryFormStepList(dict));
        dict.set("DATA_COUNT", new FormManager().queryFormCount(dict));
    }

    public void queryFormDetail(DynamicDict dict) throws BaseAppException {
        BoHelper.dtoToBO(new FormManager().queryFormDetail(dict), dict);
    }

    public void queryFormVerDetail(DynamicDict dict) throws BaseAppException {
        BoHelper.dtoToBO(new FormManager().queryFormVerDetail(dict), dict);
    }

    public void queryFormAndVerDetail(DynamicDict dict) throws BaseAppException {
        dict.set("FORM_DETAIL", BoHelper.dtoToBO(new FormManager().queryFormDetail(dict), null));
        DynamicDict dict2 = BoHelper.dtoToBO(new FormManager().queryFormVerDetail(dict), null);
        setDict(dict2, dict);
    }

    public void isExistTableByName(DynamicDict dict) throws BaseAppException {
        String tableName = dict.getString("TABLE_NAME");
        dict.set("IS_EXIST", new FormManager().isExistTableByName(tableName));
    }

    public void getFormFieldInfo(DynamicDict dict) throws BaseAppException {
        String formId = dict.getString("DYN_FORM_ID");
        dict.set("bdFormInfo", BoHelper.dtoToBO(new DynForm().getFormInfo(formId), null));
        BoHelper.listDtoToBO("collFormTable", new DynForm().getTableList(formId), DynFormTableInfo.class, dict);
        BoHelper.listDtoToBO("collFormField", new DynForm().getFieldList(formId), DynFormFieldInfo.class, dict);
    }

    public void getFormAllInfo(DynamicDict dict) throws BaseAppException {
        String formId = dict.getString("DYN_FORM_ID");
        dict.set("bdFormInfo", BoHelper.dtoToBO(new DynForm().getFormInfo(formId), null));
        BoHelper.listDtoToBO("collFormTable", new DynForm().getTableList(formId), DynFormTableInfo.class, dict);
        BoHelper.listDtoToBO("collFormField", new DynForm().getFieldList(formId), DynFormFieldInfo.class, dict);
        dict.set("FORM_DETAIL", BoHelper.dtoToBO(new FormManager().queryFormDetail(dict), null));
        DynamicDict dict2 = BoHelper.dtoToBO(new FormManager().queryFormVerDetail(dict), null);
        setDict(dict2, dict);
    }

    public String getTableStr(DynamicDict dict) throws BaseAppException {
        String tableName = new DynForm().getTableStr(dict.getString("DYN_FORM_ID"));
        dict.set("TABLE_NAME", tableName);
        return tableName;
    }

    public void addSelectData(DynamicDict dict) throws BaseAppException {
        new SelectDataManager().addSelectData(dict);
    }

    public void updateSelectData(DynamicDict dict) throws BaseAppException {
        new SelectDataManager().updateSelectData(dict);
    }

    public void updateSelectDataState(DynamicDict dict) throws BaseAppException {
        new SelectDataManager().updateSelectDataState(dict);
    }

    public void querySelectDataDetail(DynamicDict dict) throws BaseAppException {
        new SelectDataManager().querySelectDataDetail(dict);
    }

    public void querySelectDataList(DynamicDict dict) throws BaseAppException {
        new SelectDataManager().querySelectDataList(dict);
    }

    public void addCat(DynamicDict dict) throws BaseAppException {
        new FormManager().addCat(dict);
    }

    public void updateCat(DynamicDict dict) throws BaseAppException {
        new FormManager().updateCat(dict);
    }

    public void deleteCat(DynamicDict dict) throws BaseAppException {
        new FormManager().deleteCat(dict);
    }

    public void queryCatList(DynamicDict dict) throws BaseAppException {
        new FormManager().queryCatList(dict);
    }

    public void queryDataDefStepList(DynamicDict dict) throws BaseAppException {
        new FormManager().queryDataDefStepList(dict);
    }

    public void queryCat(DynamicDict dict) throws BaseAppException {
        new FormManager().queryCat(dict);
    }

    public void setDict(DynamicDict dict1, DynamicDict dict2) throws BaseAppException {
        if (dict1 != null) {
            dict1.set("DESIGN_XML", dict1.getString("DESIGN_XML_STR"));
            dict1.set("DESIGN_HTML", dict1.getString("DESIGN_HTML_STR"));
            dict1.remove("DESIGN_XML_STR");
            dict1.remove("DESIGN_HTML_STR");
            dict2.set("FORM_VER_DETAIL", dict1);
        } else {
            dict2.set("FORM_VER_DETAIL", "");
        }
    }
}
