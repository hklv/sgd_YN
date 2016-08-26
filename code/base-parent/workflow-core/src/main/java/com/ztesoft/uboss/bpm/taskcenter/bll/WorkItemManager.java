package com.ztesoft.uboss.bpm.taskcenter.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.runtime.BpmServiceImpl;
import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.uboss.bpm.runtime.constant.TaskListConstant;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmBackHolderDto;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.IWorkItemManagerDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.WorkItemManagerDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.model.HolderQueryCondition;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.uboss.bpm.utils.WorkDayUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.pot.bll.RoleManager;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.uboss.model.BpmTaskAssginDto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 工单相关操作
 *
 * @author LiybC
 */
public class WorkItemManager {
    private ZSmartLogger logger = ZSmartLogger.getLogger(WorkItemManager.class);
    private static BpmServiceImpl serviceimpl = BpmServiceImpl.getInstance();

    /**
     * 签出任务
     *
     * @param taskListId
     * @param userId
     */
    public void claimTask(String taskListId, Long userId)
            throws BaseAppException {

        getFlowManagerDAO().modTaskState_A(userId, taskListId);

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        TaskListEntity taskListEntity = taskListDAO
                .selectTaskListDetail(taskListId);

        // 若任务模板或者上一个环节/会签没有指定处理人,那么第一个签出任务单的人为任务单的拥有者
        if (taskListEntity.getOwner() == null) {
            taskListDAO.updateTaskListOwner(taskListId, userId);
        }
    }

    /**
     * 提交任务
     *
     * @param taskListId
     * @param userId
     * @throws BaseAppException
     */
    public List<DynamicDict> completeTask(String taskListId, Long userId,
                                          Map<String, Object> lvariables, Map<String, Object> gvariables)
            throws BaseAppException {

        String nextTaskId = null;

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        TaskListEntity taskList = taskListDAO.selectTaskListDetail(taskListId);

        checkTaskList(taskList, taskListId);

        //服务任务
        if (TaskListConstant.TYPE_SERVICE.state().equals(taskList.getTaskType())) {

            ProcessServiceManager.getManager().getRuntimeService()
                    .executeSingalServiceTask(taskList.getProcInstId(), taskList.getExecutionId(), taskListId, userId);

        } else {//人工任务

            //反向工单
            if (TaskListConstant.DIRECTION_BACK.state().equals(taskList.getDirection())) {

                lvariables.put("direction", TaskListConstant.DIRECTION_BACK.state());

                IFlowManagerDAO flowDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
                BpmBackHolderDto backDto = flowDao.selBackHolder(taskList.getHolderId());

                if (backDto != null) {

                    //最后一个反向工单
                    if (backDto.getTagActId().equals(taskList.getActId())) {

                        lvariables.put("directionAct", "Last");
                    }
                }
            }

            String taskId = taskList.getTaskId();

            IFlowManagerDAO flowManagerDao = getFlowManagerDAO();

            // 标记tasklist状态为completed
            if (lvariables != null) {
                Object taskResult = lvariables.get("TASK_RESULT");
                if (taskResult == null) {
                    taskResult = "";
                }
                flowManagerDao.modTaskState_C(taskListId,
                        (Date) lvariables.get("COMPLETE_DATE"),
                        (String) taskResult, userId);
            } else {
                flowManagerDao.modTaskState_C(taskListId, null, "", userId);
            }

            // 主单
            if (StringUtil.isEmpty(taskList.getParentTaskId())) {

                nextTaskId = completeMainTask(taskListId, userId, taskId, lvariables, gvariables);
            }
            // 子单
            else {

                nextTaskId = completeSubTask(taskListId, userId, taskList.getParentTaskId(), lvariables, gvariables);
            }
        }

        List<DynamicDict> list = new ArrayList<DynamicDict>();
        logger.debug("nextTaskIds = " + nextTaskId);
        if (nextTaskId != null) {
            String[] ids = nextTaskId.split(",");
            for (String id : ids) {
                list.addAll(getWorkItemDao().qryTaskListByTaskId(id));
            }
        }

        return list;
    }

