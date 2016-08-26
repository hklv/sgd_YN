package com.ztesoft.uboss.bpm.runtime.service;

import com.ztesoft.uboss.bpm.runtime.beans.*;
import com.ztesoft.uboss.bpm.runtime.call.CommitCall;
import com.ztesoft.uboss.bpm.runtime.call.StartCall;
import com.ztesoft.uboss.bpm.runtime.call.TerminateCall;
import com.ztesoft.uboss.bpm.runtime.call.UserTaskBackCall;
import com.ztesoft.uboss.bpm.runtime.client.*;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmScope;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.ZRuntimeServiceImpl;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 流程运行控制
 *
 * @author LiybC
 */
public class BpmRuntimeServiceImpl implements BpmRuntimeService {
    private ZSmartLogger logger = ZSmartLogger
            .getLogger(BpmRuntimeServiceImpl.class);
    private ServiceProvider serviceProvider;

    public BpmRuntimeServiceImpl(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String startProcess(String processDefinitionKey)
            throws ActivitiException {
        return startProcess(processDefinitionKey, null);
    }

    public String startProcess(String processDefinitionKey,
                               Map<String, Object> variables) throws ActivitiException {
        BpmParameter parameter = new BpmParameter();
        parameter.setProcessDefinitionKey(processDefinitionKey);
        parameter.setVariables(variables);
        return __startProcess__(parameter);
    }

    public String completeTaskByTaskId(String taskId,
                                       Map<String, Object> variables) throws ActivitiException {
        BpmParameter parameter = new BpmParameter();
        parameter.setTaskId(taskId);
        parameter.setVariables(variables);
        return __commitUserTask__(parameter);
    }

    public String completeTaskByExecutionId(String executionId,
                                            Map<String, Object> variables) {
        BpmParameter parameter = new BpmParameter();
        parameter.setExecutionId(executionId);
        parameter.setVariables(variables);
        return __commitUserTask__(parameter);
    }

    public List<String> backUserTask(String executionId) {
        return backUserTask(executionId, null);
    }

    public List<String> backUserTask(String executionId, Integer position) {
        return __backUserTask__(executionId, position,
                ExecuteMode.EXECUTE_AND_WAIT, 0);
    }

    /**
     * 终止流程
     */
    public void terminateProcess(String processInstanceId, String reason) {
        if (logger.isInfoEnabled()) {
            logger.info("Terminate process, instance id={}, reason={}",
                    processInstanceId, reason);
        }
        TerminateCall terminate = new TerminateCall(this.serviceProvider);
        terminate.setProcessInstanceId(processInstanceId);
        terminate.setReason(reason);

        try {
            terminate.call();

        } catch (ActivitiException e) {
            logger.error("Fail to terminate process instance, id="
                    + processInstanceId, e);
            throw e;
        }
    }

    public void signal(String executionId, Map<String, Object> variables) {
        this.serviceProvider.getRuntimeService().signal(executionId, variables);
    }


    public boolean executeServiceTask(String executionId) {
        return this.executeServiceTask(executionId, null);
    }

    public boolean executeServiceTask(String executionId, String activityId) {
        List<String> currentActivityIds = __getActiveActivityIds__(executionId);
        if (currentActivityIds == null || currentActivityIds.size() == 0) {
            throw new ActivitiException(
                    "Cannot find any active activities in execution");
        }

        ZRuntimeServiceImpl zRuntimeService = (ZRuntimeServiceImpl) serviceProvider
                .getRuntimeService();
        return zRuntimeService
                .executeServiceTask(null, executionId, activityId);
    }


    public ExecutionInfo getExecution(String executionId) {
        ExecutionEntity execution = (ExecutionEntity) ZExtendsionUtil
                .assertSingleExecution(serviceProvider.getRuntimeService(),
                        null, executionId);
        String activityId = execution.getActivityId();
        ActivityImpl activity = ZExtendsionUtil.getActivity(
                serviceProvider.getRepositoryService(),
                execution.getProcessDefinitionId(), activityId);
        return BpmUtils.buildExecutionInfo(execution, activity);
    }


    private List<ExecutionInfo> __getExecutionsByInstanceId__(
            String processInstanceId) {
        ExecutionQuery query = serviceProvider.getRuntimeService()
                .createExecutionQuery();
        List<Execution> list = query.processInstanceId(processInstanceId)
                .list();
        List<ExecutionInfo> result = new ArrayList<ExecutionInfo>();
        for (Execution execution : list) {
            ExecutionEntity entity = (ExecutionEntity) execution;
            // TODO activityId == null parentId == null isActive == false.
            // Exclude process instance
            if (entity.getActivityId() == null
                    && entity.getProcessInstanceId().equals(entity.getId())) {
                continue;
            }
            ActivityImpl activity = ZExtendsionUtil.getActivity(
                    serviceProvider.getRepositoryService(),
                    entity.getProcessDefinitionId(), entity.getActivityId());
            result.add(BpmUtils.buildExecutionInfo(execution, activity));
        }
        return result;
    }


    private List<String> __getActiveActivityIds__(String executionId) {
        return serviceProvider.getRuntimeService().getActiveActivityIds(
                executionId);
    }

    public UserTaskInfo __getUserTasks__(String executionId) {
        UserTaskInfo userTaskInfo = new UserTaskInfo();
        try {
            ExecutionEntity execution = (ExecutionEntity) ZExtendsionUtil
                    .assertSingleExecution(serviceProvider.getRuntimeService(),
                            null, executionId);
            String activityId = execution.getActivityId();
            ActivityImpl activity = ZExtendsionUtil.getActivity(
                    serviceProvider.getRepositoryService(),
                    execution.getProcessDefinitionId(), activityId);
            // find current
            UserTaskExecution current = __findCurrentUserTask__(execution, activity);
            if (current != null) {
                userTaskInfo.setCurrent(current);
                // previous
                userTaskInfo.setPrev(__findPreviousUserTask__(execution, activity));
                // next
                userTaskInfo.setNext(__findNextActivity__(execution, activity));
            }
        } catch (Exception ex) {
            logger.error("Get UserTasks error", ex);
        }
        return userTaskInfo;
    }

    private UserTaskExecution __findCurrentUserTask__(ExecutionEntity execution,
                                                      ActivityImpl activity) {
        if (BpmUtils.getActivityType(activity) == ActivityType.UserTask) {
            return (UserTaskExecution) BpmUtils.buildExecutionInfo(execution,
                    activity);
        }
        return null;
    }

    private List<ActivityInfo> __findPreviousUserTask__(ExecutionEntity execution,
                                                        ActivityImpl start) {
        List<ActivityInfo> taskList = new ArrayList<ActivityInfo>();

        HistoricActivityInstanceQuery query = serviceProvider
                .getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(execution.getProcessInstanceId()).finished();
        List<HistoricActivityInstance> trace = query
                .orderByHistoricActivityInstanceId().asc().list();

        if (trace.size() == 0) {
            return taskList;
        }

        int endpos = trace.size() - 1;
        EXIT:
        while (endpos >= 0) {
            List<PvmTransition> incomings = start.getIncomingTransitions();
            if (incomings != null) {
                int index = -1;
                HistoricActivityInstance tracePoint = null;
                for (PvmTransition incoming : incomings) {
                    ActivityImpl source = (ActivityImpl) incoming.getSource();
                    if (source.getActivityBehavior() instanceof NoneStartEventActivityBehavior) {
                        ActivityImpl parent = source.getParentActivity();
                        if (parent != null && parent.isScope()) {
                            // 返回到上级流程
                            source = parent;
                        } else {
                            // 已走到StartEvent
                            break EXIT;
                        }
                    } else if (source.isScope()) {
                        List<ActivityImpl> subActivityList = source
                                .getActivities();
                        for (ActivityImpl activity : subActivityList) {
                            // 子流程的endEvent
                            if ("endEvent".equals(ZExtendsionUtil
                                    .getActivityType(activity))) {
                                start = activity;
                                continue EXIT;
                            }
                        }
                    }
                    // 找离当前节点最近的
                    for (int i = endpos; i >= 0; i--) {
                        if (trace.get(i).getActivityId().equals(source.getId())) {
                            if (i > index) {
                                index = i;
                                tracePoint = trace.get(i);
                                start = source;
                                break;
                            }
                        }
                    }
                }

                if (BpmUtils.getActivityType(start) == ActivityType.UserTask) {
                    taskList.add(ActivityInfo.build(tracePoint));
                }

                endpos = index;
            } else {
                logger.debug("Cannot find incomings, start=" + start.getId());
                break;
            }
        }
        Collections.reverse(taskList);
        return taskList;
    }

    private List<ActivityInfo> __findNextActivity__(ExecutionEntity execution,
                                                    ActivityImpl start) {
        List<ActivityImpl> taskList = new ArrayList<ActivityImpl>();
        List<ActivityImpl> tempList = new ArrayList<ActivityImpl>();

        Stack<PvmActivity> route = new Stack<PvmActivity>();
        route.push(start);
        GOTO_START:
        while (true) {
            PvmActivity currentNode = route.peek();
            List<PvmTransition> nextOutgoings = currentNode
                    .getOutgoingTransitions();
            if (nextOutgoings != null && nextOutgoings.size() > 0) {
                int index = route.search(nextOutgoings.get(0).getDestination());
                if (index != -1) {
                    // 循环了，退出
                    break;
                }
                route.push(nextOutgoings.get(0).getDestination());
                continue;
            } else {
                // 已经到EndEvent
                PvmScope parent = currentNode.getParent();
                if (parent != null && parent instanceof PvmActivity) {
                    List<PvmTransition> scopeOutgoings = ((PvmActivity) parent)
                            .getOutgoingTransitions();
                    if (scopeOutgoings != null && scopeOutgoings.size() > 0) {
                        route.push(scopeOutgoings.get(0).getDestination());
                        continue;
                    }
                }
                tempList.clear();
                for (PvmActivity node : route) {
                    if (node instanceof ActivityImpl) {
                        ActivityImpl activity = (ActivityImpl) node;
                        if (activity.isScope()) {
                            List<ActivityImpl> subActivityList = activity
                                    .getActivities();
                            for (ActivityImpl subActivity : subActivityList) {
                                tempList.add(subActivity);
                            }
                        } else {
                            tempList.add(activity);
                        }
                    }
                }
                if (tempList.size() > taskList.size()) {
                    taskList = new ArrayList<ActivityImpl>(tempList);
                }

                // 退回上一步
                while (route.size() > 1) {
                    route.pop();
                    PvmActivity parentNode = route.peek();
                    nextOutgoings = parentNode.getOutgoingTransitions();
                    if (nextOutgoings != null && nextOutgoings.size() > 1) {
                        // 存在多个兄弟节点时，找到下一个
                        for (int i = 0; i < nextOutgoings.size(); i++) {
                            PvmActivity sibling = nextOutgoings.get(i)
                                    .getDestination();
                            if (sibling.getId().equals(currentNode.getId())
                                    && i + 1 < nextOutgoings.size()) {
                                route.push(nextOutgoings.get(i + 1)
                                        .getDestination());
                                continue GOTO_START;
                            }
                        }
                    }
                    currentNode = parentNode;
                }

                if (route.size() == 1) {
                    break;
                }
            }
        }
        // 去掉头部
        if (taskList.size() > 0) {
            taskList.remove(0);
        }

        List<ActivityInfo> result = new ArrayList<ActivityInfo>();
        for (ActivityImpl task : taskList) {
            ActivityInfo info = new ActivityInfo();
            info.setProcessInstanceId(execution.getProcessInstanceId());
            info.setActivityId(task.getId());
            info.setActivityName((String) task.getProperty("name"));
            info.setActivityType((String) task.getProperty("type"));
            info.setProcessDefinitionId(task.getProcessDefinition().getId());
            result.add(info);
        }
        return result;
    }

    private String __commitUserTask__(BpmParameter parameter) {
        CommitCall commit = new CommitCall(this.serviceProvider);
        commit.setExecutionId(parameter.getExecutionId());
        commit.setVariables(parameter.getVariables());
        commit.setTaskId(parameter.getTaskId());

        try {
            String taskId = null;
            ExecuteMode mode = parameter.getExecuteMode();
            long timeout = parameter.getTimeoutMillis();
            switch (mode) {
                case EXECUTE_AND_WAIT:
                    taskId = commit.doTask();
                    break;
                case EXECUTE_IMMEDIATELY:
                case EXECUTE_AND_QUERY:
                    FutureTask<String> future = new FutureTask<String>(commit);
                    // 加入线程池，异步处理
                    serviceProvider.asynExec(future);
                    if (timeout > 0L) {
                        try {
                            taskId = future.get(timeout, TimeUnit.MILLISECONDS);
                        } catch (TimeoutException ex) {
                            // ignore
                        } catch (InterruptedException e) {
                            throw new ActivitiException("Fail to commit user task",
                                    e);
                        } catch (ExecutionException e) {
                            throw new ActivitiException("Fail to commit user task",
                                    e);
                        }
                    }
                    break;
                default:
            }


            return taskId;
        } catch (ActivitiException e) {
            logger.error("Fail to complete current task", e);
            throw e;
        }
    }

    private String __startProcess__(BpmParameter parameter)
            throws ActivitiException {
        String processDefinitionKey = parameter.getProcessDefinitionKey();

        ExecuteMode executeMode = parameter.getExecuteMode();
        long queryTimeoutMillis = parameter.getTimeoutMillis();

        Map<String, Object> variables = parameter.getVariables();
        String businessKey = UUID.randomUUID().toString();

        if (variables == null) {
            variables = new HashMap<String, Object>();
        }

        StartCall start = new StartCall(this.serviceProvider);
        start.setProcessDefinitionKey(processDefinitionKey);
        start.setBusinessKey(businessKey);
        start.setVariables(variables);

        try {
            String processInstanceId = null;

            switch (executeMode) {
                case EXECUTE_AND_WAIT:
                    processInstanceId = start.doTask();
                    break;
                case EXECUTE_IMMEDIATELY:
                case EXECUTE_AND_QUERY:
                    FutureTask<String> future = new FutureTask<String>(start);
                    // 加入线程池，异步处理
                    serviceProvider.asynExec(future);
                    if (queryTimeoutMillis > 0L) {
                        try {
                            processInstanceId = future.get(queryTimeoutMillis,
                                    TimeUnit.MILLISECONDS);
                        } catch (TimeoutException e) {
                            // nothing to do
                        } catch (InterruptedException e) {
                            throw new ActivitiException(
                                    "Error occurred while starting process", e);
                        } catch (ExecutionException e) {
                            throw new ActivitiException(
                                    "Error occurred while starting process", e);
                        }
                    } else if (queryTimeoutMillis == 0L) {
                        ProcessInstanceQuery procQuery = serviceProvider
                                .getRuntimeService()
                                .createProcessInstanceQuery()
                                .processInstanceBusinessKey(businessKey,
                                        processDefinitionKey);
                        List<ProcessInstance> instList = procQuery.list();
                        if (instList != null && instList.size() > 0) {
                            processInstanceId = instList.get(0)
                                    .getProcessInstanceId();
                        }
                    }
                    break;
                default:
            }
            return processInstanceId;
        } catch (ActivitiException e) {
            logger.error("Fail to start process", e);
            throw e;
        }
    }

    private List<String> __backUserTask__(String executionId, Integer position,
                                          ExecuteMode executeMode, long queryTimeoutMillis) {
//		UserTaskInfo userTaskInfo = this.__getUserTasks__(executionId);
//		// 如果无法回退，返回当前的UserTask
//		if (!userTaskInfo.canStepBackward()) {
//			return userTaskInfo.getCurrent().getActivityId();
//		}
        UserTaskBackCall back = new UserTaskBackCall(this.serviceProvider);
        back.setExecutionId(executionId);
        back.setPosition(position);
        back.call();
        //return __singleActivityId__(executionId);

        List<Execution> executions = this.serviceProvider.getRuntimeService().createExecutionQuery().executionId(executionId).list();

        //退到开始节点了
        if (executions.isEmpty()) {

            return new ArrayList<String>();
        } else {

            String procInstId = this.serviceProvider.getRuntimeService()
                    .createExecutionQuery().executionId(executionId).list()
                    .get(0).getProcessInstanceId();

            return getCurrTasks(procInstId);
        }

    }

//	private String __singleActivityId__(String executionId) {
//		List<String> activityList = this.__getActiveActivityIds__(executionId);
//		if (activityList == null || activityList.size() == 0) {
//			return null;
//		}
//		return activityList.get(0);
//	}

    private List<String> getCurrTasks(String procInstId) {

        TaskService taskService = serviceProvider.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .processInstanceId(procInstId);

        List<Task> tasks = taskQuery.list();

        List<String> taskIds = new ArrayList<String>(tasks.size());

        for (int i = 0; i < taskIds.size(); i++) {

            taskIds.add(tasks.get(i).getId());
        }

        return taskIds;

    }

    public boolean executeExceptionServiceTask(String executionId, String exceptionTaskListId)
            throws ActivitiException {
        List<String> currentActivityIds = __getActiveActivityIds__(executionId);
        if (currentActivityIds == null || currentActivityIds.size() == 0) {
            throw new ActivitiException(
                    "Cannot find any active activities in execution");
        }

        ZRuntimeServiceImpl zRuntimeService = (ZRuntimeServiceImpl) serviceProvider
                .getRuntimeService();
        return zRuntimeService
                .executeExceptionServiceTask(null, executionId, exceptionTaskListId);
    }

    @Override
    public String executeSingalServiceTask(String processInstanceId, String executionId,
                                           String taskListId, Long userId) throws ActivitiException {

//		List<String> currentActivityIds = __getActiveActivityIds__(executionId);
//		if (currentActivityIds == null || currentActivityIds.size() == 0) {
//			throw new ActivitiException(
//					"Cannot find any active activities in execution");
//		}

        String ids = null;

        ZRuntimeServiceImpl zRuntimeService = (ZRuntimeServiceImpl) serviceProvider.getRuntimeService();

        boolean bool = zRuntimeService.executeSignalServiceTask(executionId, taskListId, userId);

        if (bool) {

            TaskService taskService = serviceProvider.getTaskService();
            TaskQuery taskQuery = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId);

            List<Task> tasks = taskQuery.list();

            if (tasks != null && tasks.size() > 0) {

                for (int i = 0; i < tasks.size(); i++) {
                    if (i == tasks.size() - 1) {
                        ids += tasks.get(i).getId();
                    } else {
                        ids += tasks.get(i).getId() + ",";
                    }
                }
            }
        }

        return ids;
    }


}
