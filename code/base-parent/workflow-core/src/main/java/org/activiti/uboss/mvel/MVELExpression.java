package org.activiti.uboss.mvel;

import org.activiti.engine.impl.el.Expression;

public abstract class MVELExpression implements Expression {
    protected String expression;
    
    public MVELExpression(String expression){
        this.expression = expression;
    }
    
    public String getExpressionText(){
        return this.expression;
    }
}