    private String completeMainTask(String taskListId, Long userId, String taskId,
                                    Map<String, Object> lvariables, Map<String, Object> gvariables)
            throws BaseAppException {

        String nextTaskId = null;

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        // 先判断所有未完成的子单是否完成
        List<TaskListEntity> subTaskLists = taskListDAO.selectSubTaskList(taskListId);

        // 所有子单完成才通知引擎的环节往下走
        if (subTaskLists.size() == 0) {
            // 通知引擎完成任务
            if (serviceimpl.isTasktExist(taskId)) {
                ProcessServiceManager.getManager().getServiceProvider()
                        .getTaskService()
                        .setVariablesLocal(taskId, lvariables);
                nextTaskId = serviceimpl.completeTask(taskId, gvariables);
            }
        }

        return nextTaskId;
    }

    private String completeSubTask(String taskListId, Long userId, String parentTaskId,
                                   Map<String, Object> lvariables, Map<String, Object> gvariables)
            throws BaseAppException {

        String nextTaskId = null;
        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        // 先判断所有未完成的子单是否完成
        List<TaskListEntity> subTaskLists = taskListDAO.selectSubTaskList(parentTaskId);

        // 所有子单完成才通知引擎的环节往下走
        if (subTaskLists.size() == 0) {
            TaskListEntity parentTask = taskListDAO.selectTaskListDetail(parentTaskId);

            if (TaskListConstant.COMPLETED.state().equals(
                    parentTask.getState())) {
                String taskId = parentTask.getTaskId();

                // 通知引擎完成任务
                if (serviceimpl.isTasktExist(taskId)) {
                    ProcessServiceManager.getManager().getServiceProvider()
                            .getTaskService()
                            .setVariablesLocal(taskId, lvariables);
                    nextTaskId = serviceimpl.completeTask(taskId, gvariables);
                }
            }
        }

        return nextTaskId;
    }

    private void checkTaskList(TaskListEntity taskList, String taskListId) throws BaseAppException {

        if (taskList == null) {

            ExceptionHandler.publish("system can not find this taskList, taskListId:" + taskListId);
        } else if (TaskListConstant.COMPLETED.state().equals(taskList.getState())) {

            ExceptionHandler.publish("this taskList has been completed, taskListId:" + taskListId);
        } else if (TaskListConstant.JUMP.state().equals(taskList.getState())) {

            ExceptionHandler.publish("this taskList has been jumped, taskListId:" + taskListId);
        } else if (TaskListConstant.DELETE.state().equals(taskList.getState())) {

            ExceptionHandler.publish("this taskList has been deleted, taskListId:" + taskListId);
        } else if (TaskListConstant.BACK.state().equals(taskList.getState())) {

            ExceptionHandler.publish("this taskList has been backed, taskListId:" + taskListId);
        }
    }

    /**
     * 自动工单异常时，重新执行(给job用的)
     *
     * @param taskListId
     * @param userId
     * @param variables
     * @throws BaseAppException
     */
    public void reExecuteTask(String taskListId, Long userId,
                              Map<String, Object> variables) throws BaseAppException {
        String executionId = null;
        serviceimpl.reExecuteTask(executionId);

    }

    /**
     * 接挂任务
     *
     * @param taskListId
     * @throws ActivitiException
     * @throws BaseAppException
     */
    public void resumeTask(String taskListId) throws ActivitiException,
            BaseAppException {
        getFlowManagerDAO().modTaskState_A(taskListId);
    }

    /**
     * 挂起任务
     *
     * @param taskListId
     * @param variables
     * @throws ActivitiException
     * @throws BaseAppException
     */
    public void suspendTask(String taskListId, Map<String, Object> variables)
            throws ActivitiException, BaseAppException {
        getFlowManagerDAO().modTaskState_H(taskListId,
                (String) variables.get("SUSPEND_REASON"));
    }

