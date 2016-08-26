package com.ztesoft.uboss.bpm.runtime;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.runtime.beans.*;
import com.ztesoft.uboss.bpm.runtime.client.BpmConstants;
import com.ztesoft.uboss.bpm.runtime.client.BpmRuntimeService;
import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;
import com.ztesoft.uboss.bpm.runtime.constant.TaskHolderConstant;
import com.ztesoft.uboss.bpm.runtime.constant.TaskListConstant;
import com.ztesoft.uboss.bpm.runtime.dao.IBpmRunTimeDAO;
import com.ztesoft.uboss.bpm.runtime.dao.mysqlimpl.BpmRunTimeDAOMySQL;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskHolderEntity;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.ProcessTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmTaskTemplateDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessTemplateVersion;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.IWorkItemManagerDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.WorkItemManagerDAOMySQL;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.SessionContext;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.GatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.uboss.ZExtendsionUtil;
import org.activiti.uboss.ZRuntimeServiceImpl;
import org.activiti.uboss.behavior.*;
import org.activiti.uboss.cmd.ZUserTaskJumpSignCommand;
import org.activiti.uboss.model.HolderChangeInfo;
import org.activiti.uboss.parser.ZClassDelegate;
import org.activiti.uboss.task.ITaskHolderEntity;

import java.util.*;

/**
 * BPM 对外接口
 *
 * @author LiybC
 */
public class BpmServiceImpl implements IBpmService {

    private static BpmServiceImpl impl = new BpmServiceImpl();
    private ServiceProvider serviceProvider = ProcessServiceManager
            .getManager().getServiceProvider();
    private ZSmartLogger logger = ZSmartLogger.getLogger(BpmServiceImpl.class);

    private BpmServiceImpl() {
    }

    public static BpmServiceImpl getInstance() {
        return impl;
    }

    public String startProcess(String processDefinitionKey,
                               Map<String, Object> variables) throws BaseAppException {

        AssertUtil.isNotNull(processDefinitionKey,
                "processDefinitionKey cannot be null");
        String processIntanceId = null;
        BpmRuntimeService bpmRuntimeService = ProcessServiceManager
                .getManager().getRuntimeService();
        try {
            processIntanceId = bpmRuntimeService.startProcess(
                    processDefinitionKey, variables);
        } catch (ActivitiException ex) {
            ExceptionHandler.publish(ex.getMessage());
        }
        return processIntanceId;
    }

    public List<DynamicDict> getNextActivityTask(String processIntanceId)
            throws BaseAppException {
        TaskService taskService = ProcessServiceManager.getManager()
                .getServiceProvider().getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(
                processIntanceId);
        String nextTaskId = "";
        List<Task> tasks = taskQuery.list();
        if (tasks != null && tasks.size() > 0) {

            for (int i = 0; i < tasks.size(); i++) {
                if (i == tasks.size() - 1) {
                    nextTaskId += tasks.get(i).getId();
                } else {
                    nextTaskId += tasks.get(i).getId() + ",";
                }
            }
        }
        if (nextTaskId != null && nextTaskId.trim().length() > 0) {
            List<DynamicDict> list = new ArrayList<DynamicDict>();
            IWorkItemManagerDAO dao = SgdDaoFactory.getDaoImpl(WorkItemManagerDAOMySQL.class);
            logger.debug("nextTaskIds = " + nextTaskId);
            if (nextTaskId != null) {
                String[] ids = nextTaskId.split(",");
                for (String id : ids) {
                    list.addAll(dao.qryTaskListByTaskId(id));
                }

            }
            return list;
        }
        return null;
    }

    public void terminateProcess(String taskHolderId, String terminateUser, String reason)
            throws BaseAppException {

        AssertUtil.isNotNull(taskHolderId,
                "taskHolderId cannot be null");

        ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
        String processInstanceId = taskHolderDAO.selectTaskHolerId(taskHolderId);

        BpmRuntimeService bpmRuntimeService = ProcessServiceManager
                .getManager().getRuntimeService();
        try {
            bpmRuntimeService.terminateProcess(processInstanceId, reason);

//			IBpmRunTimeDAO bpmRunTimeDAO = DAOFactory
//					.getDAO(IBpmRunTimeDAO.class);
//			
//			bpmRunTimeDAO.updatePorcessState(processInstanceId, "T", reason);

            ITaskHolderEntity th = new TaskHolderEntity();
            HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
            holderChangeInfo.setAction(TaskHolderConstant.HOLDER_ACTION_STOP);
            holderChangeInfo.setHolderId(taskHolderId);
            holderChangeInfo.setReason(reason);
            holderChangeInfo.setUserName(terminateUser);

            th.changeHolder(holderChangeInfo);

        } catch (Exception ex) {
            ExceptionHandler.publish("BFM-BPM-00020", ex);
        }
    }

