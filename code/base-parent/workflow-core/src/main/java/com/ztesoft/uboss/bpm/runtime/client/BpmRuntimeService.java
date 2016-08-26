package com.ztesoft.uboss.bpm.runtime.client;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;

/**
 * 工作流引擎Runtime服务，对流程的操作
 * 
 * @author chen.gang71
 * 
 */
public interface BpmRuntimeService {

	
    /**
     * 启动流程
     * @param processDefinitionKey
     * @return processInstanceId
     * @throws ActivitiException
     */
    String startProcess(String processDefinitionKey) throws ActivitiException;
    
    String startProcess(String processDefinitionKey, Map<String, Object> variables) throws ActivitiException;
    
    String completeTaskByTaskId(String taskId, Map<String, Object> variables) throws ActivitiException;
    /**
     * 提交当前UserTask
     * @param executionId 执行路径id
     * @return 提交的taskId
     */
    String completeTaskByExecutionId(String executionId, Map<String, Object> variables) throws ActivitiException;


    /**
     * 使流程回退到上一步UserTask，如果无法回退，则返回当前activityId
     * 
     * @param executionId 执行路径id
     * @return String 上一步的activityId
     * @throws ActivitiException
     */
    List<String> backUserTask(String executionId) throws ActivitiException;
    
    List<String> backUserTask(String executionId, Integer position) throws ActivitiException;

 
    /**
     * 终止流程
     * @param processInstanceId
     * @param reason
     * @throws ActivitiException
     */
    void terminateProcess(String processInstanceId, String reason) throws ActivitiException;
 
 
    void signal(String executionId, Map<String, Object> variables) throws ActivitiException;
    
    /**
     * 执行ServiceTask服务，service task必须是wait状态
     * @param executionId 执行路径id
     * @throws ActivitiException
     */
    boolean executeServiceTask(String executionId) throws ActivitiException;
    
    boolean executeExceptionServiceTask(String executionId, String exceptionTaskListId) throws ActivitiException;
    
    String executeSingalServiceTask(String processInstanceId, String executionId,
                                    String taskListId, Long userId) throws ActivitiException;

    boolean executeServiceTask(String executionId, String activityId) throws ActivitiException;
}