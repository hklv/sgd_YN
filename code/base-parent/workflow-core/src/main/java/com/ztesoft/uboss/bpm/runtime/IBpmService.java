package com.ztesoft.uboss.bpm.runtime;

import com.ztesoft.uboss.bpm.runtime.beans.ActivityInfo;
import com.ztesoft.uboss.bpm.runtime.beans.ProcessBackInfo;
import com.ztesoft.uboss.bpm.runtime.beans.TaskListInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;
import java.util.Map;


/**
 * 流程对外接口一览
 *
 * @author LiybC
 */
public interface IBpmService {

    // --process
    String startProcess(String processDefinitionId,
                        Map<String, Object> variables) throws BaseAppException;

    List<DynamicDict> getNextActivityTask(String processIntanceId)
            throws BaseAppException;

    void terminateProcess(String taskHolderId, String terminateUser, String reason)
            throws BaseAppException;

    // --task

    /**
     * 任务完成提交,需要传入当前处理人,下一个环节的执行人可选（如果设置Pre_assign）
     *
     * @param taskId
     * @param variables
     * @throws BaseAppException
     */
    String completeTask(String taskId, Map<String, Object> variables)
            throws BaseAppException;

    /**
     * 跳转 需要传入当前处理人,跳转目标环节的执行人可选（如果设置Pre_assign）
     *
     * @param taskId
     * @param nextActivityId
     * @param variables
     * @throws BaseAppException
     */
    String jumpTask(String taskId, String nextActivityId,
                    Map<String, Object> variables) throws BaseAppException;

    /**
     * 任务回退,需要传入当前处理人
     *
     * @param taskId
     * @param variables
     * @throws BaseAppException
     */
    void backTask(String taskId, Map<String, Object> variables)
            throws BaseAppException;

    boolean reExecuteTask(String executionId) throws BaseAppException;

    // --Query---

    /**
     * 查询环节实例
     */
    List<Map<String, Object>> queryActivityInstance(String processInstanceId) throws BaseAppException;

    List<Map<String, Object>> queryHisActivityInstance(String processInstanceId) throws BaseAppException;

    /**
     * 查询环节模板定义
     *
     * @param processDefinitionId
     * @throws BaseAppException
     */
    ActivityInfo[] queryActivities(String processDefinitionId)
            throws BaseAppException;

    /**
     * 根据流程定义查询流程实例
     *
     * @param processDefinitionId
     * @return
     * @throws BaseAppException
     */
    List<Map<String, Object>> queryProcessIntances(
            String processDefinitionId, String isQueryFinished)
            throws BaseAppException;

    List<TaskListInfo> qryProcessTrack(String processInstanceId)
            throws BaseAppException;

    /**
     * 任务回退
     * 返回当前任务
     *
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> backTask(ProcessBackInfo processBackInfo) throws BaseAppException;

    /**
     * 流程回撤
     * 返回当前任务
     *
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> backProcess(ProcessBackInfo processBackInfo) throws BaseAppException;
}
