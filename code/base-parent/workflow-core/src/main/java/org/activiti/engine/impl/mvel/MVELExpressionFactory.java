package org.activiti.engine.impl.mvel;

import java.lang.reflect.Constructor;

import org.activiti.engine.ActivitiClassLoadingException;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.util.ReflectUtil;

public class MVELExpressionFactory {
	public static final String DEFAULT_CONDITION_IMPL = "com.ztesoft.uboss.bpm.runtime.mvel.MVELConditionImpl";
	public static final String DEFAULT_EXPRESSION_IMPL = "com.ztesoft.uboss.bpm.runtime.mvel.MVELExpressionImpl";

	public MVELExpressionFactory() {

	}

	public MVELCondition createCondition(String script) {
		try {
		    Class implClass = ReflectUtil.loadClass(DEFAULT_CONDITION_IMPL);
			return createCondition(script, implClass);
		} catch (ActivitiClassLoadingException ex) {
			throw new ActivitiException("Cannot load class " + DEFAULT_CONDITION_IMPL, ex);
		}
	}

	public MVELCondition createCondition(String script, Class<? extends MVELCondition> implClass) {
		if (script == null || script.length() == 0) {
			throw new IllegalArgumentException("Script cannot be empty!");
		}

		MVELCondition expr = null;
		try {
			expr = (MVELCondition) implClass.newInstance();
		} catch (Throwable t) {
			throw new ActivitiException("Cannot instantiate expression for class " + implClass.getName(), t);
		}
		expr.setScript(script);

		return expr;

	}
	
	public MVELExpression createExpression(String expression){
        if (expression == null || expression.length() == 0) {
            throw new IllegalArgumentException("Expression cannot be empty!");
        }
        try {
            Class implClass = ReflectUtil.loadClass(DEFAULT_EXPRESSION_IMPL);
            Constructor constructor = implClass.getConstructor(String.class);
            return (MVELExpression)constructor.newInstance(expression);
        } catch (ActivitiClassLoadingException ex) {
            throw new ActivitiException("Cannot load class " + DEFAULT_EXPRESSION_IMPL, ex);
        } catch (Throwable t){
            throw new ActivitiException("Cannot instantiate expression for class " + DEFAULT_EXPRESSION_IMPL, t);
        } 
	}
}
