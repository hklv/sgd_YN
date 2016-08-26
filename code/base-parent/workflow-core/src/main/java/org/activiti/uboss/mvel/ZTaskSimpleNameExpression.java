package org.activiti.uboss.mvel;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.VariableScope;

public class ZTaskSimpleNameExpression implements Expression {
	
	private final String name;
	
	public ZTaskSimpleNameExpression(String name){
		this.name = name;
	}

	public Object getValue(VariableScope variableScope) {
		return name;
	}

	public void setValue(Object value, VariableScope variableScope) {
	}
	
	public String getExpressionText() {
		return name;
	}

}
