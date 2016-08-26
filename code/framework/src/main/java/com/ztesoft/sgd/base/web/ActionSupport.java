package com.ztesoft.sgd.base.web;

import com.ztesoft.sgd.base.helper.StringHelper;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.IAction;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 继承此类,不需要再写{@link #perform}方法.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2016-5-19 10:29
 */
public class ActionSupport implements IAction {
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(ActionSupport.class);

    @Override
    public int perform(DynamicDict dynamicDict) throws BaseAppException {
        String serviceName = dynamicDict.serviceName;
        String methodName = toMethodName(serviceName);

        try {
            Method method = this.getClass().getMethod(methodName, DynamicDict.class);

            Object obj = method.invoke(this, dynamicDict);
            if (obj != null && NumberUtils.isNumber(obj.toString())) {
                return Integer.valueOf(obj.toString());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error(String.format("no such method named '%s'!", methodName));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }

        return -1;
    }

    private String toMethodName(String serviceName) {
        Assert.hasText(serviceName);

        String first = StringHelper.substring(serviceName, 0, 1);
        String others = StringHelper.substring(serviceName, 1);

        return StringHelper.lowerCase(first) + others;
    }
}
