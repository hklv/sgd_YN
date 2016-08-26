package com.ztesoft.uboss.bpm.runtime.persistence;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.model.BpmTaskAssginDto;
import org.activiti.uboss.model.ZFlowDef;
import org.activiti.uboss.task.ITaskListEntity;

import com.ztesoft.uboss.bpm.runtime.constant.TaskListConstant;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;


public class TaskListEntity implements ITaskListEntity {

    private static ZSmartLogger logger = ZSmartLogger.getLogger(TaskListEntity.class);
    private static ITaskListDAO taskDAO = null;

    private String taskListId;
    private String holderId;
    private String siginType;
    private String taskType;
    private Long templateId;
    private String procInstId;
    private String executionId;
    private String taskId;
    private String taskName;
    private String parentTaskId;
    private Long owner;
    private Long userId;
    private Long roleId;
    private Long jobId;
    private Long orgId;
    private String description;
    private Date startTime;
    private Date endTime;
    private Long duration;
    private Long priority;
    private Date dueDate;
    private Date createDate;
    private String state;
    private Date stateDate;
    private String stateReason;
    private String actInstId;
    private String taskResult;
    private String taskParam1;
    private String taskParam2;
    private String taskParam3;
    private String taskParam4;
    private String taskParam5;
    private String taskParam6;

    private String direction;
    private String actId;

    private BpmTaskAssginDto designate;

    public void createUserTask(String taskTemplateId, TaskEntity task,
                               ActivityExecution execution) throws BaseAppException {
        logger.debug("[BEGIN-createUserTask],taskTemplateId={},taskName={}",
                taskTemplateId, task.getName());

        if (execution.isConcurrent()) {
            direction = execution.getParent().getDirection();

        } else {
            direction = execution.getDirection();
        }

        actId = task.getTaskDefinitionKey();

        this.holderId = execution.getVariable(ZFlowDef.BPM_TASK_HOLDER_ID)
                .toString();
        this.taskListId = UUID.randomUUID().toString();
        this.executionId = execution.getId();
        this.procInstId = execution.getProcessInstanceId();
        this.createDate = DateUtil.GetDBDateTime();
        this.startTime = this.createDate;
        if (task.getDueDate() != null) {
            this.dueDate = new Date(task.getDueDate().getTime());
        }
        this.priority = new Long(task.getPriority());
        this.description = task.getDescription();
        this.taskId = task.getId();
        this.taskName = task.getName();
        this.taskType = "U";
        if (execution.getActivity().getProperty("historicActivityInstanceId") != null) {
            this.actInstId = (String) execution.getActivity().getProperty(
                    "historicActivityInstanceId");
        }

        this.templateId = Long.valueOf(taskTemplateId);

        // 设置处理人
        setAssgin(taskTemplateId, execution);

        if (this.owner == null && this.roleId == null && this.jobId == null
                && this.orgId == null) {
            throw new ActivitiException(this.taskName + "创建失败,未指定执行人.");
        }

        this.state = TaskListConstant.PENDING.state();
        this.stateDate = this.startTime;

        getTaskListDAO().save(this);
        dealExtTaskListParams(this, execution);
        logger.debug("[END-createUserTask]--taskname ={}", task.getName());
    }

    private void dealExtTaskListParams(TaskListEntity entity,
                                       ActivityExecution execution) throws BaseAppException {
        /* 扩展字段取值 */
        Object task_param1 = execution.getVariable("task_param1");
        Object task_param2 = execution.getVariable("task_param2");
        Object task_param3 = execution.getVariable("task_param3");
        Object task_param4 = execution.getVariable("task_param4");
        Object task_param5 = execution.getVariable("task_param5");
        Object task_param6 = execution.getVariable("task_param6");
        execution.removeVariable("task_param1");
        execution.removeVariable("task_param2");
        execution.removeVariable("task_param3");
        execution.removeVariable("task_param4");
        execution.removeVariable("task_param5");
        execution.removeVariable("task_param6");

        logger.debug("[task_param1]= " + task_param1);
        logger.debug("[task_param2]= " + task_param2);
        logger.debug("[task_param3]= " + task_param3);
        logger.debug("[task_param4]= " + task_param4);
        logger.debug("[task_param5]= " + task_param5);
        logger.debug("[task_param6]= " + task_param6);

        if (entity != null && entity.getTaskListId() != null
                && task_param1 != null) {
            entity.setTaskParam1(task_param1 == null ? null : task_param1
                    .toString());
            entity.setTaskParam2(task_param2 == null ? null : task_param2
                    .toString());
            entity.setTaskParam3(task_param3 == null ? null : task_param3
                    .toString());
            entity.setTaskParam4(task_param4 == null ? null : task_param4
                    .toString());
            entity.setTaskParam5(task_param5 == null ? null : task_param5
                    .toString());
            entity.setTaskParam6(task_param6 == null ? null : task_param6
                    .toString());
            logger.debug("[==saveTaskListParam==]");
            getTaskListDAO().saveParam(entity);
        }

    }

