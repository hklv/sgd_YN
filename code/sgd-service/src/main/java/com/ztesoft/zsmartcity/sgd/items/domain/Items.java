package com.ztesoft.zsmartcity.sgd.items.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

/**
 * Created by liu.hao-pc on 2016-5-23.
 */
@Table(name = "SGD_ITEMS", sequence ="ITEM_SEQ")
public class Items extends BaseDto {


    @Column
    private String name;
    @Column
    private String itemCode;
    @Column
    private String spTypeId;
    @Column
    private Long orgId;
    @Column
    private String spObject;
    @Column
    private String setGist;
    @Column
    private String dealCondition;
    @Column
    private Long legalPeriod;
    @Column
    private Long promisePeriod;
    @Column
    private String fee;

    @Column
    private String state;

    @Column
    private String comments;
    @Column
    private String procDefId;

    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }





    public String getDealCondition() {
        return dealCondition;
    }

    public void setDealCondition(String dealCondition) {
        this.dealCondition = dealCondition;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }



    public Long getLegalPeriod() {
        return legalPeriod;
    }

    public void setLegalPeriod(Long legalPeriod) {
        this.legalPeriod = legalPeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getPromisePeriod() {
        return promisePeriod;
    }

    public void setPromisePeriod(Long promisePeriod) {
        this.promisePeriod = promisePeriod;
    }

    public String getSetGist() {
        return setGist;
    }

    public void setSetGist(String setGist) {
        this.setGist = setGist;
    }

    public String getSpObject() {
        return spObject;
    }

    public void setSpObject(String spObject) {
        this.spObject = spObject;
    }

    public String getSpTypeId() {
        return spTypeId;
    }

    public void setSpTypeId(String spTypeId) {
        this.spTypeId = spTypeId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }



}
