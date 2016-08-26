package com.ztesoft.uboss.bpm.runtime.mvel;

import com.ztesoft.uboss.bpm.runtime.client.BpmConstants;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.mvel.MVELCondition;
import org.activiti.engine.impl.mvel.MVELContext;

import java.io.Serializable;

public class MVELConditionImpl implements MVELCondition {
    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());

    private String script = "";
    private Serializable compiledScript = null;

    public MVELConditionImpl() {

    }

    public void addImport(String name, Object val) {
        throw new UnsupportedOperationException();
    }

    public void setScript(String script) {
        if (script != null) {
            this.script = script;
            this.compiledScript = null;
        } else {
            throw new NullPointerException("script");
        }
    }

    public String getScript() {
        return this.script;
    }

    public boolean evaluate(DelegateExecution execution) {
        //判断是否debug模式
        Object debugMode = execution.getVariable(BpmConstants.VAR_NAME_DEBUG_MODE);
        boolean isDebugMode = debugMode != null ? (Boolean) debugMode : false;
        if (isDebugMode) {
            return true;
        }

        if (script == null || script.length() == 0) {
            return false;
        }
        Object retValue = null;
        try {
            MVELContext.getContext().begin(execution, null);
            if (compiledScript == null) {
                //编译
//                compiledScript = ElUtils.compileExpression(script);
            }
            //取commonDataId
            String commonDataId = (String) execution.getVariable(BpmConstants.VAR_NAME_COMMON_DATA_ID);
            AssertUtil.isNotEmpty(commonDataId, "Cannot find variable '"
                    + BpmConstants.VAR_NAME_COMMON_DATA_ID + "' in process context");
            //获取boInstance
//            MetadataRoot boInstance = BoHelper.getMetaDataRoot(commonDataId);
//            MVELContext.getContext().setAttribute(BpmConstants.VAR_NAME_BO_INSTANCE, boInstance);

            //执行表达式  eg.ElFunctions.equals(invokeBoMethod("641", ["661" : "/2107", "662" : ""]), "Y")
//            retValue = ElUtils.executeExpression(compiledScript, boInstance);
            if (logger.isDebugEnabled()) {
                logger.debug("Script:" + script + ",result: " + retValue);
            }
            if (retValue instanceof Boolean) {
                return ((Boolean) retValue).booleanValue();
            } else {
                throw new IllegalStateException(
                        "MVEL script error should return a boolean value. script : " + script);
            }
        } catch (Throwable t) {
            logger.error("MVEL script error,process instance id="
                    + execution.getProcessInstanceId() + ", script=" + script + ",result=" + retValue, t);
            throw new ActivitiException("MVEL script error.", t);
        } finally {
            MVELContext.getContext().end();
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof MVELCondition) {
            return this.script.equals(((MVELCondition) obj).getScript());
        }
        return false;
    }

    public int hashCode() {
        return script != null ? script.hashCode() : 0;
    }
}
