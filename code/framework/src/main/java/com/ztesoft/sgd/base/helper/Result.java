package com.ztesoft.sgd.base.helper;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.BlobWrapper;
import com.ztesoft.zsmart.core.jdbc.ClobWrapper;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.BoHelper;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果集<br>
 *
 * @author sunhao<br>
 * @version 1.0 2015年6月1日 下午4:09:55<br>
 */
public final class Result {

    /**
     * Success
     */
    public static final String REQ_SUCCESS = "Success!";

    /**
     * Failed
     */
    public static final String REQ_FAILURE = "Failed!";

    /**
     * result
     */
    private Boolean result = Boolean.FALSE;

    /**
     * msg
     */
    private String msg;

    /**
     * model
     */
    private Map<String, Object> model = new HashMap<String, Object>();

    /**
     * 设置result值 <br>
     *
     * @param result <br>
     * @return Result
     */
    public Result setResult(Boolean result) {
        this.result = result;
        return this;
    }

    /**
     * Description: <br>
     *
     * @param msg String
     * @return Result
     */
    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * 追加一个map参数 <br>
     *
     * @param model <br>
     * @return Result
     */
    public Result append(Map<String, Object> model) {
        if (this.model == null) {
            this.model = new HashMap<String, Object>();
        }

        this.model.putAll(model);
        return this;
    }

    /**
     * 追加键值对 <br>
     *
     * @param key   String 键
     * @param value Object 值
     * @return Result
     */
    public Result append(String key, Object value) {
        if (this.model == null) {
            this.model = new HashMap<String, Object>();
        }

        this.model.put(key, value);
        return this;
    }

    /**
     * 将result追加到DynamicDict中
     *
     * @param dict DynamicDict
     * @throws BaseAppException <br>
     */
    public void appendTo(DynamicDict dict) throws BaseAppException {
        AssertUtil.isNotNull(dict, "DynamicDict is required!");

        dict.set("RESULT", this.result);
        dict.set("MSG", this.msg);
        for (Map.Entry<String, Object> entry : this.model.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            Class<?> valueClazz = value.getClass();
            if (isSimpleType(valueClazz)) {
                dict.set(key, value);
            } else {
                dict.set(key, BoHelper.dtoToBO(value, new DynamicDict()));
            }
        }
    }

    /**
     * Description:
     *
     * @param fieldType <br/>
     * @return boolean <br/>
     */
    private boolean isSimpleType(Class<?> fieldType) {
        if (fieldType.isPrimitive()) {
            return true;
        } else if (fieldType.equals(Integer.class) || fieldType.equals(Long.class) || fieldType.equals(String.class)
                || fieldType.equals(Boolean.class) || fieldType.equals(BlobWrapper.class)
                || fieldType.equals(ClobWrapper.class) || fieldType.equals(Date.class)
                || fieldType.equals(java.util.Date.class)) {
            return true;
        }

        return false;
    }

    public Boolean getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
