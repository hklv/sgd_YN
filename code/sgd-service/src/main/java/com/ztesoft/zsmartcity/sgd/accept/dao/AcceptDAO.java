package com.ztesoft.zsmartcity.sgd.accept.dao;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

/**
 * Created by ZhangLu on 2016/6/15.
 */
public interface AcceptDAO {
    /**
     * 保存一个申请人信息
     *
     * @param dict
     * @throws BaseAppException
     */
    void saveAccept(DynamicDict dict) throws BaseAppException;
}
