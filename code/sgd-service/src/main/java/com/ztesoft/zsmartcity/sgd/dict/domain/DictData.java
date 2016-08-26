package com.ztesoft.zsmartcity.sgd.dict.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

/**
 * Created by asus-pc on 2016-5-17.
 */
@Table(name = "BFM_DICTIONARY_DATA", sequence = "DICT_DATA_SEQ")
public class DictData extends BaseDto {
    @Column
    private Long dictionaryId;

    @Column
    private String valueName;

    @Column
    private String valueCode;

    @Column
    private String isLocked;

    @Column
    private String state;

    @Column
    private String comments;

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
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
}
