package com.ztesoft.uboss.bpm.taskcenter.services;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.sgd.base.helper.LoginUserHolder;
import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.ProcessTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.bll.WorkItemManager;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.IWorkItemManagerDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.WorkItemManagerDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.uboss.model.BpmTaskAssginDto;
import utils.UbossActionSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务中心操作类
 *
 * @author LiybC
 */
public class WorkItemService extends UbossActionSupport {

    private ZSmartLogger logger = ZSmartLogger.getLogger(WorkItemService.class);

    /**
     * 签出任务
     *
     * @param dict
     * @throws BaseAppException
     */
    public void claimTask(DynamicDict dict) throws BaseAppException {
        String taskListId = dict.getString("TASK_LIST_ID", true);
        DynamicDict uboss_session = dict.getBO("zsmart_session");
        Long userId = uboss_session.getLong("user-id");
        WorkItemManager workItemManager = new WorkItemManager();
        workItemManager.claimTask(taskListId, userId);
    }

    /**
     * 提交任务
     *
     * @param dict
     * @throws BaseAppException
     */
    public void completeTask(DynamicDict dict) throws BaseAppException {
        String taskListId = dict.getString("TASK_LIST_ID", true);
        //edit by liuhao 2014 9.2
        DynamicDict bo = dict.getBO("GVAR");
        Map<String, Object> gvar = dict.getBO("GVAR") == null ? null : dict
                .getBO("GVAR").valueMap;
        Map<String, Object> localVar = dict.getBO("LVAR") == null ? null : dict
                .getBO("LVAR").valueMap;

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        Long userId = uboss_session.getLong("user-id");
        WorkItemManager workItemManager = new WorkItemManager();
        List<DynamicDict> ret = workItemManager.completeTask(taskListId,
                userId, localVar, gvar);
        dict.set("TASK_LIST", ret);

    }

    /**
     * 查询任务单列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTaskHolderList(DynamicDict bo) throws BaseAppException {
        WorkItemManager manager = new WorkItemManager();
        manager.qryTaskHolderList(bo);
    }

    /**
     * 查询任务列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTaskList(DynamicDict bo) throws BaseAppException {
        logger.info("logger info call qryTaskList  service...");
        WorkItemManager manager = new WorkItemManager();
        manager.qryTaskList(bo);
    }

    /**
     * 根据任务单查询工单
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTaskListByHolder(DynamicDict bo) throws BaseAppException {
        logger.info("call qryTaskListByHolder  service...");
        WorkItemManager manager = new WorkItemManager();
        manager.qryTaskListByHolder(bo);
    }

    /**
     * 查询任务详情
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTaskDetai(DynamicDict bo) throws BaseAppException {
        logger.info("call qryTaskDetail  service...");
        WorkItemManager manager = new WorkItemManager();
        manager.qryTaskDetail(bo);
    }

    public void qryabnoramlTaskList(DynamicDict bo) throws BaseAppException {
        logger.info("logger info call qryTaskList  service...");

        IWorkItemManagerDAO workItemManagerDAO = SgdDaoFactory.getDaoImpl(WorkItemManagerDAOMySQL.class);
        List<DynamicDict> list = workItemManagerDAO.qryAbnoramlTaskList(bo);

        bo.set("taskList", list);
    }

    /**
     * 重新执行系统任务
     *
     * @param dict
     * @throws BaseAppException
     */
    public int reDoSysTask(DynamicDict dict) throws BaseAppException {

        String executionId = dict.getString("EXECUTION_ID");
        String taskListId = dict.getString("TASK_LIST_ID");

        boolean res = ProcessServiceManager.getManager().getRuntimeService()
                .executeExceptionServiceTask(executionId, taskListId);

        if (res) {
            dict.set("RES_STR", "re process sucesssful");
        } else {
            dict.set("RES_STR", "re process fail");
        }

        return 0;
    }

    /**
     * 任务跳转
     *
     * @param dict
     * @throws BaseAppException
     */
    public void jumpTask(DynamicDict dict) throws BaseAppException {
        DynamicDict zsmart_session = dict.getBO("zsmart_session");
        Long userId = LoginUserHolder.get();
        if (zsmart_session != null) {
            userId = zsmart_session.getLong("user-id");
        }
        WorkItemManager manager = new WorkItemManager();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("JUMP_REASON", dict.get("JUMP_REASON"));
        variables.put("zsmart_session", zsmart_session);

        if (StringUtil.isNotEmpty(dict.getString("NEXT_ACTIVITY_ID"))) {

            List<DynamicDict> ret = manager.jumpTask(dict.getString("TASK_LIST_ID"),
                    dict.getString("NEXT_ACTIVITY_ID"), userId, variables);

            dict.set("TASK_LIST", ret);
        } else {

            List<DynamicDict> ret = manager.jumpTaskByTaskTemplate(dict.getString("TASK_LIST_ID"),
                    dict.getString("NEXT_TASK_TEMPLATE_ID"), userId, variables);

            dict.set("TASK_LIST", ret);
        }
    }

