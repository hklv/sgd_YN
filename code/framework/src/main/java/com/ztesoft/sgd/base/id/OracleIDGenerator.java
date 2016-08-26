package com.ztesoft.sgd.base.id;

import com.ztesoft.sgd.base.helper.JdbcUtil4SGD;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import org.springframework.util.Assert;

/**
 * oracle主键自增长,sequence.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/5/18 上午9:41
 */
public class OracleIDGenerator implements IDGenerator {
    @Override
    public Long nextId(String sequenceName) throws BaseAppException {
        Assert.hasText(sequenceName);

        return SeqUtil.longValue(JdbcUtil4SGD.getDefaultDbService(), sequenceName);
    }
}
