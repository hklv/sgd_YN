package org.activiti.uboss.mvel;

import java.lang.reflect.Method;

import org.activiti.engine.impl.Condition;

/**
 * MVEL表达式
 * @author chen.gang71
 *
 */
public interface MVELCondition extends Condition{
    /**
     * 设置脚本
     * @param script
     */
    public void setScript(String script);
    
    public String getScript();
    
    public void addImport(String name, Object val);
}