    /**
     * 任务回退
     *
     * @param dict
     * @throws BaseAppException
     */
    public int backUserTask(DynamicDict dict) throws BaseAppException {

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        Long userId = uboss_session.getLong("user-id");

        WorkItemManager manager = new WorkItemManager();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("BACK_REASON", dict.get("BACK_REASON"));
        List<DynamicDict> ret = manager.backUserTask(dict.getString("TASK_LIST_ID"),
                dict.getString("BACK_TASK_LIST_ID"), userId, variables);

        dict.set("TASK_LIST", ret);

        return 0;
    }

    /**
     * 增派
     *
     * @param dict
     * @throws BaseAppException
     */
    public int addSubTask(DynamicDict dict) throws BaseAppException {
        WorkItemManager manager = new WorkItemManager();
        TaskListEntity subTaskListEntity = (TaskListEntity) BoHelper.boToDto(
                dict.getBO("SUB_TASK"), TaskListEntity.class);
        manager.addSubTask(dict.getString("TASK_LIST_ID"), subTaskListEntity);

        return 0;
    }

    /**
     * 转单
     *
     * @param dict
     * @throws BaseAppException
     */
    public int forwardTask(DynamicDict dict) throws BaseAppException {
        WorkItemManager manager = new WorkItemManager();

        BpmTaskAssginDto assgin = (BpmTaskAssginDto) BoHelper.boToDto(dict.getBO("ASSGIN_IFNO"),
                BpmTaskAssginDto.class);
        String taskListId = dict.getString("TASK_LIST_ID");
        String forwardReason = dict.getString("FORWARD_REASON");

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        Long userId = uboss_session.getLong("user-id");

        manager.forwardTask(taskListId, assgin, forwardReason, userId);

        return 0;
    }

    /**
     * 提交环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int submitTaskTemplate(DynamicDict dict) throws BaseAppException {

        String taskTemplateCode = dict.getString("TASK_TEMPLATE_CODE");
        String taskHolderId = dict.getString("HOLDER_ID");

        if (StringUtil.isNotEmpty(taskHolderId)) {
            ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
            String taskListId = taskListDAO.qryTaskListByHolderId(taskHolderId, taskTemplateCode);

            dict.set("TASK_LIST_ID", taskListId);
        } else {
            String taskHolderNo = dict.getString("HOLDER_NO");

            ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
            String taskListId = taskListDAO.qryTaskListByHolderNo(taskHolderNo, taskTemplateCode);

            dict.set("TASK_LIST_ID", taskListId);
        }

        completeTask(dict);

        return 0;
    }

    /**
     * 查询流程当前停留的环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcessCurrTaskTemplate(DynamicDict dict) throws BaseAppException {

        String taskHolderId = dict.getString("HOLDER_ID");

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        List<String> templateIds = new ArrayList<String>();

        if (StringUtil.isNotEmpty(taskHolderId)) {

            templateIds = taskListDAO.qryTaskListTemplateId(taskHolderId);
        } else {

            String taskHolderNo = dict.getString("HOLDER_NO");
            templateIds = taskListDAO.qryTaskListTemplateIdByNo(taskHolderNo);
        }

        if (templateIds.size() == 1) {
            ArrayList alObj = new ArrayList();
            alObj.add(templateIds.get(0));
            dict.valueMap.put("TEMPLATE_IDS", alObj);
        } else {
            for (String templateId : templateIds) {
                dict.add("TEMPLATE_IDS", templateId);
            }
        }

        return 0;
    }

    /**
     * 查询任务回退原因以及设置
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryTaskBackCfgInfo(DynamicDict dict) throws BaseAppException {

        String taskListId = dict.getString("TASK_LIST_ID");

        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        if (StringUtil.isNotEmpty(taskListId)) {

            TaskListEntity taskList = taskListDAO.selectTaskListDetail(taskListId);

            if (taskList == null) {

                ExceptionHandler.publish("tasklist can not be found, TASK_LIST_ID:" + taskListId);
            }

            String holderId = taskList.getHolderId();
            IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
            String procDefId = processTemplateDAO.selProcDefId(holderId);
            List<TaskBackReasonInfo> backReasonInfos = processTemplateDAO.selProcBackCfg(taskList.getTemplateId(), procDefId);

            BoHelper.listDtoToBO("BACK_REASON_LIST", backReasonInfos, TaskBackReasonInfo.class, dict);
        }

        return 0;
    }
}