    /**
     * 删除任务
     *
     * @param taskListId
     * @param variables
     * @throws ActivitiException
     * @throws BaseAppException
     */
    public void deleteTask(String taskListId, Map<String, Object> variables)
            throws ActivitiException, BaseAppException {

        getFlowManagerDAO().modTaskState_H(taskListId,
                (String) variables.get("DELETE_REASON"));

    }

    /**
     * 查询工单列表
     *
     * @param bo
     * @return
     * @throws BaseAppException
     */
    public void qryTaskList(DynamicDict bo) throws BaseAppException {
        HolderQueryCondition condition = (HolderQueryCondition) BoHelper.boToDto(bo,
                HolderQueryCondition.class);
        DynamicDict uboss_session = bo.getBO("zsmart_session");

        // 查委托任务
        if ("Y".equals(bo.getString("PROXY_FLAG"))) {
            //add by hklv solve nullpointer
            if (uboss_session != null) {
               /* List<Long> proxyUserId = getWorkItemDao().qryProxyUser(
                        uboss_session.getLong("user-id"));*/
                //TODO by HKLv portal中的User无proxy_user_id字段
                List<Long> proxyUserId = null;
                if (proxyUserId != null && !proxyUserId.isEmpty()) {
                    bo.set("PROXY_USER", proxyUserId);
                    //edit by liuhao ;
                    RoleManager roleMgr = new RoleManager();
//                    List<Long> proxyUserRoleId = roleMgr.selAllRole4LinkUserIdList(proxyUserId);
//                    bo.set("PROXY_USER_ROLE", proxyUserRoleId);
                    //end
                } else {
                    bo.set("taskList", new ArrayList<DynamicDict>());
                    return;
                }
            }
        }

        List<DynamicDict> list = getWorkItemDao().qryTaskList(bo, condition,
                uboss_session);
        bo.set("taskList", list);
    }

    /**
     * 查询流程单列表
     *
     * @param bo
     * @return
     * @throws BaseAppException
     */
    public void qryTaskHolderList(DynamicDict bo) throws BaseAppException {
        HolderQueryCondition condition = (HolderQueryCondition) BoHelper.boToDto(bo,
                HolderQueryCondition.class);
        //edit by liuhao  1118
        if (bo.getString("HOLDER_NO") != null && bo.getString("HOLDER_NO").length() > 0) {
            bo.set("HOLDER_NO", "%" + bo.getString("HOLDER_NO") + "%");
        }
        List<DynamicDict> list = getWorkItemDao().qryHolderList(bo, condition);
        if (list != null && list.size() > 0) {
            java.util.Date now = new java.util.Date();
            for (DynamicDict holder : list) {
                // 判断是否是完成或者废弃的
                if ("C".equalsIgnoreCase(holder.getString("HOLDER_STATE"))
                        || "E".equalsIgnoreCase(holder
                        .getString("HOLDER_STATE"))) {
                    continue;
                } else {
                    Long overTime = holder.getLong("OVER_TIME");
                    Date startDate = holder.getDate("START_TIME");
                    int workDays = WorkDayUtil.getWorkDays(startDate, now); // 判断流程单开始到今天的工作日天数
                    if (overTime == null) {
                        holder.set("OVER", false);
                    } else if (workDays > overTime) {
                        holder.set("OVER", true);
                    } else if (workDays <= overTime) {
                        holder.set("OVER", false);
                    }
                }

            }

        }
        bo.set("holderList", list);
    }

    /**
     * 根据流程单查询工单
     *
     * @param bo
     * @return
     * @throws BaseAppException
     */
    public void qryTaskListByHolder(DynamicDict bo) throws BaseAppException {
        IWorkItemManagerDAO dao = getWorkItemDao();

        List<DynamicDict> list = dao.qryTaskListByHolder(bo);
        bo.set("taskList", list);
    }

    /**
     * 查询工单详情
     *
     * @param bo
     * @return
     * @throws BaseAppException
     */
    public void qryTaskDetail(DynamicDict bo) throws BaseAppException {
        DynamicDict task = getWorkItemDao().selectTaskDetail(bo);
        bo.set("taskDetail", task);
    }

