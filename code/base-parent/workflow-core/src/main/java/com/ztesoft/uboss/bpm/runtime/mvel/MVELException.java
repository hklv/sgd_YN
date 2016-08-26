package com.ztesoft.uboss.bpm.runtime.mvel;

import org.activiti.engine.ActivitiException;

/**
 * MVEL表达式运行时异常
 * 
 * @author chen.gang71
 * 
 */
public class MVELException extends ActivitiException {
    private static final long serialVersionUID = 6764986058761719400L;

    public MVELException(String message) {
        super(message);
    }

    public MVELException(String message, Throwable cause) {
        super(message, cause);
    }
}
