package com.ztesoft.uboss.bpm.test.dao;


import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

public interface IStaticFormDAO {
    int createForm(DynamicDict dict) throws BaseAppException;

    DynamicDict qryForm(DynamicDict dict) throws BaseAppException;

    int createManagerForm(DynamicDict dict) throws BaseAppException;

}
