/******************************************************************************
 * All rights are reserved. Reproduction or transmission in whole or in part， in
 * any form or by any means， electronic， mechanical or otherwise， is prohibited
 * without the prior written consent of the copyright owner
 ********************************************************************************/
package com.ztesoft.uboss.admin.serviceconfig.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.admin.serviceconfig.dao.ICatDAO;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.BaseDAO;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Filename: CatDAO.java
 * @Author: cao.shilei
 * @CreateDate: 2007-10-30
 * @decription:目录管理DAO
 */
public class CatDAO extends BaseDAO implements ICatDAO {

    private static BusiBaseDAO busiBaseDAO = new BusiBaseDAO();

    @SuppressWarnings("unchecked")
    public List<DynamicDict> qryAllCatTree() throws BaseAppException {
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("SELECT CAT_CODE, CAT_NAME, TFM_CAT_CODE, CAT_DESC").append(
                " FROM TFM_SERVICE_CAT ORDER BY TFM_CAT_CODE asc");
        List<DynamicDict> list = (List<DynamicDict>) query(sqlStr.toString(), null, null, new RowSetMapper() {
            public Object mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException, BaseAppException {

                List<DynamicDict> list = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    DynamicDict cat = new DynamicDict();
                    cat.setValueByName("id", op.getString(rs, "CAT_CODE"));
                    cat.setValueByName("name", op.getString(rs, "CAT_NAME"));
                    cat.setValueByName("parent", op.getString(rs, "TFM_CAT_CODE"));
                    cat.setValueByName("CAT_DESC", op.getString(rs, "CAT_DESC"));
                    list.add(cat);
                }

                return list;
            }

        });

        List<DynamicDict> newList = new ArrayList<DynamicDict>();
        for (DynamicDict dict : list) {
            if (dict.getString("parent") == null) {
                newList.add(dict);
                Map<String, String> keyMap = new HashMap<String, String>();
                keyMap.put("ID", "id");
                keyMap.put("PARENT_ID", "parent");
                keyMap.put("PARENT_CODE", dict.getString(keyMap.get("ID")));
                busiBaseDAO.recuriveList(keyMap, list, newList);
            }
        }
        return newList;
    }

}