    public List<DynamicDict> jumpTask(String taskListId, String nextActivityId, Long userId,
                                      Map<String, Object> variables) throws BaseAppException {

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        String stateReason = (String) variables.get("JUMP_REASON");
        if (stateReason == null) {
            stateReason = "";
        }
        stateReason = stateReason + ", jump to node:" + nextActivityId;

        // 标记当前任务
        flowManagerDao.modTaskState(taskListId, TaskListConstant.JUMP.state(),
                userId, stateReason);

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        String taskId = taskListDAO.selectTaskId(taskListId);

        String nextTaskId = serviceimpl.jumpTask(taskId, nextActivityId, variables);

        List<DynamicDict> list = new ArrayList<DynamicDict>();
        logger.debug("nextTaskIds = " + nextTaskId);
        if (nextTaskId != null) {
            String[] ids = nextTaskId.split(",");
            for (String id : ids) {
                list.addAll(getWorkItemDao().qryTaskListByTaskId(id));
            }
        }
        return list;
    }

    public List<DynamicDict> jumpTaskByTaskTemplate(String taskListId, String nextTaskTempId, Long userId,
                                                    Map<String, Object> variables) throws BaseAppException {

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        String stateReason = (String) variables.get("JUMP_REASON");
        if (stateReason == null) {
            stateReason = "";
        }
        stateReason = stateReason + ", jump to node:" + nextTaskTempId;

        // 标记当前任务
        if (taskListId != null) {
            flowManagerDao.modTaskState(taskListId, TaskListConstant.JUMP.state(),
                    userId, stateReason);
            ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

            String taskId = taskListDAO.selectTaskId(taskListId);

            String nextTaskId = serviceimpl.jumpTaskByTemplateId(taskId, nextTaskTempId, variables);

            List<DynamicDict> list = new ArrayList<DynamicDict>();
            logger.debug("nextTaskIds = " + nextTaskId);
            if (nextTaskId != null) {
                String[] ids = nextTaskId.split(",");
                for (String id : ids) {
                    list.addAll(getWorkItemDao().qryTaskListByTaskId(id));
                }
            }
            return list;
        }
        return null;


    }

    public List<DynamicDict> backUserTask(String taskListId, String backTaskListId,
                                          Long userId, Map<String, Object> variables) throws BaseAppException {

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        String taskId = taskListDAO.selectTaskId(taskListId);

        // String preActId = backActId;
        String preActId = null;
        // if (StringUtil.isEmpty(preActId)) {
        if (StringUtil.isEmpty(backTaskListId)) {
            // 先去找上一个人工任务活动
            HistoricActivityInstance preHistoricActivityInstance = serviceimpl
                    .findPreUserTask(taskId);
            preActId = preHistoricActivityInstance.getActivityId();
        } else {
            preActId = taskListDAO.qryHisAct(backTaskListId);
        }

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        String stateReason = (String) variables.get("BACK_REASON");
        if (stateReason == null) {
            stateReason = "";
        }
        stateReason = stateReason + ", back to node:" + preActId;

        // 标记当前任务
        flowManagerDao.modTaskState(taskListId, TaskListConstant.JUMP.state(),
                userId, stateReason);

        // 从当前任务活动跳到上一个人工任务活动 即 回退
        String nextTaskId = serviceimpl.jumpTask(taskId, preActId, variables);

        List<DynamicDict> list = new ArrayList<DynamicDict>();
        logger.debug("nextTaskIds = " + nextTaskId);
        if (nextTaskId != null) {
            String[] ids = nextTaskId.split(",");
            for (String id : ids) {
                list.addAll(getWorkItemDao().qryTaskListByTaskId(id));
            }
        }
        return list;
    }

