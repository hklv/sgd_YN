package com.ztesoft.sgd.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注字段<br>
 *
 * @author sunhao
 * @version 1.0 2015年5月20日 下午4:45:29
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * 标注是否是blob字段
     *
     * @return
     */
    boolean isBlob() default false;
}
