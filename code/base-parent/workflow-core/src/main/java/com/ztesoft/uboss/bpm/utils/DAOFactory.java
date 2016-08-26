package com.ztesoft.uboss.bpm.utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.BaseDAO;
import com.ztesoft.zsmart.core.jdbc.JdbcUtil;

/**
 * 获取DAO的工具类
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/5/10
 */
public class DAOFactory {
    public static BaseDAO getDAO(Class daoClazz) throws BaseAppException, IllegalAccessException, InstantiationException {
        BaseDAO e = (BaseDAO) daoClazz.newInstance();
        e.initRouting();
        e.setConnection(JdbcUtil.getDbIdentifier());
        return e;
    }
}
