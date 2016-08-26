package com.ztesoft.zsmartcity.sgd.accept.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

@Table(name = "SGD_APPLY_MATERIALS", sequence = "APPLY_MATERIALS_SEQ")
public class ApplyMaterials extends BaseDto {
    @Column
    private Long applyId;
    @Column
    private Long materialsId;
    @Column
    private Integer attachId;
    @Column
    private String state;
    @Column
    private String comments;

    private String aliasName;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getMaterialsId() {
        return materialsId;
    }

    public void setMaterialsId(Long materialsId) {
        this.materialsId = materialsId;
    }

    public Integer getAttachId() {
        return attachId;
    }

    public void setAttachId(Integer attachId) {
        this.attachId = attachId;
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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}