    public void createUserTask(String taskTemplateId, TaskEntity task,
                               ActivityExecution execution, BpmTaskAssginDto designate)
            throws BaseAppException {
        this.designate = designate;

        createUserTask(taskTemplateId, task, execution);
    }

    private void setAssgin(String taskTemplateId, ActivityExecution execution) throws BaseAppException {
        // 前一个环节执行了处理人
        if (designate != null) {

            this.orgId = designate.getOrgId();
            this.roleId = designate.getRoleId();
            this.owner = designate.getUserId();
            this.jobId = designate.getJobId();
        }
        // 没有指定就取任务里面的处理人
        else {
            BpmTaskAssginDto candidate = getTaskListDAO().selectTaskCandidate(
                    taskTemplateId);

            if (candidate != null) {

                if (StringUtil.isNotEmpty(candidate.getServiceName())) {

                    String serviceName = candidate.getServiceName();
                    String[] services = serviceName.split("\\.");

                    DynamicDict dict = new DynamicDict();

                    dict.valueMap = (HashMap<String, Object>) execution.getVariables();

                    dict.serviceName = services[0];
                    dict.set("method", services[1]);

                    ServiceFlow.callService(dict);

                    this.orgId = dict.getLong("bpm_org_id");
                    this.roleId = dict.getLong("bpm_role_id");
                    this.owner = dict.getLong("bpm_user_id");
                    this.jobId = dict.getLong("bpm_job_id");

                } else {
                    this.orgId = candidate.getOrgId();
                    this.roleId = candidate.getRoleId();
                    this.owner = candidate.getUserId();
                    this.jobId = candidate.getJobId();
                }
            }
        }
    }

    public void createSysTask(ActivityExecution execution, String taskTemplateId, boolean isBlocked)
            throws BaseAppException {
        logger.debug("--store data into BPM_TASK_LIST--");

        direction = execution.getDirection();
        actId = execution.getActivity().getId();

        this.holderId = execution.getVariable(ZFlowDef.BPM_TASK_HOLDER_ID)
                .toString();
        this.taskListId = UUID.randomUUID().toString();
        this.executionId = execution.getId();
        this.procInstId = execution.getProcessInstanceId();
        this.taskName = (String) execution.getActivity().getProperty("name");
        this.createDate = DateUtil.GetDBDateTime();
        this.startTime = this.createDate;
        this.endTime = this.createDate;
        this.taskType = "S";

        if (isBlocked) {
            this.state = "K";
        } else {
            this.state = "C";
        }

        if (execution.getActivity().getProperty("historicActivityInstanceId") != null) {
            this.actInstId = (String) execution.getActivity().getProperty(
                    "historicActivityInstanceId");
        }
        if (taskTemplateId != null) {
            this.templateId = Long.valueOf(taskTemplateId);
        }

        this.actId = execution.getActivity().getId();
        this.direction = execution.getDirection();

        getTaskListDAO().save(this);
        dealExtTaskListParams(this, execution);
        logger.debug("--complete store--");
    }

    public void createSysExTask(ActivityExecution execution,
                                String taskTemplateId, String exceptionMsg) throws Exception {
        logger.debug("--store data into BPM_TASK_LIST--");

        direction = execution.getDirection();
        actId = execution.getActivity().getId();

        this.holderId = execution.getVariable(ZFlowDef.BPM_TASK_HOLDER_ID)
                .toString();
        this.taskListId = UUID.randomUUID().toString();
        this.executionId = execution.getId();
        this.procInstId = execution.getProcessInstanceId();
        this.taskName = (String) execution.getActivity().getProperty("name");
        this.createDate = DateUtil.GetDBDateTime();
        this.startTime = this.createDate;
        this.taskType = "S";
        this.state = "E";
        this.stateDate = this.createDate;
        if (execution.getActivity().getProperty("historicActivityInstanceId") != null) {
            this.actInstId = (String) execution.getActivity().getProperty(
                    "historicActivityInstanceId");
        }
        if (StringUtil.isNotEmpty(taskTemplateId)) {
            this.templateId = Long.valueOf(taskTemplateId);
        }
        if (exceptionMsg != null && exceptionMsg.length() > 1000) {
            this.stateReason = exceptionMsg.substring(1, 1000);
        } else {
            this.stateReason = exceptionMsg;
        }

        this.actId = execution.getActivity().getId();
        this.direction = execution.getDirection();

        getTaskListDAO().save(this);
        dealExtTaskListParams(this, execution);
        logger.debug("--complete store--");
    }

