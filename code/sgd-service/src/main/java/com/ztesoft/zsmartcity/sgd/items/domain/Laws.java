package com.ztesoft.zsmartcity.sgd.items.domain;

import com.ztesoft.sgd.base.annotations.Column;


import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

import java.sql.Date;

/**
 * Created by asus-pc on 2016-5-23.
 */
@Table(name = "SGD_LAWS", sequence ="LAWS_SEQ")
public class Laws extends BaseDto {

    @Column
    private String name;
    @Column
    private Long typeId;
    @Column
    private String fileNo;
    @Column
    private String scope;
    @Column
    private Date exeDate;
    @Column
    private String pubDepartment;
    @Column
    private Date pubDate;
    @Column
    private String pubSummary;
    @Column
    private String file;
    @Column
    private String state;

    @Column
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getExeDate() {
        return exeDate;
    }

    public void setExeDate(Date exeDate) {
        this.exeDate = exeDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDepartment() {
        return pubDepartment;
    }

    public void setPubDepartment(String pubDepartment) {
        this.pubDepartment = pubDepartment;
    }

    public String getPubSummary() {
        return pubSummary;
    }

    public void setPubSummary(String pubSummary) {
        this.pubSummary = pubSummary;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }








    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }
}
