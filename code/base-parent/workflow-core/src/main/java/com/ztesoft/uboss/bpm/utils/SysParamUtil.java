/****************************************************************************************
 * Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 * transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 * or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.*;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SysParamUtil {

    private static Map<String, Object> systemParamMap = new ConcurrentHashMap<String, Object>();

    public static Map<String, Object> getValueMap() {
        return systemParamMap;
    }

    /**
     * @description 根据某个参数的MASK获取某个参数信息-先从缓存取<br>
     * @param mask
     * @return
     * @throws BaseAppException
     * <br>
     */
    @SuppressWarnings("unchecked")
    public static DynamicDict queryParamByMask(String mask)
            throws BaseAppException {
        /*if (systemParamMap.get(mask) != null) {  //禁用缓存
			return (DynamicDict) systemParamMap.get(mask);
		}*/
        DynamicDict dict = null;
        Session ses = SessionContext.currentSession();
        ses.beginTrans();
        try {
            String sql = "SELECT PARAM,PARAM_NAME,CURRENT_VALUE,COMMENTS,MASK FROM BFM_PARAMS WHERE MASK = ?";
            ParamArray pa = new ParamArray();
            pa.set("", mask);
            dict = (DynamicDict) JdbcTemplate.query(JdbcUtil.getDbIdentifier(),
                    sql, pa, null, new RowSetMapper() {
                        public Object mapRows(RowSetOperator op, ResultSet rs,
                                              int colNum, Object para) throws SQLException,
                                BaseAppException {
                            DynamicDict temp = null;
                            if (rs.next()) {
                                temp = new DynamicDict();
                                int flag = 1;
                                temp.set("PARAM", rs.getObject(flag++));
                                temp.set("PARAM_NAME", rs.getObject(flag++));
                                temp.set("CURRENT_VALUE", rs.getObject(flag++));
                                temp.set("COMMENTS", rs.getObject(flag++));
                                temp.set("MASK", rs.getObject(flag++));
                            }
                            return temp;
                        }
                    });
            ses.commitTrans();
        } catch (BaseAppException ex) {
            throw new BaseAppException(
                    "Error occurred when query system param by mask.", ex);
        } finally {
            ses.releaseTrans();
        }
		/*if (dict != null) {
			systemParamMap.put(mask, dict);
		}*/
        return dict;
    }

    public static String getParamValueByMask(String mask) throws BaseAppException {
        DynamicDict bo = queryParamByMask(mask);
        if (bo != null) {
            return bo.getString("CURRENT_VALUE");
        }
        return null;
    }
}
