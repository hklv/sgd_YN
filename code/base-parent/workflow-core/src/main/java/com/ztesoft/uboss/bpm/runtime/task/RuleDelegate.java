package com.ztesoft.uboss.bpm.runtime.task;

import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.uboss.ext.CallableDefinition;
import org.activiti.uboss.ext.ZRuleDefinition;

public class RuleDelegate extends ServiceCaller implements JavaDelegate {
    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());

    @Override
    public void setCallableDefinition(CallableDefinition callableDefinition) {
        if (callableDefinition instanceof ZRuleDefinition) {
            super.setCallableDefinition(callableDefinition);
        } else {
            throw new ActivitiException("callableDefinition must be a "
                    + ZRuleDefinition.class);
        }
    }

    public ZRuleDefinition getRuleDefinition() {
        return (ZRuleDefinition) super.getCallableDefinition();
    }

    public void execute(DelegateExecution execution) throws Exception {
        String taskTemplateId = this.getRuleDefinition().getTaskTemplateId();

        logger.debug("BEGIN, invoke rule \"" + taskTemplateId);

        if (taskTemplateId == null || taskTemplateId.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "The property \"taskTemplateId\" cannot be empty");
        }
        Object retVal = null;
        try {
            // 获取boInstance
            // MetadataNode boInstance = BoHelper.getMetaDataRoot(commonDataId);
            // // 调用引擎
            // List<BcMessage> retVal = BoHelper.invokeRule(ruleName,
            // (MetadataRoot) boInstance, commonDataId);

            // 将规则返回值设置到变量
            String resultVarName = this.getRuleDefinition()
                    .getResultVariableName();
            if (resultVarName != null) {
                execution.setVariable(resultVarName, retVal);
            }
        } catch (ActivitiException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Fail to execute rule,name=" + taskTemplateId, ex);
            throw ex;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("END, invoke rule \"" + taskTemplateId);
        }
    }
}
