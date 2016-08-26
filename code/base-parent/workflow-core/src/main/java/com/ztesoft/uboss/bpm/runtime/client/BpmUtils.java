package com.ztesoft.uboss.bpm.runtime.client;

import com.ztesoft.uboss.bpm.runtime.beans.*;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.uboss.ext.ParamBinding;

import java.util.List;

public class BpmUtils {


    public static ActivityType getActivityType(ActivityImpl activity) {
        ActivityType activityType;

//		Object activityInfo = ZExtendsionUtil.getActivityInfo(activity);
//		if (activityInfo == null) {
//			ActivityBehavior activityBehavior = activity.getActivityBehavior();
//			if(activityBehavior instanceof MultiInstanceActivityBehavior){
//			    //多实例
//			    activityBehavior = ((MultiInstanceActivityBehavior)activityBehavior).getInnerActivityBehavior();
//			}
//			if (activityBehavior instanceof CallActivityBehavior) {
//				activityType = ActivityType.CallActivity;
//			} else if (activityBehavior instanceof SubProcessActivityBehavior) {
//				activityType = ActivityType.SubProcess;
//			} else {
//				activityType = ActivityType.UnKnown;
//			}
//		} else if (activityInfo instanceof ZTaskDefinition) {
//			activityType = ActivityType.UserTask;
//		} else if (activityInfo instanceof ZServiceDefinition) {
//			activityType = ActivityType.ServiceTask;
//		} else if (activityInfo instanceof ZRuleDefinition) {
//			activityType = ActivityType.BusinessRuleTask;
//		} else {
//			activityType = ActivityType.UnKnown;
//		}
//
//		return activityType;

        return ActivityType.UserTask;
    }


    public static ExecutionInfo buildExecutionInfo(Execution execution, ActivityImpl activity) {
        ActivityType activityType = getActivityType(activity);
        switch (activityType) {
            case UserTask:
                return new UserTaskExecution(execution, activity);
            case ServiceTask:
                return new ServiceTaskExecution(execution, activity);
            case BusinessRuleTask:
                return new BusinessRuleTaskExecution(execution, activity);
            case CallActivity:
                return new CallActivityExecution(execution, activity);
            case SubProcess:
                return new SubProcessExecution(execution, activity);
        }
        throw new ActivitiException("unknown ActivityType! activity = " + activity);
    }

    public static ParamInfo[] wrap(List<ParamBinding> blist) {
        if (blist == null) {
            return new ParamInfo[]{};
        }
        ParamInfo[] ps = new ParamInfo[blist.size()];
        for (int i = 0; i < blist.size(); i++) {
            ps[i] = new ParamInfo(blist.get(i));
        }
        return ps;
    }

}
