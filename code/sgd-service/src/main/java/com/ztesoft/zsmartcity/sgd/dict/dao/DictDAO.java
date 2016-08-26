package com.ztesoft.zsmartcity.sgd.dict.dao;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;

/**
 * Created by liuhao 2016-5-25.
 * sgd
 */
public interface DictDAO {
    List<DynamicDict> qryDictDataByCode(DynamicDict dict) throws BaseAppException;
    int qryDictCount(DynamicDict dict) throws BaseAppException;
}
