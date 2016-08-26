package com.ztesoft.sgd.base.helper;


import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.RowSetFormatter;
import com.ztesoft.zsmart.core.service.ActionDomain;
import com.ztesoft.zsmart.core.service.DynamicDict;

/**
 * 对dict对象操作，获取分页设置对象的工具类<br>
 *
 * @author liuhao<br>
 * @version 1.0 2015年6月7日 上午11:18:02<br>
 */
public abstract  class PageHelper {
    public static RowSetFormatter getRowSetFormat(DynamicDict dict) throws BaseAppException {
        DynamicDict pagingFilter = dict.getBO(ActionDomain.BO_FIELD_QUERY_PAGE);
        RowSetFormatter rf = null;
        if (pagingFilter != null) {
            rf = new RowSetFormatter();
            Long pageSize = pagingFilter.getLong(ActionDomain.BO_FIELD_QUERY_PAGESIZE, false);
            if (pageSize != null) {
                rf.setPageSize(pageSize.intValue());
                rf.setPageIndex(pagingFilter.getLong(ActionDomain.BO_FIELD_QUERY_PAGEINDEX).intValue());
            }
            int count = pagingFilter.getCount(ActionDomain.BO_FIELD_QUERY_ORDER);
            if (count > 0) {
                String[] orderFields = new String[count];
                for (int i = 0; i < count; i++) {
                    orderFields[i] = pagingFilter.getString(ActionDomain.BO_FIELD_QUERY_ORDER, i);
                }
                rf.setOrderFields(orderFields);
            }
        }
        return rf;
    }
}
