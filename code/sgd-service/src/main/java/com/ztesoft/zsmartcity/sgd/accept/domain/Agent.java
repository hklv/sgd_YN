package com.ztesoft.zsmartcity.sgd.accept.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

import java.util.Date;

@Table(name = "SGD_AGENT", sequence = "AGENT_SEQ")
public class Agent extends BaseDto {
    @Column
    private Long applyId;
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
    private String state;
    @Column
    private String comments;
    private String genderStr;
    private String idTypeName;

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }


    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
        this.idNo = idNo == null ? null : idNo.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
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
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone == null ? null : fixedPhone.trim();
    }

    public String getPermanentAdd() {
        return permanentAdd;
    }

    public void setPermanentAdd(String permanentAdd) {
        this.permanentAdd = permanentAdd == null ? null : permanentAdd.trim();
    }

    public String getResidenceAdd() {
        return residenceAdd;
    }

    public void setResidenceAdd(String residenceAdd) {
        this.residenceAdd = residenceAdd == null ? null : residenceAdd.trim();
    }

    public String getMailingAdd() {
        return mailingAdd;
    }

    public void setMailingAdd(String mailingAdd) {
        this.mailingAdd = mailingAdd == null ? null : mailingAdd.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }
}