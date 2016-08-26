package com.ztesoft.sgd.base.beans;

import com.ztesoft.sgd.base.annotations.Column;
import com.ztesoft.sgd.base.annotations.Id;
import com.ztesoft.zsmart.core.jdbc.AbstractDto;

import java.util.Date;

/**
 * dto公共字段.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-27 09:40
 */
public class BaseDto extends AbstractDto {
    /**
     * 主键
     */
    @Id
    private Long id;
    /**
     * 创建人
     */
    @Column
    private Long createUser;
    /**
     * 创建时间
     */
    @Column
    private Date createDate;
    /**
     * 更新人
     */
    @Column
    private Long updateUser;
    /**
     * 更新时间
     */
    @Column
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