    /**
     * 增派
     *
     * @param taskListId
     * @param subTaskListEntity
     * @throws BaseAppException
     */
    public void addSubTask(String taskListId, TaskListEntity subTaskListEntity)
            throws BaseAppException {

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        TaskListEntity taskListEntity = taskListDAO
                .selectTaskListDetail(taskListId);

        Date now = DateUtil.getNowDate();

		/*subTaskListEntity.setHolderId(taskListEntity.getHolderId());
        subTaskListEntity.setParentTaskId(taskListId);
		subTaskListEntity.setTaskType(taskListEntity.getTaskType());
		subTaskListEntity.setTaskListId(UUID.randomUUID().toString());
		subTaskListEntity.setExecutionId(taskListEntity.getExecutionId());
		subTaskListEntity.setProcInstId(taskListEntity.getProcInstId());
		subTaskListEntity.setState("I");
		subTaskListEntity.setStartTime(now);
		subTaskListEntity.setCreateDate(now);
		subTaskListEntity.setStateDate(now);
		//修改 ＢＹliuhao
		subTaskListEntity.setOwner(subTaskListEntity .getUserId());
		//subTaskListEntity.setUserId(null);
		taskListDAO.save(subTaskListEntity);*/
        /**
         * 修改后的增派 仿转派的写法  by: liuhao  上面写法会将工单加到流程start后面,下面方法
         * 会将工单默认加载到 当前环节之后
         */

        taskListEntity.setTaskListId(UUID.randomUUID().toString());
        taskListEntity.setCreateDate(now);
        taskListEntity.setParentTaskId(taskListId);
        taskListEntity.setOwner(subTaskListEntity.getUserId());
        taskListEntity.setJobId(subTaskListEntity.getJobId());
        taskListEntity.setRoleId(subTaskListEntity.getRoleId());
        taskListEntity.setOrgId(subTaskListEntity.getOrgId());
        //taskListEntity.setState(TaskListConstant.PENDING.state());
        taskListEntity.setState("I");
        taskListEntity.setTaskName(subTaskListEntity.getTaskName());

        taskListEntity.setStartTime(now);
        taskListEntity.setCreateDate(now);
        taskListEntity.setStateDate(now);
        taskListDAO.save(taskListEntity);

    }

    public void forwardTask(String taskListId,
                            BpmTaskAssginDto bpmTaskAssginDto, String forwardReason, Long userId)
            throws BaseAppException {

        validateAssign(bpmTaskAssginDto);
        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        TaskListEntity taskListEntity = taskListDAO
                .selectTaskListDetail(taskListId);
        Date nowDate = DateUtil.getNowDate();

        // 将原单拷贝一份出来
        taskListEntity.setTaskListId(UUID.randomUUID().toString());
        taskListEntity.setCreateDate(nowDate);
        taskListEntity.setOwner(bpmTaskAssginDto.getUserId());
        taskListEntity.setJobId(bpmTaskAssginDto.getJobId());
        taskListEntity.setRoleId(bpmTaskAssginDto.getRoleId());
        taskListEntity.setOrgId(bpmTaskAssginDto.getOrgId());
        taskListEntity.setState(TaskListConstant.PENDING.state());

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        String forwardLog = "转派:" + taskListEntity.getTaskListId();
        if (StringUtil.isNotEmpty(forwardReason)) {
            forwardLog = forwardReason + " " + forwardLog;
        }

        // 将原单完成
        flowManagerDao.modTaskState_C(taskListId, nowDate, forwardLog, userId);

        // 保存新单
        taskListDAO.save(taskListEntity);
    }

    private void validateAssign(BpmTaskAssginDto bpmTaskAssginDto)
            throws BaseAppException {
        if (bpmTaskAssginDto == null
                || (bpmTaskAssginDto.getJobId() == null
                && bpmTaskAssginDto.getUserId() == null
                && bpmTaskAssginDto.getOrgId() == null && bpmTaskAssginDto
                .getRoleId() == null)) {

            ExceptionHandler.publish("没有指定任务处理人");
        }
    }

    private IWorkItemManagerDAO getWorkItemDao() throws BaseAppException {

        return SgdDaoFactory.getDaoImpl(WorkItemManagerDAOMySQL.class);
    }

    private IFlowManagerDAO getFlowManagerDAO() throws BaseAppException {

        return SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
    }
}
