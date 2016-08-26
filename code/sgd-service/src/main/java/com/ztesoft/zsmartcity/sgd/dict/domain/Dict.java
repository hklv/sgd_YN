package com.ztesoft.zsmartcity.sgd.dict.domain;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Table;
import com.ztesoft.sgd.base.beans.BaseDto;

/**
 * 字典.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-17 14:17
 */
@Table(name = "BFM_DICTIONARY", sequence = "DICT_SEQ")
public class Dict extends BaseDto {
    @Column
    private String name;
    @Column
    private String code;
    @Column
    private String state;
    @Column
    private String comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
