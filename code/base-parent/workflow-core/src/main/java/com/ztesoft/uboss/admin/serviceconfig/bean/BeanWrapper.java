package com.ztesoft.uboss.admin.serviceconfig.bean;


import com.ztesoft.zsmart.core.jdbc.AbstractDto;
import com.ztesoft.zsmart.core.utils.AssertUtil;

/**
 * @param <T>
 * @author ZEN 2012-4-27
 */
public abstract class BeanWrapper<T extends AbstractDto> {
    protected T dto;

    public BeanWrapper(T dto) {
        AssertUtil.isNotNull(dto);
        this.dto = dto;
    }

    public T getDto() {
        return dto;
    }

    @SuppressWarnings("unchecked")
    public T getDtoCopy() {
        return (T) dto.copy();
    }

    public void setDto(T dto) {
        this.dto = dto;
    }


}
