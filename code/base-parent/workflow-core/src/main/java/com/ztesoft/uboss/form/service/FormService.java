package com.ztesoft.uboss.form.service;

import com.ztesoft.uboss.form.bll.FormManager;
import com.ztesoft.uboss.form.bll.SelectDataManager;
import com.ztesoft.uboss.form.model.DynFormVerDto;
import com.ztesoft.uboss.form.sz600.formEngine2.DynForm;
import com.ztesoft.uboss.form.sz600.formEngine2.DynFormFieldInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.XMLDom4jUtils;
import org.dom4j.Document;
import utils.UbossActionSupport;

import java.util.ArrayList;
import java.util.HashMap;

public class FormService extends UbossActionSupport {

    private static HashMap<String, ArrayList<DynFormFieldInfo>> dynFormFieldList = new HashMap<String, ArrayList<DynFormFieldInfo>>(); //动态表单字段列表
    private static HashMap<String, String> dynFormConditionHTML = new HashMap<String, String>(); //动态表单查询条件语句html

    /**
     * 查询动态表单by id
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int qryDynFormById(DynamicDict dict) throws BaseAppException {
        DynFormVerDto dto = new FormManager().queryFormVerDetail(dict);
        BoHelper.dtoToBO(dto, dict);
        dict.set("FORM_DETAIL", BoHelper.dtoToBO(new FormManager().queryFormDetail(dict), null));
        return 0;
    }

    /**
     * 刷新动态表单字段列表
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public static int refreshFieldList(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        dynFormFieldList.remove(dynFormId);
        dynFormConditionHTML.remove(dynFormId);
        return 0;
    }

    /**
     * 查询动态表单的字段属性
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int qryDynFormFieldList(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = dynFormFieldList.get(dynFormId);
        if (formFieldList == null) {
            formFieldList = new DynForm().getFieldList(dynFormId);
            dynFormFieldList.put(dynFormId, formFieldList);
        }
        BoHelper.listDtoToBO("formFieldList", formFieldList, DynFormFieldInfo.class, dict);
        //dict.set("formFieldList", formFieldList);
        return 0;
    }

    /**
     * 查询动态表单的数据by orderId
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int qryDynFormData(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = dynFormFieldList.get(dynFormId);
        if (formFieldList == null || formFieldList.size() == 0) {
            formFieldList = new DynForm().getFieldList(dynFormId);
            dynFormFieldList.put(dynFormId, formFieldList);
        }
        dict.set("formFieldList", formFieldList);
        new FormManager().qryDynFormData(dict);
        return 0;
    }

    /**
     * 保存带表form提交的数据
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int saveFormData(DynamicDict dict) throws BaseAppException {
        new FormManager().saveFormData(dict);
        return 0;
    }

    /**
     * 保存task  form提交的数据
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int saveTaskFormData(DynamicDict dict) throws BaseAppException {
        new FormManager().saveTaskFormData(dict);
        return 0;
    }

    /**
     * 保存task  form提交的数据 到对应DYB_TABLENAME数据表中。by liuhao
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int saveTaskFormData2(DynamicDict dict) throws BaseAppException {
        // new FormManager().saveTaskFormData(dict);
        //edit by liuhao 原先保存环节表单的方法注释掉 ，改为用保存起始表单的方法保存环节表单的数据
        new FormManager().saveFormData(dict);
        return 0;
    }

    /**
     * 保存task  form提交的数据
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int modTaskFormData(DynamicDict dict) throws BaseAppException {
        new FormManager().modTaskFormData(dict);
        return 0;
    }

    /**
     * 查询环节处理结果(动态表单内容)
     *
     * @param dict
     * @throws BaseAppException
     */
    public int queryDynTaskResult(DynamicDict dict) throws BaseAppException {
        new FormManager().queryDynTaskResult(dict);
        return 0;
    }

    /**
     * 查询弹出选择框关联内容
     *
     * @param dict
     * @throws BaseAppException
     */
    public void querySelectDataDetail(DynamicDict dict) throws BaseAppException {
        new SelectDataManager().querySelectDataDetail(dict);
    }

    public void getHtmlIdToTableColumn(DynamicDict dict) throws BaseAppException {
        dict.set("filedInfo", new FormManager().getHtmlIdToTableColumn2Map(dict.getLong("DYN_FORM_ID"), dict.getLong("VER_ID")));

    }

    public void queryDynFormDataList(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        if (dynFormId == null || dynFormId.length() <= 0)
            dynFormId = dict.getString("DYN_FORM_ID");
        if (dynFormId == null || dynFormId.length() <= 0) return;

        ArrayList<DynFormFieldInfo> formFieldList = dynFormFieldList.get(dynFormId);
        if (formFieldList == null) {
            formFieldList = new DynForm().getFieldList(dynFormId);
            dynFormFieldList.put(dynFormId, formFieldList);
        }
        dict.set("formFieldList", formFieldList);
        dict.set("DATA_LIST", new FormManager().queryDynFormDataList(dict));
        dict.remove("formFieldList");
    }

    public void queryDynFormDataStepList(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        if (dynFormId == null || dynFormId.length() <= 0)
            dynFormId = dict.getString("DYN_FORM_ID");
        if (dynFormId == null || dynFormId.length() <= 0) return;

        ArrayList<DynFormFieldInfo> formFieldList = dynFormFieldList.get(dynFormId);
        if (formFieldList == null) {
            formFieldList = new DynForm().getFieldList(dynFormId);
            dynFormFieldList.put(dynFormId, formFieldList);
        }
        dict.set("formFieldList", formFieldList);
        dict.set("DATA_LIST", new FormManager().queryDynFormDataStepList(dict));
        dict.set("DATA_COUNT", new FormManager().queryDynFormDataCount(dict));
        dict.remove("formFieldList");
    }

    public void getDynFormDataListQryPageInfo(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = dynFormFieldList.get(dynFormId);
        if (formFieldList == null) {
            formFieldList = new DynForm().getFieldList(dynFormId);
            dynFormFieldList.put(dynFormId, formFieldList);
        }
        String html = dynFormConditionHTML.get(dynFormId);
        if (html == null) {
            html = new FormManager().queryFormHTML(dynFormId);
            Document document = XMLDom4jUtils.createDocument(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT></ROOT>",
                    false, null);
            dynFormConditionHTML.put(dynFormId, html);
        }

        BoHelper.listDtoToBO("formFieldList", formFieldList, DynFormFieldInfo.class, dict);
    }

    /**
     * 查询动态表单的数据by orderId
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    public int qryDynFormDataByDynId(DynamicDict dict) throws BaseAppException {
        String dynFormId = dict.getString("DYN_FORM_ID");
        ArrayList<DynFormFieldInfo> formFieldList = dynFormFieldList.get(dynFormId);
        if (formFieldList == null) {
            formFieldList = new DynForm().getFieldList(dynFormId);
            dynFormFieldList.put(dynFormId, formFieldList);
        }
        dict.set("formFieldList", formFieldList);
        new FormManager().qryDynFormDataByDynId(dict);
        return 0;
    }

    public int updateDynTableValue(DynamicDict dict) throws BaseAppException {

        new DynForm().updateDynTableValue(dict);
        return 0;

    }
}
