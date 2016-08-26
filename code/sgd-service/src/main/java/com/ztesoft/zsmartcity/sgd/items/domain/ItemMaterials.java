package com.ztesoft.zsmartcity.sgd.items.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

/**
 * Created by liu.hao on 2016-5-23.
 */
@Table(name = "SGD_ITEM_MATERIALS", sequence ="SGD_ITEM_MATERIALS_SEQ")
public class ItemMaterials extends BaseDto{

    public  ItemMaterials(){

    }
    @Column
    private Long itemId;
    @Column
    private String name;
    @Column
    private Long formatId;
    @Column
    private String requirement;
    @Column
    private Long num;
    @Column
    private String state;

    @Column
    private String comments;

    private String aliasName;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }







    public Long getFormatId() {
        return formatId;
    }

    public void setFormatId(Long formatId) {
        this.formatId = formatId;
    }


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }







}
