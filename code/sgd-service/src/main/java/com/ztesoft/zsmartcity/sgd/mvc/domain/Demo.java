package com.ztesoft.zsmartcity.sgd.mvc.domain;

import com.ztesoft.zsmartcity.sgd.mvc.domain.enums.Sex;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午4:48
 */
public class Demo {
    private Long id;
    private String name;
    private Integer age;
    private Sex sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
