package com.ztesoft.uboss.bpm.setup.model;

import java.sql.Date;

public class BpmFormDto {
    //
    private long formId;
    //
    private String formName;

    private String formCode;

    private long catgId;

    //
    private String tableName;

    private String formType;

    private String applyType;

    private String formTypeName;

    //
    private String pageUrl;


    private Long dynamicFormId;

    private String dynamicFormName;

    public String getDynamicFormName() {
        return dynamicFormName;
    }

    public void setDynamicFormName(String dynamicFormName) {
        this.dynamicFormName = dynamicFormName;
    }

    //
    private String comments;
    //
    private Date stateDate;

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;

        if ("M".equals(formType)) {
            formTypeName = "手工表单";
        } else if ("D".equals(formType)) {
            formTypeName = "动态表单";
        }
    }

    public Long getDynamicFormId() {
        return dynamicFormId;
    }

    public void setDynamicFormId(Long dynamicFormId) {
        this.dynamicFormId = dynamicFormId;
    }

    public String getFormTypeName() {
        return formTypeName;
    }

    public void setFormTypeName(String formTypeName) {
        this.formTypeName = formTypeName;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public long getCatgId() {
        return catgId;
    }

    public void setCatgId(long catgId) {
        this.catgId = catgId;
    }


    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
}
