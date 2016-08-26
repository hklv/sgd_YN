package com.ztesoft.sgd.base.helper;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.BoHelper;

import java.util.Map;

/**
 * 调用外部服务 <br>
 *
 * @author sunhao<br>
 * @version 1.0 15/5/15<br>
 */
public abstract class RemoteHelper {

    /**
     * Description: 调外部服务,返回对象
     *
     * @param serviceName 服务service name
     * @param returnType  返回类型
     * @param params      参数
     * @param <T>         <br>
     * @return <T> T BO
     * @throws BaseAppException <br>
     */
    public static <T> T call(String serviceName, Class<T> returnType, Map<String, Object> params) throws BaseAppException {
        AssertUtil.hasLength(serviceName, "service name is required!");
        AssertUtil.isNotNull(returnType, "return type is required!");

        DynamicDict dict = new DynamicDict();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                dict.add(entry.getKey(), entry.getValue());
            }
        }
        dict.serviceName = serviceName;

        ServiceFlow.callService(false, dict);

        return (T) BoHelper.boToDto(dict, returnType);
    }
}
