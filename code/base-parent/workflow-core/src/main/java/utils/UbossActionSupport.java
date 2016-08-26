package utils;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.IAction;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.apache.commons.lang.math.NumberUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 公用的接口实现类
 *
 * @author lv huikang (mailto:lv.huikang@zte.com.cn))
 * @since 2016/5/30
 */
public class UbossActionSupport implements IAction {

    private ZSmartLogger logger = ZSmartLogger.getLogger(UbossActionSupport.class);

    @Override
    public int perform(DynamicDict dict) throws BaseAppException {

        String methodName = (String) dict.get("method");
        Method method;
        Object obj = null;
        try {
            method = this.getClass().getMethod(methodName, DynamicDict.class);
            obj = method.invoke(this, dict);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error(String.format("no such method named '%s'!", methodName));
            ExceptionHandler.publish("callMethod Has Exception", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            ExceptionHandler.publish("callMethod Has Exception", e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            ExceptionHandler.publish("callMethod Has Exception", e);
        }
        if (obj != null && NumberUtils.isNumber(obj.toString())) {
            return Integer.valueOf(obj.toString());
        }
        return 0;
    }
}
