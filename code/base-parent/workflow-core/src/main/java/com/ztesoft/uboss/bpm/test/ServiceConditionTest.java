package com.ztesoft.uboss.bpm.test;


import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import utils.UbossActionSupport;

public class ServiceConditionTest extends UbossActionSupport {
    public Boolean passcondition(DynamicDict dict) throws BaseAppException {
        return true;
    }

    public boolean unPasscondition(DynamicDict dict) throws BaseAppException {
        return false;
    }
}
