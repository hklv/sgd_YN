package com.ztesoft.uboss.form.dao;

import com.ztesoft.uboss.form.model.DynFormCatDto;
import com.ztesoft.uboss.form.model.DynFormDto;
import com.ztesoft.uboss.form.model.DynFormVerDto;
import com.ztesoft.uboss.form.model.DynTaskResult;
import com.ztesoft.uboss.form.sz600.formEngine2.DynFormFieldInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IFormDAO {

    long addForm(DynFormDto dynFormDto) throws BaseAppException;

    void addForm(DynamicDict dict) throws BaseAppException;

    void addFormVer(DynFormVerDto dynFormVerDto) throws BaseAppException;

    void addFormVer(DynamicDict dict) throws BaseAppException;

    void addFormVer(String dynFormId, String xmlStr, String htmlStr, String version, String userId) throws BaseAppException;

    void addFormVer(String dynFormId, String version, String userId) throws BaseAppException;

    void addFormVer(String dynFormId, String xmlStr, String version, String userId) throws BaseAppException;

    void updateForm(DynFormDto dynFormDto) throws BaseAppException;

    void updateForm(DynamicDict dict) throws BaseAppException;

    void updateFormVer(DynFormVerDto dynFormVerDto) throws BaseAppException;

    void updateFormVer(DynamicDict dict) throws BaseAppException;

    void updateFormVerXML(String dynFormId, String xml) throws BaseAppException;

    void updateFormVerHTML(String dynFormId, String html) throws BaseAppException;

    void updateFormState(long dynFormId) throws BaseAppException;

    void updateFormVerState(long dynFormId) throws BaseAppException;

    void updateFormVerStateById(long dynFormVerId, String state) throws BaseAppException;

    int queryFormCount(DynamicDict dict) throws BaseAppException;

    List<DynFormDto> queryFormList(DynFormDto dynFormDto) throws BaseAppException;

    List<DynamicDict> queryFormStepList(DynamicDict dict) throws BaseAppException;

    List<DynFormVerDto> queryFormVerList(long dynFormId) throws BaseAppException;

    DynFormDto queryForm(long dynFormId) throws BaseAppException;

    DynFormVerDto queryFormVer(long verId) throws BaseAppException;

    DynFormVerDto queryActiveFormVer(long dynFormId) throws BaseAppException;

    String queryFormHTML(String dynFormId) throws BaseAppException;

    String queryFormXML(String dynFormId) throws BaseAppException;

    long queryMaxVerId(String dynFormId, String state) throws BaseAppException;

    int saveFormData(String sql, DynamicDict varMap, ArrayList<DynFormFieldInfo> formFieldList) throws BaseAppException;

    DynamicDict qryDynFormData(String tableName, String holderNo) throws BaseAppException;

    List<Map> getHtmlIdToTableColumn(String dynFormId, String dynFormVerId) throws BaseAppException;

    int[] saveTaskFormData(String taskListId, List<DynamicDict> fieldList) throws BaseAppException;

    List<DynTaskResult> queryDynTaskResult(String taskListId) throws BaseAppException;

    int delDynTaskResult(String taskListId) throws BaseAppException;

    List<DynamicDict> queryDynFormDataList(String sql, ParamMap pm) throws BaseAppException;

    List<DynamicDict> queryDynFormDataStepList(String sql, ParamMap pm, RowSetFormatter rowSetFormatter) throws BaseAppException;

    int queryDynFormDataCount(String sql, ParamMap pm) throws BaseAppException;

    DynamicDict qryDynFormDataByDynId(String tableName, long dynId) throws BaseAppException;


    long addCat(DynFormCatDto dynFormCatDto) throws BaseAppException;

    void updateCat(DynFormCatDto dynFormCatDto) throws BaseAppException;

    void deleteCat(DynFormCatDto dynFormCatDto) throws BaseAppException;

    long queryCatCount(DynamicDict dict) throws BaseAppException;

    long isExist(DynamicDict dict) throws BaseAppException;

    List<DynFormCatDto> queryCatList(DynamicDict dict) throws BaseAppException;

    List<DynFormCatDto> queryCatStepList(DynamicDict dict) throws BaseAppException;

    DynFormCatDto queryCat(DynamicDict dict) throws BaseAppException;

    void executeUpdate(String sql) throws BaseAppException;

    int updateDynTableValue(DynamicDict dict) throws BaseAppException;
}