    public void suspendProcessByHolderId(String holderId, String suspendUser, String reason)
            throws BaseAppException {
        AssertUtil.isNotNull(holderId,
                "holderId cannot be null");

//		IBpmRunTimeDAO bpmRunTimeDAO = DAOFactory.getDAO(IBpmRunTimeDAO.class);
//
//		bpmRunTimeDAO.updatePorcessStateByHolderId(holderId, "B", reason);

        ITaskHolderEntity th = new TaskHolderEntity();
        HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
        holderChangeInfo.setAction(TaskHolderConstant.HOLDER_ACTION_BLOCK);
        holderChangeInfo.setHolderId(holderId);
        holderChangeInfo.setReason(reason);
        holderChangeInfo.setUserName(suspendUser);

        th.changeHolder(holderChangeInfo);
    }

    public void unSuspendProcessByHolderId(String holderId, String unSuspendUser, String reason)
            throws BaseAppException {
        AssertUtil.isNotNull(holderId, "holderId cannot be null");

//		IBpmRunTimeDAO bpmRunTimeDAO = DAOFactory.getDAO(IBpmRunTimeDAO.class);
//
//		bpmRunTimeDAO.updatePorcessStateByHolderId(holderId, "A", reason);

        ITaskHolderEntity th = new TaskHolderEntity();
        HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
        holderChangeInfo.setAction(TaskHolderConstant.HOLDER_ACTION_UNBLOCK);
        holderChangeInfo.setHolderId(holderId);
        holderChangeInfo.setReason(reason);
        holderChangeInfo.setUserName(unSuspendUser);

        th.changeHolder(holderChangeInfo);
    }

    public List<TaskListInfo> qryProcessTrack(String processInstanceId)
            throws BaseAppException {
        AssertUtil.isNotNull(processInstanceId,
                "processInstanceId cannot be null");

        IBpmRunTimeDAO bpmRunTimeDAO = SgdDaoFactory.getDaoImpl(BpmRunTimeDAOMySQL.class);

        List<TaskListInfo> taskList = bpmRunTimeDAO
                .qryTaskList(processInstanceId);

        for (TaskListInfo taskListInfo : taskList) {
            String state = taskListInfo.getTaskState();

            taskListInfo.setTaskStateStr(TaskListConstant.valueOf(state)
                    .state());
        }

        // 如何流程已经结束则加上流程结束节点
        String holderState = bpmRunTimeDAO
                .qryTaskHolderState(processInstanceId);
        if (TaskListConstant.H_COMPLETED.state().equals(holderState)) {
            TaskListInfo taskListInfo = new TaskListInfo();
            taskListInfo.setTaskListName("end");
            taskList.add(taskListInfo);
        }

        return taskList;
    }

    public List<TaskListInfo> qryProcessTrackByHolderId(String holderId)
            throws BaseAppException {
        AssertUtil.isNotNull(holderId,
                "holderId cannot be null");

        IBpmRunTimeDAO bpmRunTimeDAO = SgdDaoFactory.getDaoImpl(BpmRunTimeDAOMySQL.class);

        List<TaskListInfo> taskList = bpmRunTimeDAO
                .qryTaskListByHolderId(holderId);

        for (TaskListInfo taskListInfo : taskList) {
            String state = taskListInfo.getTaskState();

            taskListInfo.setTaskStateStr(TaskListConstant.valueOf(state)
                    .state());
        }

        // 如何流程已经结束则加上流程结束节点
        String holderState = bpmRunTimeDAO
                .qryTaskHolderStateByHolderId(holderId);

        if (TaskListConstant.H_COMPLETED.state().equals(holderState)) {
            TaskListInfo taskListInfo = new TaskListInfo();
            taskListInfo.setTaskListName("end");
            taskList.add(taskListInfo);
        }

        return taskList;
    }

    public List<FlowActivity> qryProcessAct(String processInstanceId,
                                            String currtaskListId) throws BaseAppException {
        IBpmRunTimeDAO bpmRunTimeDAO = SgdDaoFactory.getDaoImpl(BpmRunTimeDAOMySQL.class);

        // 当前任务
        String taskId = bpmRunTimeDAO.qryTaskId(currtaskListId);
        String actId = ((TaskEntity) serviceProvider.getTaskService()
                .createTaskQuery().taskId(taskId).list().get(0))
                .getTaskDefinitionKey();

        // 定义流程定义ID
        String processDefinitionId = ((ExecutionEntity) serviceProvider
                .getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId).list().get(0))
                .getProcessDefinitionId();

