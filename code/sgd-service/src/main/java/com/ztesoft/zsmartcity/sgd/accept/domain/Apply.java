package com.ztesoft.zsmartcity.sgd.accept.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

import java.util.Date;

@Table(name = "SGD_APPLY", sequence = "APPLY_SEQ")
public class Apply extends BaseDto {
    @Column
    private String serialNum;
    @Column
    private Integer itemId;
    @Column
    private Integer applyTypeId;
    @Column
    private String name;
    @Column
    private Integer idTypeId;
    @Column
    private String idNo;
    @Column
    private String gender;
    @Column
    private Date birthDate;
    @Column
    private String mobilePhone;
    @Column
    private String fixedPhone;
    @Column
    private String permanentAdd;
    @Column
    private String residenceAdd;
    @Column
    private String mailingAdd;
    @Column
    private String isMsg;
    @Column
    private String state;

    @Column
    private String comments;

    @Column
    private String holderNo;

    private String procDefId;

    private String idTypeName;

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getApplyTypeId() {
        return applyTypeId;
    }

    public void setApplyTypeId(Integer applyTypeId) {
        this.applyTypeId = applyTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdTypeId() {
        return idTypeId;
    }

    public void setIdTypeId(Integer idTypeId) {
        this.idTypeId = idTypeId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getPermanentAdd() {
        return permanentAdd;
    }

    public void setPermanentAdd(String permanentAdd) {
        this.permanentAdd = permanentAdd;
    }

    public String getResidenceAdd() {
        return residenceAdd;
    }

    public void setResidenceAdd(String residenceAdd) {
        this.residenceAdd = residenceAdd;
    }

    public String getMailingAdd() {
        return mailingAdd;
    }

    public void setMailingAdd(String mailingAdd) {
        this.mailingAdd = mailingAdd;
    }

    public String getIsMsg() {
        return isMsg;
    }

    public void setIsMsg(String isMsg) {
        this.isMsg = isMsg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHolderNo() {
        return holderNo;
    }

    public void setHolderNo(String holderNo) {
        this.holderNo = holderNo;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }
}