package com.ztesoft.sgd.base.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注类
 *
 * @author sunhao
 * @version 1.0 2015年5月20日 下午7:14:50
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 数据库表名
     *
     * @return
     */
    String name();

    /**
     * 序列名
     *
     * @return
     */
    String sequence() default "";
}
