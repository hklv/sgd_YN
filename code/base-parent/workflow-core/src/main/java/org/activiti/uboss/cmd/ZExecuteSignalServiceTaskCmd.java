package org.activiti.uboss.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.history.handler.ActivityInstanceEndHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.task.ITaskListEntity;

public class ZExecuteSignalServiceTaskCmd implements Serializable, Command<Boolean> {
    private static final long serialVersionUID = 1L;
    
    protected final String executionId;
    protected final String taskListId;
    protected final Long userId;
    
    String TASK_ENTITY_STR="com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity";
    
    public ZExecuteSignalServiceTaskCmd(String executionId, String taskListId, Long userId){
        this.executionId = executionId;
        this.taskListId = taskListId;
        this.userId = userId;
    }

    public Boolean execute(CommandContext commandContext){
        ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(executionId);

        ExecutionEntity target = execution;
        if(target == null){
            throw new ActivitiException("Cannot find execution, executionId=" + executionId);
        }
       
        try
        {
        	target.signal("leaveByWaitSignal", null);
        	
	        ITaskListEntity te = (ITaskListEntity) Class.forName(TASK_ENTITY_STR).newInstance();
	        
			te.compSignalSysTask(taskListId, userId);
        }
        catch(Exception e)
        {
        	return Boolean.FALSE;
        }
        
        //返回执行是否成功
        HistoricActivityInstanceEntity activityInstance = 
            ActivityInstanceEndHandler.findActivityInstance(target);
        if(activityInstance != null && ZExtendsionUtil.SERVICE_TASK_STATE_EXCEPTION.equals(activityInstance.getAssignee())){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }
}
