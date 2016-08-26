package com.ztesoft.sgd.base.helper;

/**
 * 参数放入ThreadLocal中,同一线程中,不用传递参数,即可使用.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/10/27 下午3:10
 */
public class LoginUserHolder {
    private LoginUserHolder() {
    }

    private static ThreadLocal<Long> paramsHolder = new ThreadLocal<Long>();

    public static void set(Long loginUserId) {
        paramsHolder.set(loginUserId);
    }

    public static Long get() {
        return paramsHolder.get();
    }
}
