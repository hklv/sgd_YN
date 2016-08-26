package com.ztesoft.zsmartcity.sgd.dict.dao.impl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.sgd.base.helper.PageHelper;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetMapper;
import com.ztesoft.zsmart.core.jdbc.rowset.RowSetOperator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;
import com.ztesoft.zsmartcity.sgd.dict.dao.DictDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao 2016-5-25.
 * sgd
 * 模糊查询与分页
 */
public class DictDAOImpl extends BusiBaseDAO implements DictDAO {
    @Override
    public List<DynamicDict> qryDictDataByCode(DynamicDict dict) throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from bfm_dictionary  where 1=1 and state ='A'  [and CODE like :CODE]");
        ParamMap pm  = new ParamMap();
        ParamMapHelper.setValue(pm,"CODE",dict.getString("CODE"));
        // 增加分页逻辑
        RowSetFormatter rf  = PageHelper.getRowSetFormat(dict);
        return  this.query(sql.toString(), pm, rf, null, new RowSetMapper<List<DynamicDict>>() {
            public List<DynamicDict> mapRows(RowSetOperator op, ResultSet rs, int colNum, Object para) throws SQLException,
                    BaseAppException {
                List<DynamicDict> attrDefDtoList = new ArrayList<DynamicDict>();
                while (rs.next()) {
                    int flag = 1;
                    op.getLong(rs, flag++);
                    DynamicDict dict = new DynamicDict();
                    dict.set("ID", op.getLong(rs, flag++));
                    dict.set("NAME", op.getString(rs, flag++));
                    dict.set("CODE", op.getString(rs, flag++));
                    dict.set("STATE", op.getString(rs, flag++));
                    dict.set("CREATE_USER", op.getLong(rs, flag++));
                    dict.set("CREATE_DATE", op.getString(rs, flag++));
                    dict.set("UPDATE_USER", op.getLong(rs, flag++));
                    dict.set("UPDATE_DATE", op.getString(rs, flag++));
                    dict.set("COMMENTS", op.getString(rs, flag++));
                    attrDefDtoList.add(dict);
                }
                return attrDefDtoList;
            }
        });
    }

    @Override
    public int qryDictCount(DynamicDict dict) throws BaseAppException {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from bfm_dictionary where  1=1 and state ='A' " +
                "[ and CODE like :CODE]");
        ParamMap map = new ParamMap();
        ParamMapHelper.setValue(map,"CODE",dict.getString("CODE"));
        return (int) selectCount(sql.toString(),map);

    }


}
