package com.ztesoft.zsmartcity.sgd.attachment.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

/**
 * Created by liuhao on 2016-6-14.
 * 2016-6-14
 * sgd-service
 */
@Table(name = "BFM_ATTACHMENT", sequence ="BFM_ATTACHMENT_SEQ")
public class Attachment extends BaseDto {
    @Column
    private String aliasName;
    @Column
    private String trueName;
    @Column
    private String path;
    @Column
    private String fileSize;
    @Column
    private String fileType;
    @Column
    private String state;
    @Column
    private String comments;

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

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
