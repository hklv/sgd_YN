package com.ztesoft.zsmartcity.sgd.mvc.domain.enums;

import lodsve.core.bean.Codeable;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午4:48
 */
public enum Sex implements Codeable {
    NONE("0", "性别不详"),
    MALE("1", "男性"),
    FEMALE("2", "女性");

    private String code;
    private String title;

    Sex(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
