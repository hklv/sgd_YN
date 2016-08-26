package com.ztesoft.sgd.base.id;

import com.ztesoft.zsmart.core.exception.BaseAppException;

/**
 * make database id.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/17 下午3:07
 */
public interface IDGenerator {
    /**
     * get next long value
     *
     * @param sequenceName sequenceName
     * @return
     */
    Long nextId(String sequenceName) throws BaseAppException;
}
