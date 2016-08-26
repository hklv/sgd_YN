package com.ztesoft.zsmartcity.sgd.items.dao;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;

/**
 * Created by liuhao 2016-5-25.
 * sgd
 */
public interface ItemDAO {
    /**
     * 根据各种条件查询事项库
     *
     * @param dict
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> qryItemsByParams(DynamicDict dict) throws BaseAppException;

    /**
     * qry items count(1)
     */
    int qryItemsCountByParams(DynamicDict dict) throws BaseAppException;

    /**
     * @param dict
     * @return
     * @throws BaseAppException
     */
    int qryLawsCount(DynamicDict dict) throws BaseAppException;

    /**
     * @param dict
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> qryLaws(DynamicDict dict) throws BaseAppException;
}