        return findFlowActivities(processDefinitionId, actId);
    }

    public List<FlowActivity> findFlowActivities(String processDefinitionId,
                                                 String actId) throws BaseAppException {
        RepositoryService repositoryService = serviceProvider
                .getProcessEngine().getRepositoryService();

        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);

        List<ActivityImpl> activities = def.getActivities();

        List<FlowActivity> flowActivities = findAllFlowActivity(activities,
                actId);

        IProcessTemplateDAO ProcessTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);

        for (FlowActivity flowActivitie : flowActivities) {

            if (StringUtil.isEmpty(flowActivitie.getTaskTemplateId())) {
                continue;
            }

            BpmTaskTemplateDto bpmTaskTemplateDto = ProcessTemplateDAO
                    .selTaskTempLate(Long.valueOf(flowActivitie
                            .getTaskTemplateId()));
            flowActivitie.setTaskTemplateCode(bpmTaskTemplateDto.getCode());
            flowActivitie.setTaskTemplateName(bpmTaskTemplateDto
                    .getTemplateName());
        }

        return flowActivities;
    }

    public List<FlowActivity> findPreOneActs(String currActId, String processInstanceId) throws BaseAppException {

        List<FlowActivity> acts = new ArrayList<FlowActivity>();

        String procDefId = ((ExecutionEntity) serviceProvider
                .getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId).list().get(0))
                .getProcessDefinitionId();

        List<ActivityImpl> preActs = findPreActs(currActId, procDefId);

        for (ActivityImpl preAct : preActs) {

            FlowActivity flowActivity = new FlowActivity();

            if (preAct.getActivityBehavior() instanceof ZNoneStartEventActivityBehavior) {

                flowActivity.setActName("start");
                flowActivity.setActType("start");

            } else {

                flowActivity.setActName((String) preAct.getProperty("name"));
                flowActivity.setActType((String) preAct.getProperty("type"));
            }

            flowActivity.setActId(preAct.getId());
            flowActivity.setTaskTemplateId((String) preAct.getProperty("taskTemplateId"));

            acts.add(flowActivity);
        }

        return acts;
    }

    private List<ActivityImpl> findPreActs(String currActId, String procDefId) throws BaseAppException {

        List<ActivityImpl> preActs = new ArrayList<ActivityImpl>();

        RepositoryService repositoryService = serviceProvider.getProcessEngine().getRepositoryService();

        //查流程所有环节
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(procDefId);
        List<ActivityImpl> allActs = def.getActivities();

        ActivityImpl currAct = null;
        //找当前环节
        for (ActivityImpl act : allActs) {

            if (act.getId().equals(currActId)) {

                currAct = act;
                break;
            }
        }

        if (currAct != null) {

            List<PvmTransition> incomings = currAct.getIncomingTransitions();

            for (PvmTransition incoming : incomings) {

                ActivityImpl incomingAct = (ActivityImpl) incoming.getSource();
                if (!(incomingAct.getActivityBehavior() instanceof GatewayActivityBehavior)) {

                    preActs.add(incomingAct);
                } else {//上一个环节是网关则继续往前找

                    preActs.addAll(findPreActs(incomingAct.getId(), procDefId));
                }
            }

        } else {

            ExceptionHandler.publish("can not find this active, actId:" + currActId);
        }

        return preActs;
    }

    public List<FlowActivity> qryProcessDefAct(String procBizKey) throws BaseAppException {

        IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);

        ProcessTemplateVersion processTemplateVersion = processTemplateDAO.selActiveProcessVer(procBizKey);

        String procDefId = processTemplateDAO.selProcdefId(processTemplateVersion.getProcDefId());

        return findFlowActivities(procDefId, null);
    }

    private List<FlowActivity> findAllFlowActivity(
            List<ActivityImpl> activities, String actId) {
        List<FlowActivity> flowActivityList = new ArrayList<FlowActivity>();

        //boolean isAdd = false;

        for (ActivityImpl act : activities) {
            ActivityBehavior activityBehavior = act.getActivityBehavior();

            if (activityBehavior != null) {
                if (activityBehavior instanceof ZUserTaskActivityBehavior
                        || activityBehavior instanceof ZClassDelegate
                        || activityBehavior instanceof ZBusinessRuleTaskActivityBehavior
                        || activityBehavior instanceof ZMQSendActivitiBehavior
                        || activityBehavior instanceof ZScriptTaskActivityBehavior) {

                    // 当前的环节不放在列表中
                    if (StringUtil.isNotEmpty(actId) && act.getId().equals(actId)) {
                        //isAdd = true;
                        continue;
                    }

//					if(!isAdd){
//						continue;
//					}

                    FlowActivity flowActivity = new FlowActivity();
                    flowActivity.setActId(act.getId());
                    flowActivity.setActName((String) act.getProperty("name"));
                    flowActivity.setActType((String) act.getProperty("type"));
                    flowActivity.setTaskTemplateId((String) act.getProperty("taskTemplateId"));

                    flowActivityList.add(flowActivity);
                } else if (activityBehavior instanceof SubProcessActivityBehavior) {
                    List<ActivityImpl> subActivities = act.getActivities();
                    if (subActivities != null && !subActivities.isEmpty()) {
                        flowActivityList.addAll(findAllFlowActivity(
                                subActivities, actId));
                    }
                }
            }
        }

        return flowActivityList;
    }

    public List<FlowActivity> findBeforeFlowActivity(String procDefId, String currActId) {
        List<FlowActivity> bFlowActivityList = new ArrayList<FlowActivity>();

        RepositoryService repositoryService = serviceProvider.getProcessEngine().getRepositoryService();

        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(procDefId);

        List<ActivityImpl> activities = def.getActivities();

        for (int i = activities.size() - 1; i >= 0; i--) {
            ActivityImpl act = activities.get(i);
            ActivityBehavior activityBehavior = act.getActivityBehavior();

            if (act.getId().equals(currActId)) {
                break;
            }

            if (activityBehavior != null) {
                if (activityBehavior instanceof ZUserTaskActivityBehavior
                        || activityBehavior instanceof ZClassDelegate
                        || activityBehavior instanceof ZBusinessRuleTaskActivityBehavior
                        || activityBehavior instanceof ZMQSendActivitiBehavior
                        || activityBehavior instanceof ZScriptTaskActivityBehavior) {

                    FlowActivity flowActivity = new FlowActivity();
                    flowActivity.setActId(act.getId());
                    flowActivity.setActName((String) act.getProperty("name"));
                    flowActivity.setActType((String) act.getProperty("type"));
                    flowActivity.setTaskTemplateId((String) act.getProperty("taskTemplateId"));

                    bFlowActivityList.add(flowActivity);
                } else if (activityBehavior instanceof SubProcessActivityBehavior) {
                    List<ActivityImpl> subActivities = act.getActivities();
                    if (subActivities != null && !subActivities.isEmpty()) {
                        bFlowActivityList.addAll(findAllFlowActivity(
                                subActivities, currActId));
                    }
                }
            }
        }

        return bFlowActivityList;
    }

    public List<FlowActivity> findAfterFlowActivity(String processInstanceId, String currActId) {
        List<FlowActivity> bFlowActivityList = new ArrayList<FlowActivity>();
        if (serviceProvider.getRuntimeService().createProcessInstanceQuery() == null || serviceProvider.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).list() == null
                || serviceProvider.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).list().size() == 0
                ) {
            return null;
        }
        String procDefId = ((ExecutionEntity) serviceProvider.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).list().get(0)).getProcessDefinitionId();

        RepositoryService repositoryService = serviceProvider.getProcessEngine().getRepositoryService();

        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(procDefId);

        List<ActivityImpl> activities = def.getActivities();

        for (ActivityImpl act : activities) {
            ActivityBehavior activityBehavior = act.getActivityBehavior();

            if (act.getId().equals(currActId)) {
                break;
            }

            if (activityBehavior != null) {
                if (activityBehavior instanceof ZUserTaskActivityBehavior
                        || activityBehavior instanceof ZClassDelegate
                        || activityBehavior instanceof ZBusinessRuleTaskActivityBehavior
                        || activityBehavior instanceof ZMQSendActivitiBehavior
                        || activityBehavior instanceof ZScriptTaskActivityBehavior) {

                    FlowActivity flowActivity = new FlowActivity();
                    flowActivity.setActId(act.getId());
                    flowActivity.setActName((String) act.getProperty("name"));
                    flowActivity.setActType((String) act.getProperty("type"));
                    flowActivity.setTaskTemplateId((String) act.getProperty("taskTemplateId"));

                    bFlowActivityList.add(flowActivity);
                } else if (activityBehavior instanceof SubProcessActivityBehavior) {
                    List<ActivityImpl> subActivities = act.getActivities();
                    if (subActivities != null && !subActivities.isEmpty()) {
                        bFlowActivityList.addAll(findAllFlowActivity(
                                subActivities, currActId));
                    }
                }
            }
        }

        return bFlowActivityList;
    }

    public String completeTask(String taskId, Map<String, Object> variables)
            throws BaseAppException {
        BpmRuntimeService bpmRuntimeService = ProcessServiceManager
                .getManager().getRuntimeService();
        try {
            return bpmRuntimeService.completeTaskByTaskId(taskId, variables);
        } catch (ActivitiException e) {
            ExceptionHandler.publish(e.getMessage(), e);
        }

        return null;
    }

    public String jumpTask(String taskId, String nextActivityId,
                           Map<String, Object> variables) throws BaseAppException {

        TaskService taskService = serviceProvider.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();

        // 找当前任务
        TaskEntity taskEntity = (TaskEntity) taskQuery.taskId(taskId)
                .singleResult();

        List<Task> tasks = taskQuery.executionId(taskEntity.getExecutionId())
                .list();

        if (tasks != null && tasks.size() > 1) {
            ExceptionHandler.publish("一次只能从一个任务上跳走!");
        }

        RepositoryService repositoryService = serviceProvider
                .getProcessEngine().getRepositoryService();

        // 找流程定义
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskEntity
                        .getProcessDefinitionId());
        // 获得流程上的所有节点
        List<ActivityImpl> activitiList = def.getActivities();

        RuntimeService runtimeService = serviceProvider.getProcessEngine()
                .getRuntimeService();

        // 获取当前执行实例
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService
                .createExecutionQuery()
                .executionId(taskEntity.getExecutionId()).singleResult();

        // 在流程的所有节点中找当前环节
        ActivityImpl srcActiveActivity = findActivityImpl(activitiList,
                executionEntity.getActivityId());

        // 在流程的所有节点中找需要跳转的目标节点
        ActivityImpl tarActiveActivity = findActivityImpl(activitiList,
                nextActivityId);

        // 动态创建一条线
        TransitionImpl transition = srcActiveActivity
                .createDynOutgoingTransition();
        transition.setDestination(tarActiveActivity);

        // 打流程跳转标记
        ((ZRuntimeServiceImpl) serviceProvider.getRuntimeService())
                .getCommandExecutor().execute(
                new ZUserTaskJumpSignCommand(executionEntity));

        // 手动完成任务
        return completeTask(taskId, variables);
    }

    public String jumpTaskByTemplateId(String taskId, String taskTemplateId,
                                       Map<String, Object> variables) throws BaseAppException {

        TaskService taskService = serviceProvider.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery();

        // 找当前任务
        TaskEntity taskEntity = (TaskEntity) taskQuery.taskId(taskId)
                .singleResult();

        List<Task> tasks = taskQuery.executionId(taskEntity.getExecutionId())
                .list();

        if (tasks != null && tasks.size() > 1) {
            ExceptionHandler.publish("一次只能从一个任务上跳走!");
        }

        RepositoryService repositoryService = serviceProvider
                .getProcessEngine().getRepositoryService();

        // 找流程定义
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskEntity
                        .getProcessDefinitionId());
        // 获得流程上的所有节点
        List<ActivityImpl> activitiList = def.getActivities();

        RuntimeService runtimeService = serviceProvider.getProcessEngine()
                .getRuntimeService();

        // 获取当前执行实例
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService
                .createExecutionQuery()
                .executionId(taskEntity.getExecutionId()).singleResult();

        // 在流程的所有节点中找当前环节
        ActivityImpl srcActiveActivity = findActivityImpl(activitiList,
                executionEntity.getActivityId());

        // 在流程的所有节点中找需要跳转的目标节点
        ActivityImpl tarActiveActivity = findActivityImpl2(activitiList,
                taskTemplateId);

        // 动态创建一条线
        TransitionImpl transition = srcActiveActivity
                .createDynOutgoingTransition();
        transition.setDestination(tarActiveActivity);

        // 打流程跳转标记
        ((ZRuntimeServiceImpl) serviceProvider.getRuntimeService())
                .getCommandExecutor().execute(
                new ZUserTaskJumpSignCommand(executionEntity));

        // 手动完成任务
        return completeTask(taskId, variables);
    }

    private ActivityImpl findActivityImpl(List<ActivityImpl> activitiList,
                                          String activitiId) {

        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (id.equals(activitiId)) {
                return activityImpl;
            } else {
                List<ActivityImpl> subsActivities = activityImpl
                        .getActivities();
                if (subsActivities != null && !subsActivities.isEmpty()) {
                    ActivityImpl subsActivity = findActivityImpl(
                            subsActivities, activitiId);
                    if (subsActivity != null) {
                        return subsActivity;
                    }
                }
            }
        }
        return null;
    }

    private ActivityImpl findActivityImpl2(List<ActivityImpl> activitiList,
                                           String taskTemplateId) {

        for (ActivityImpl activityImpl : activitiList) {
            //String id = activityImpl.getId();
            String tId = (String) activityImpl.getProperty("taskTemplateId");

            if (StringUtil.isEmpty(tId)) {
                continue;
            }

            if (tId.equals(taskTemplateId)) {
                return activityImpl;
            } else {
                List<ActivityImpl> subsActivities = activityImpl
                        .getActivities();
                if (subsActivities != null && !subsActivities.isEmpty()) {
                    ActivityImpl subsActivity = findActivityImpl(
                            subsActivities, taskTemplateId);
                    if (subsActivity != null) {
                        return subsActivity;
                    }
                }
            }
        }
        return null;
    }

    public void backTask(String taskId, Map<String, Object> variables)
            throws BaseAppException {
        BpmRuntimeService bpmRuntimeService = ProcessServiceManager
                .getManager().getRuntimeService();
        Task task = ProcessServiceManager.getManager().getServiceProvider()
                .getTaskService().createTaskQuery().taskId(taskId)
                .singleResult();
        if (task != null) {
            try {
                bpmRuntimeService.backUserTask(task.getExecutionId());
            } catch (ActivitiException e) {
                ExceptionHandler.publish(e.getMessage(), e);
            }
        }

    }

    public boolean reExecuteTask(String executionId) throws BaseAppException {
        BpmRuntimeService bpmRuntimeService = ProcessServiceManager
                .getManager().getRuntimeService();
        try {
            return bpmRuntimeService.executeServiceTask(executionId);
        } catch (Exception ex) {
            if (ex instanceof BaseAppException) {
                throw (BaseAppException) ex;
            }
            ExceptionHandler.publish("BFM-BPM-00021", ex);
        }
        return false;
    }

    public boolean isTasktExist(String taskId) {
        Task task = serviceProvider.getTaskService().createTaskQuery()
                .taskId(taskId).singleResult();
        return task == null ? false : true;
    }

    public List<Map<String, Object>> queryActivityInstance(
            String processInstanceId) throws BaseAppException {
        HistoryService historyService = serviceProvider.getHistoryService();
        RepositoryService repositoryService = serviceProvider
                .getRepositoryService();
        RuntimeService runtimeService = serviceProvider.getRuntimeService();
        HistoricActivityInstanceQuery query = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(
                        processInstanceId);

        List<HistoricActivityInstance> queryResult = query
                .orderByHistoricActivityInstanceId().asc().list();
        List<Map<String, Object>> retVal = new ArrayList<Map<String, Object>>();
        if (queryResult != null) {
            /*
             * 已执行步骤
			 */
            for (HistoricActivityInstance inst : queryResult) {
                Map<String, Object> hisInfoMap = __buildHistoryActivityInfo__(
                        inst, repositoryService, runtimeService);
                retVal.add(hisInfoMap);
            }
            /*
             * 取出下一步
			 */
            if (queryResult.size() > 0) {
                HistoricActivityInstance last = queryResult.get(queryResult
                        .size() - 1);
                ActivityImpl lastActivity = ZExtendsionUtil.getActivity(
                        repositoryService, last.getProcessDefinitionId(),
                        last.getActivityId());
                List<PvmTransition> transitionList = lastActivity
                        .getOutgoingTransitions();
                for (PvmTransition transition : transitionList) {
                    ActivityImpl next = (ActivityImpl) transition
                            .getDestination();
                    retVal.add(buildUnexecutedActivityInfo(processInstanceId,
                            next));
                }
            }
        }
        return retVal;
    }

    public List<Map<String, Object>> queryHisActivityInstance(
            String processInstanceId) throws BaseAppException {
        ServiceProvider serviceProvider = ProcessServiceManager.getManager()
                .getServiceProvider();
        HistoryService historyService = serviceProvider.getHistoryService();
        RepositoryService repositoryService = serviceProvider
                .getRepositoryService();
        RuntimeService runtimeService = serviceProvider.getRuntimeService();
        HistoricActivityInstanceQuery query = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(
                        processInstanceId);

        List<HistoricActivityInstance> queryResult = query
                .orderByHistoricActivityInstanceId().asc().list();
        List<Map<String, Object>> retVal = new ArrayList<Map<String, Object>>();
        if (queryResult != null) {
            for (HistoricActivityInstance inst : queryResult) {
                Map<String, Object> hisInfoMap = __buildHistoryActivityInfo__(
                        inst, repositoryService, runtimeService);
                retVal.add(hisInfoMap);
            }
        }
        return retVal;
    }

    public HistoricActivityInstance findPreUserTask(String taskId)
            throws BaseAppException {
        // 当前任务详情
        TaskEntity task = (TaskEntity) serviceProvider.getTaskService()
                .createTaskQuery().taskId(taskId).list().get(0);
        String processInstanceId = task.getProcessInstanceId();

        // 查活动记录(降序)
        List<HistoricActivityInstance> historicActivityInstances = serviceProvider
                .getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId().desc().list();

        int l = historicActivityInstances.size();
        HistoricActivityInstance currHistoricActivityInstance = null;
        HistoricActivityInstance preHistoricActivityInstance = null;
        for (int i = 0; i < l; i++) {
            HistoricActivityInstance historicActivityInstance = historicActivityInstances
                    .get(i);

            if (currHistoricActivityInstance == null) {
                // 先找到当前任务活动
                if (historicActivityInstance.getActivityId().equals(
                        task.getTaskDefinitionKey())
                        && historicActivityInstance.getExecutionId().equals(
                        task.getExecutionId())) {

                    currHistoricActivityInstance = historicActivityInstance;
                    continue;
                }
            }

            if (preHistoricActivityInstance == null) {

                // 找到上一个人工任务活动
                if ("userTask".equals(historicActivityInstance
                        .getActivityType())) {
                    preHistoricActivityInstance = historicActivityInstance;
                    continue;
                }
            }
        }

        if (currHistoricActivityInstance == null) {
            ExceptionHandler.publish("当前人工任务活动没有找到,taskId:" + taskId);
        } else if (preHistoricActivityInstance == null) {
            ExceptionHandler.publish("上一个人工任务活动没有找到,taskId:" + taskId);
        }

        return preHistoricActivityInstance;
    }

    public ActivityInfo[] queryActivities(String processDefinitionId)
            throws BaseAppException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 根据流程定义查询流程实例
     *
     * @param processDefinitionId
     * @return
     * @throws BaseAppException
     */
    public List<Map<String, Object>> queryProcessIntances(
            String processDefinitionId, String isQueryFinished)
            throws BaseAppException {

        if (processDefinitionId == null) {
            throw new IllegalArgumentException("TEMPLATE_ID is required");
        }
        HistoryService historyService = serviceProvider.getHistoryService();
        RepositoryService repositoryService = serviceProvider
                .getRepositoryService();
        HistoricProcessInstanceQuery procInstQuery = historyService
                .createHistoricProcessInstanceQuery();

        if (processDefinitionId != null) {
            ProcessDefinitionQuery defQuery = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionId).latestVersion();
            ProcessDefinition processDef = defQuery.singleResult();
            if (processDef != null) {
                procInstQuery = procInstQuery.processDefinitionId(processDef
                        .getId());
            } else {
                logger.error("Cannot find process definition for key "
                        + processDefinitionId);
                return null;
            }
        }
        if (isQueryFinished != null) {
            boolean flag = Boolean.valueOf(isQueryFinished);
            if (flag) {
                procInstQuery.finished();
            } else {
                procInstQuery.unfinished();
            }
        }
        List<HistoricProcessInstance> queryResult = procInstQuery
                .orderByProcessInstanceStartTime().desc().list();
        List<Map<String, Object>> retVal = new ArrayList<Map<String, Object>>();
        if (queryResult != null) {
            for (HistoricProcessInstance inst : queryResult) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID", inst.getId());
                map.put("START_TIME", __formatTime__(inst.getStartTime()));
                map.put("END_TIME", __formatTime__(inst.getEndTime()));
                map.put("START_ACTIVITY_ID", inst.getStartActivityId());
                map.put("END_ACTIVITY_ID", inst.getEndActivityId());
                retVal.add(map);
            }
        }
        return retVal;
    }

    public List<DynamicDict> qryProcessVar(String procInstId, String varName)
            throws BaseAppException {
        IBpmRunTimeDAO bpmRunTimeDAO = SgdDaoFactory.getDaoImpl(BpmRunTimeDAOMySQL.class);
        List<ProcessRunVar> varList = null;
        List<DynamicDict> retList = new ArrayList<DynamicDict>();
        if (varName == null || varName.trim().length() == 0) {
            varList = bpmRunTimeDAO.qryAllProcessVar(procInstId);
        } else {
            varList = bpmRunTimeDAO.qryProcessVar(procInstId, varName);
        }
        if (varList != null) {
            for (ProcessRunVar dto : varList) {
                if (!"BPM_TASK_HOLDER_ID".equalsIgnoreCase(dto.getVarname())) {
                    DynamicDict bo = new DynamicDict();

                    String vartype = dto.getVartype();

                    bo.set("VAR_TYPE", vartype);
                    bo.set("VAR_NAME", dto.getVarname());

                    if ("double".equals(vartype)) {
                        bo.set("VAR_VALUE", dto.getVardouble());
                    } else if ("date".equals(vartype)) {
                        bo.set("VAR_VALUE", DateUtil.date2String(new Date(dto.getVardate()), DateUtil.DATETIME_FORMAT_1));
                    } else if ("boolean".equals(vartype)) {
                        bo.set("VAR_VALUE", dto.getVardate() == 1 ? true : false);
                    } else {
                        bo.set("VAR_VALUE", dto.getVartext());
                    }

                    retList.add(bo);
                }
            }
        }

        return retList;
    }

    private Map<String, Object> __buildHistoryActivityInfo__(
            HistoricActivityInstance inst, RepositoryService repositoryService,
            RuntimeService runtimeService) {
        String activityType = inst.getActivityType();
        Date endTime = inst.getEndTime();

        Map<String, Object> hisInfoMap = new HashMap<String, Object>();
        hisInfoMap.put("INSTANCE_ID", inst.getId());
        hisInfoMap.put("ACTIVITY_ID", inst.getActivityId());
        hisInfoMap.put("ACTIVITY_NAME", inst.getActivityName());
        hisInfoMap.put("ACTIVITY_TYPE", activityType);
        hisInfoMap.put("START_TIME", __formatTime__(inst.getStartTime()));
        hisInfoMap.put("END_TIME", __formatTime__(endTime));
        hisInfoMap.put("EXECUTION_ID", inst.getExecutionId());
        hisInfoMap.put("COMMENTS", null);
        if (endTime != null) {
            hisInfoMap.put("STATE", BpmConstants.ACTIVITY_STATE_COMPLETED);
        } else {
            hisInfoMap.put("STATE", BpmConstants.ACTIVITY_STATE_PROCESSING);
        }
        return hisInfoMap;
    }

    private String __formatTime__(Date time) {
        if (time == null) {
            return null;
        }
        return DateUtil.date2String(time, DateUtil.DATETIME_FORMAT_1);
    }

    private Map<String, Object> buildUnexecutedActivityInfo(
            String processInstanceId, ActivityImpl activity) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("INSTANCE_ID", processInstanceId);
        map.put("ACTIVITY_ID", activity.getId());
        map.put("ACTIVITY_NAME", activity.getProperty("name"));
        map.put("ACTIVITY_TYPE", activity.getProperty("type"));
        map.put("START_TIME", null);
        map.put("END_TIME", null);
        if ("endEvent".equals((String) activity.getProperty("type"))) {
            map.put("STATE", null);
            map.put("COMMENTS", "End event.");
        } else {
            map.put("STATE", BpmConstants.ACTIVITY_STATE_UNEXECUTED);
            map.put("COMMENTS", "One of the potential next steps.");
        }
        return map;
    }

    @Override
    public List<DynamicDict> backTask(ProcessBackInfo processBackInfo)
            throws BaseAppException {

        String taskListId = processBackInfo.getTaskListId();

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        TaskListEntity taskListEntity = taskListDAO.selectTaskListDetail(taskListId);

        IFlowManagerDAO flowDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        processBackInfo.setHolderId(taskListEntity.getHolderId());
        processBackInfo.setCreatedTime(DateUtil.GetDBDateTime());
        processBackInfo.setBackId(UUID.randomUUID().toString());
        processBackInfo.setState("A");

        //插入回退实例单
        flowDao.insertBackHolder(processBackInfo);

        //修改流程状态并记录日志
        ITaskHolderEntity th = new TaskHolderEntity();
        HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
        holderChangeInfo.setAction(TaskHolderConstant.HOLDER_ACTION_BACK);
        holderChangeInfo.setHolderId(processBackInfo.getHolderId());
        holderChangeInfo.setReason(processBackInfo.getBackComments());
        holderChangeInfo.setUserName(processBackInfo.getBackUser());
        th.changeHolder(holderChangeInfo);

        //更新当前任务状态被回退了
        flowDao.backTaskList(taskListId, processBackInfo.getUserId());

        BpmRuntimeService bpmRuntimeService = ProcessServiceManager.getManager().getRuntimeService();
        //通知引擎回退
        List<String> taskIds = bpmRuntimeService.backUserTask(taskListEntity.getExecutionId());

        //获取 当前任务
        List<DynamicDict> list = new ArrayList<DynamicDict>();

        if (!taskIds.isEmpty()) {

            IWorkItemManagerDAO workItemManagerDAO = SgdDaoFactory.getDaoImpl(WorkItemManagerDAOMySQL.class);

            for (int i = 0; i < taskIds.size(); i++) {

                list.addAll(workItemManagerDAO.qryTaskListByTaskId(taskIds.get(i)));
            }
        }

        return list;
    }

    public Map<String, Object> qryProcessRtVar(String holderId) throws BaseAppException {

        ITaskHolderDAO dao = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
        TaskHolderEntity holderEntity = dao.select(holderId);
        String procInstId = holderEntity.getProcInstId();

        return serviceProvider.getRuntimeService().getVariables(procInstId);
    }

    public static void main(String[] args) throws BaseAppException {

        SessionContext.newSession().beginTrans();
        BpmServiceImpl.getInstance().startProcess("C5AB2A33D6C000016D8F198041061630", null);
        SessionContext.currentSession().commitTrans();
    }

    @Override
    public List<DynamicDict> backProcess(ProcessBackInfo processBackInfo)
            throws BaseAppException {

        String holderId = processBackInfo.getHolderId();
        ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
        TaskHolderEntity entry = taskHolderDAO.select(holderId);
        String procInstId = entry.getProcInstId();
        TaskService taskService = ProcessServiceManager.getManager().getServiceProvider().getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(procInstId);
        List<Task> tasks = taskQuery.list();
        if (tasks == null || tasks.size() == 0) {

            ExceptionHandler.publish("there no active task in this holder, holderId:" + holderId);
        } else if (tasks.size() > 1) {

            ExceptionHandler.publish("withdraw holder error, there are too many tasks in this holder, holderId:" + holderId);

        } else if (tasks.size() == 1) {

            IFlowManagerDAO flowDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

            String taskId = tasks.get(0).getId();
            ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
            TaskListEntity taskEntity = taskListDAO.selTaskDetail(taskId);

            processBackInfo.setCreatedTime(DateUtil.GetDBDateTime());
            processBackInfo.setBackId(UUID.randomUUID().toString());
            processBackInfo.setState("A");

            //修改流程状态并记录日志
            ITaskHolderEntity th = new TaskHolderEntity();
            HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
            holderChangeInfo.setAction(TaskHolderConstant.HOLDER_ACTION_WITHDRAW);
            holderChangeInfo.setHolderId(processBackInfo.getHolderId());
            holderChangeInfo.setReason(processBackInfo.getBackComments());
            holderChangeInfo.setUserName(processBackInfo.getBackUser());
            th.changeHolder(holderChangeInfo);

            //插入回退实例单
            flowDao.insertBackHolder(processBackInfo);


            //更新当前任务状态被回退了
            flowDao.backTaskList(taskEntity.getTaskListId(), processBackInfo.getUserId());

            BpmRuntimeService bpmRuntimeService = ProcessServiceManager.getManager().getRuntimeService();
            //通知引擎回退
            List<String> taskIds = bpmRuntimeService.backUserTask(taskEntity.getExecutionId());

            //获取 当前任务
            List<DynamicDict> list = new ArrayList<DynamicDict>();

            if (!taskIds.isEmpty()) {

                IWorkItemManagerDAO workItemManagerDAO = SgdDaoFactory.getDaoImpl(WorkItemManagerDAOMySQL.class);

                for (int i = 0; i < taskIds.size(); i++) {

                    list.addAll(workItemManagerDAO.qryTaskListByTaskId(taskIds.get(i)));
                }
            }

            return list;
        }

        return null;
    }
}
