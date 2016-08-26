package com.ztesoft.uboss.bpm.runtime.call;

import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;
import java.util.Map;

/**
 * 流程提交当前任务
 *
 * @author chen.gang71
 */
public class CommitCall extends AbstractCallable<String> {
    private ZSmartLogger logger = ZSmartLogger.getLogger(CommitCall.class);
    private String executionId;
    private String taskId;
    private Map<String, Object> variables;

    public CommitCall(ServiceProvider sp) {
        super(sp);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    /**
     * @return taskId
     */
    public String doTask() {

        if (taskId != null) {
            Task task = sp.getTaskService().createTaskQuery().taskId(taskId)
                    .singleResult();
            if (task != null) {
                sp.getTaskService().complete(task.getId(), variables);
                TaskService taskService = sp.getTaskService();
                TaskQuery taskQuery = taskService.createTaskQuery()
                        .processInstanceId(task.getProcessInstanceId());

                List<Task> tasks = taskQuery.list();
                if (tasks != null && tasks.size() > 0) {
                    String ids = "";
                    for (int i = 0; i < tasks.size(); i++) {
                        if (i == tasks.size() - 1) {
                            ids += tasks.get(i).getId();
                        } else {
                            ids += tasks.get(i).getId() + ",";
                        }
                    }
                    return ids;
                }
            }
        } else {
            ExecutionQuery executionQuery = sp.getRuntimeService()
                    .createExecutionQuery().executionId(executionId);

            ExecutionEntity execution = (ExecutionEntity) executionQuery
                    .singleResult();
            if (execution == null) {
                throw new ActivitiException("Cannot find execution by id \""
                        + executionId + "\"");
            }
            // 获取当前活动节点ID
            List<String> activityIds = sp.getRuntimeService()
                    .getActiveActivityIds(executionId);
            if (activityIds == null || activityIds.size() == 0) {
                throw new ActivitiException(
                        "Illegal activity state! Cannot find active activity");
            }

            if (activityIds.size() > 1) {
                throw new ActivitiException(
                        "Illegal activity state! Too many active activities found in execution "
                                + executionId);
            }

            return this.completeUserTask(execution);
        }
        return null;
    }

    private String completeUserTask(ExecutionEntity execution) {
        TaskService taskService = sp.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery().executionId(
                execution.getId());

        List<Task> tasks = taskQuery.list();

        if (tasks == null || tasks.size() == 0) {
            throw new ActivitiException(
                    "No user task found in execution, execution id = "
                            + execution.getId());
        } else if (tasks.size() == 1) {
            Task task = tasks.get(0);
            if (logger.isDebugEnabled()) {
                logger.debug("Prepare to complete task,id=" + task.getId()
                        + ",name=" + task.getName());
            }

            taskService.complete(task.getId(), variables);
            // 完成UserTask，移至下面的步骤
            if (logger.isDebugEnabled()) {
                logger.debug("Task is completed,id=" + task.getId());
            }
            return task.getId();
        } else {
            throw new ActivitiException("Too many tasks found in execution.");
        }
    }

}