    public void updateSysTaskState(String exceptionTaskListId, String state)
            throws Exception {
        getTaskListDAO().updateTaskState(exceptionTaskListId, state);
    }

    public String getTaskListId() {
        return taskListId;
    }

    public void setTaskListId(String taskListId) {
        this.taskListId = taskListId;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getSiginType() {
        return siginType;
    }

    public void setSiginType(String siginType) {
        this.siginType = siginType;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public String getStateReason() {
        return stateReason;
    }

    public void setStateReason(String stateReason) {
        this.stateReason = stateReason;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public String getTaskParam1() {
        return taskParam1;
    }

    public void setTaskParam1(String taskParam1) {
        this.taskParam1 = taskParam1;
    }

    public String getTaskParam2() {
        return taskParam2;
    }

    public void setTaskParam2(String taskParam2) {
        this.taskParam2 = taskParam2;
    }

    public String getTaskParam3() {
        return taskParam3;
    }

    public void setTaskParam3(String taskParam3) {
        this.taskParam3 = taskParam3;
    }

    public String getTaskParam4() {
        return taskParam4;
    }

    public void setTaskParam4(String taskParam4) {
        this.taskParam4 = taskParam4;
    }

    public String getTaskParam5() {
        return taskParam5;
    }

    public void setTaskParam5(String taskParam5) {
        this.taskParam5 = taskParam5;
    }

    public String getTaskParam6() {
        return taskParam6;
    }

    public void setTaskParam6(String taskParam6) {
        this.taskParam6 = taskParam6;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public void createMultiUserTask(String taskTemplateId, TaskEntity task,
                                    ActivityExecution execution, List<BpmTaskAssginDto> designates)
            throws Exception {

        // 指定多人处理时默认第一个人的单子为主单
        if (designates.size() > 0) {
            this.designate = designates.get(0);
            this.createUserTask(taskTemplateId, task, execution);
        }

        if (designates.size() > 1) {
            for (int i = 1; i < designates.size(); i++) {
                createSubUserTask(designates.get(i));
            }
        }

    }

    private void createSubUserTask(BpmTaskAssginDto designate) throws Exception {
        TaskListEntity subTaskList = new TaskListEntity();

        subTaskList.setHolderId(holderId);
        subTaskList.setParentTaskId(taskListId);
        subTaskList.setTaskType(taskType);
        subTaskList.setTaskListId(UUID.randomUUID().toString());
        subTaskList.setExecutionId(executionId);
        subTaskList.setState(state);
        subTaskList.setStateDate(stateDate);
        subTaskList.setProcInstId(procInstId);
        subTaskList.setCreateDate(createDate);
        subTaskList.setActInstId(actInstId);
        subTaskList.setDueDate(dueDate);
        subTaskList.setPriority(priority);
        subTaskList.setTaskName(taskName);
        subTaskList.setTemplateId(templateId);
        subTaskList.setStartTime(startTime);

        subTaskList.setOwner(designate.getUserId());
        subTaskList.setOrgId(designate.getOrgId());
        subTaskList.setJobId(designate.getJobId());
        subTaskList.setRoleId(designate.getRoleId());

        getTaskListDAO().save(subTaskList);
    }

    public BpmTaskAssginDto getAssgin(String taskTemplateId) throws Exception {

        BpmTaskAssginDto bpmTaskAssginDto = getTaskListDAO()
                .selectTaskCandidate(taskTemplateId);

        if (bpmTaskAssginDto != null) {
            Long roleId = bpmTaskAssginDto.getRoleId();

            if (roleId != null) {
                bpmTaskAssginDto.setRoleUserIds(getTaskListDAO()
                        .selectRoleUsers(roleId));
            }
        }

        return bpmTaskAssginDto;
    }

    private ITaskListDAO getTaskListDAO() throws BaseAppException {
        if (taskDAO == null)
            taskDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        return taskDAO;
    }

    @Override
    public void compSignalSysTask(String taskListId, Long userId)
            throws Exception {

        getTaskListDAO().compSignalTask(taskListId, userId);
    }
}
