package org.activiti.uboss.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.history.handler.ActivityInstanceEndHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;
import org.activiti.uboss.ZExtendsionUtil;

/**
 * 只能执行处于等待状态的ServiceTask。在xml定义中必须waitForSignal="true"
 * @author chen.gang71
 *
 */
public class ZExecuteServiceTaskCmd implements Serializable, Command<Boolean> {
    private static final long serialVersionUID = 1L;
    
    protected final String processInstanceId;
    protected final String executionId;
    protected final String taskId;
    
    public ZExecuteServiceTaskCmd(String processInstanceId, String executionId, String taskId){
        this.processInstanceId = processInstanceId;
        this.executionId = executionId;
        this.taskId = taskId;
    }

    public Boolean execute(CommandContext commandContext) {
        ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(executionId);

        ExecutionEntity target = execution;
        if(target == null){
            throw new ActivitiException("Cannot find execution, processInstanceId=" 
                    + processInstanceId + ",executionId=" + executionId);
        }
        String activityType = (String)target.getActivity().getProperty("type");
        if(!"serviceTask".equals(activityType)){
            throw new ActivitiException("Current active activity is not a service task! Type is " + activityType);
        }
        if(taskId != null && !taskId.equals(target.getActivity().getId())){
            throw new ActivitiException("Cannot find active service task for id " + taskId);
        }
        target.performOperation(AtomicOperation.ACTIVITY_EXECUTE);
       //返回执行是否成功
        HistoricActivityInstanceEntity activityInstance = 
            ActivityInstanceEndHandler.findActivityInstance(target);
        if(ZExtendsionUtil.SERVICE_TASK_STATE_EXCEPTION.equals(activityInstance.getAssignee())){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
}
