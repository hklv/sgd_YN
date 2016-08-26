package com.ztesoft.zsmartcity.sgd.mvc.exception;

import lodsve.mvc.exception.ApplicationException;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/7/11 下午3:08
 */
public class MvcException extends ApplicationException {
    public MvcException(String content) {
        super(content);
    }

    public MvcException(Integer code, String content) {
        super(code, content);
    }

    public MvcException(Integer code, String content, String... args) {
        super(code, content, args);
    }
}
