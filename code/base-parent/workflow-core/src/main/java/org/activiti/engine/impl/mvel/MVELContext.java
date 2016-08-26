package org.activiti.engine.impl.mvel;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;

public class MVELContext{
    private static final ThreadLocal<MVELContext> localContext = new ThreadLocal<MVELContext>();
    private DelegateExecution execution = null;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    
    private MVELContext(){
        
    }
    
    public static MVELContext getContext(){
        MVELContext ctx = localContext.get();
        if(ctx == null){
            ctx = new MVELContext();
            localContext.set(ctx);
        }
        return ctx;
    }
    
    public void begin(DelegateExecution execution, Map<String, Object> initAttrs){
        this.execution = execution;
        if(initAttrs != null){
            this.attributes.putAll(initAttrs);
        }
    }
    
    public void end(){
        this.execution = null;
        this.attributes.clear();
    }
    
    public DelegateExecution getDelegateExecution(){
        return this.execution;
    }
    
    public void setAttribute(String key, Object value){
        this.attributes.put(key, value);
    }
    
    public Object getAttribute(String key){
        return this.attributes.get(key);
    }
}
