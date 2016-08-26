package org.activiti.uboss.parser;

import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.bpmn.helper.ClassDelegate;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.behavior.ZServiceTaskJavaDelegateActivityBehavior;
import org.activiti.uboss.ext.ZServiceDefinition;

public class ZClassDelegate extends ClassDelegate {

	private String blockFlag;
 
    public ZClassDelegate(String className, List<FieldDeclaration> fieldDeclarations) {
        super(className, fieldDeclarations);
    }
    
    protected ActivityBehavior getActivityBehaviorInstance(ActivityExecution execution) {
        Object delegateInstance = instantiateDelegate(className,
                fieldDeclarations);

        if (delegateInstance instanceof ActivityBehavior) {
            return determineBehaviour((ActivityBehavior) delegateInstance,
                    execution);
        } else if (delegateInstance instanceof JavaDelegate) {
        	
        	String taskTemplateId = ((ZServiceDefinition)fieldDeclarations.get(0).getValue()).getTaskTemplateId();
        	
        	boolean waitForSignal = "Y".equals(blockFlag)?true:false;
        	
            return determineBehaviour(
                    new ZServiceTaskJavaDelegateActivityBehavior(taskTemplateId,waitForSignal,
                            (JavaDelegate) delegateInstance), execution);
        } else {
            throw new ActivitiException(delegateInstance.getClass().getName()
                    + " doesn't implement " + JavaDelegate.class.getName()
                    + " nor " + ActivityBehavior.class.getName());
        }
    }

	public String getBlockFlag() {
		return blockFlag;
	}

	public void setBlockFlag(String blockFlag) {
		this.blockFlag = blockFlag;
	}

}
