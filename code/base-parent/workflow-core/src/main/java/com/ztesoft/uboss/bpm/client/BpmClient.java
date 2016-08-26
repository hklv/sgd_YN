package com.ztesoft.uboss.bpm.client;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.runtime.BpmServiceImpl;
import com.ztesoft.uboss.bpm.runtime.beans.FlowActivity;
import com.ztesoft.uboss.bpm.runtime.beans.ProcessBackInfo;
import com.ztesoft.uboss.bpm.runtime.beans.TaskListInfo;
import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.uboss.bpm.runtime.dao.IBpmRunTimeDAO;
import com.ztesoft.uboss.bpm.runtime.dao.mysqlimpl.BpmRunTimeDAOMySQL;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskHolderEntity;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.ProcessTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmProcessVarDto;
import com.ztesoft.uboss.bpm.setup.model.HolderNoIdDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessDefView;
import com.ztesoft.uboss.bpm.setup.model.ProcessTemplateVersion;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.AssertUtil;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.uboss.model.ZFlowDef;
import utils.UbossActionSupport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BpmClient extends UbossActionSupport {

    private static final ZSmartLogger logger = ZSmartLogger.getLogger(BpmClient.class);

    /**
     * 查询所有的可用的流程版本
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryAllProcessDef(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        BoHelper.listDtoToBO("PROCESS_DEF_LIST",
                flowManagerDao.qryAllProcessDef(), ProcessDefView.class, dict);

        return 0;
    }

    /**
     * 启动流程
     *
     * @param dict
     * @throws BaseAppException
     */
    public int startProcess(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {
        logger.info("BpmClient startProcess...........");

        String definitionKey = dict.getString("PROC_DEF_ID");
        if (definitionKey == null) {
            definitionKey = dict.getString("PROC_DEF_KEY");
        }
        DynamicDict variablesDict = dict.getBO("VARIABLES");

        Map<String, Object> variables = new HashMap<String, Object>();

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        if (uboss_session != null) {

            variables.put(ZFlowDef.BPM_HOLDER_USER_ID,
                    uboss_session.getLong("user-id"));
        }

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        // 查询流程变量
        List<BpmProcessVarDto> varList = flowManagerDao
                .qryProcessVarDefByProcKey(definitionKey);

        for (BpmProcessVarDto bpmProcessVarDto : varList) {
            String varType = bpmProcessVarDto.getVarType();
            String defaultValue = bpmProcessVarDto.getDefaultValue();

            if (defaultValue == null) {
                continue;
            }
            if ("String".equals(varType)) {
                variables.put(bpmProcessVarDto.getVarName(), defaultValue);
            } else if ("Long".equals(varType)) {
                variables.put(bpmProcessVarDto.getVarName(),
                        Long.valueOf(defaultValue));
            } else if ("Date".equals(varType)) {
                String[] dateValue = defaultValue.split(" ");
                if (dateValue != null && dateValue.length == 1) {
                    defaultValue += " 00:00:00";
                }
                variables.put(bpmProcessVarDto.getVarName(), DateUtil
                        .string2SQLDate(defaultValue,
                                DateUtil.DATETIME_FORMAT_1));
            } else if ("Float".equals(varType) || "Double".equals(varType)) {
                variables.put(bpmProcessVarDto.getVarName(),
                        Double.valueOf(defaultValue));
            } else if ("Boolean".equals(varType)) {
                variables.put(bpmProcessVarDto.getVarName(),
                        Boolean.valueOf(defaultValue));
            }
        }

        if (variablesDict != null) {
            HashMap<String, Object> map = variablesDict.valueMap;

            Iterator<Map.Entry<String, Object>> iterator = map.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                Object obj = entry.getValue();
                if (obj == null) {
                    continue;
                } else {
                    variables.put(entry.getKey(), obj);
                }
            }
        }

        String processInstanceId = BpmServiceImpl.getInstance().startProcess(
                definitionKey, variables);
        logger.info("startProcess Successful.......");
        if (StringUtil.isNotEmpty(processInstanceId)) {

            HolderNoIdDto holderNoIdDto = flowManagerDao
                    .qryProcessHolderNum(processInstanceId);
            dict.set("PROCESS_HOLDER_NO", holderNoIdDto != null ? holderNoIdDto.getHolderNo() : null);
            dict.set("PROCESS_HOLDER_ID", holderNoIdDto != null ? holderNoIdDto.getHolderId() : null);
        }

        dict.set("PROCESS_INSTANCE_ID", processInstanceId);

        List<DynamicDict> taskList = BpmServiceImpl.getInstance()
                .getNextActivityTask(processInstanceId);
        if (taskList != null) {
            dict.set("TASK_LIST", taskList);
        }
        return 0;
    }

    /**
     * 启动流程
     *
     * @param dict
     * @throws BaseAppException
     */
    public int startProcessByBizKey(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        String bizKey = dict.getString("PROC_BIZ_KEY");

        IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);

        ProcessTemplateVersion processTemplateVersion = processTemplateDAO
                .selActiveProcessVer(bizKey);

        if (processTemplateVersion == null) {
            ExceptionHandler
                    .publish("System Can Not Found A Active Flow Version, Flow BizKey:"
                            + bizKey);
        }

        dict.set("PROC_DEF_ID", processTemplateVersion.getProcDefId());

        startProcess(dict);

        return 0;
    }

    /**
     * 查询流程定义环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcessDefAct(DynamicDict dict) throws BaseAppException {

        List<FlowActivity> acts = BpmServiceImpl.getInstance()
                .qryProcessDefAct(dict.getString("PROC_BIZ_KEY"));

        BoHelper.listDtoToBO("ACTIVITY_LIST", acts, FlowActivity.class, dict);

        return 0;
    }

    /**
     * 查询流程定义某一个环节之前的环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcDefBeforeAct(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);

        ProcessTemplateVersion processTemplateVersion = processTemplateDAO
                .selActiveProcessVer(dict.getString("PROC_BIZ_KEY"));

        String procDefId = processTemplateDAO
                .selProcdefId(processTemplateVersion.getProcDefId());

        List<FlowActivity> acts = BpmServiceImpl.getInstance()
                .findBeforeFlowActivity(procDefId,
                        dict.getString("CURR_ACT_ID"));

        BoHelper.listDtoToBO("ACTIVITY_LIST", acts, FlowActivity.class, dict);

        return 0;
    }

    /**
     * 查询流程定义某一个环节之后的环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcDefAfterAct(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        String holderId = dict.getString("HOLDER_ID");

        AssertUtil.isNotNull(holderId, "HOLDER_ID cannot be null");

        String currCurrActId = null;
        String procInstanceId = null;

        String taskListId = dict.getString("TASK_LIST_ID");
        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        TaskListEntity taskListEntity = taskListDAO
                .selectTaskListDetail(taskListId);

        if (taskListEntity == null) {

            ExceptionHandler
                    .publish("system can not find this taskList, [TASK_LIST_ID]:"
                            + taskListId);
        } else {

            currCurrActId = taskListEntity.getActId();
            procInstanceId = taskListEntity.getProcInstId();
        }

        List<FlowActivity> acts = BpmServiceImpl.getInstance()
                .findAfterFlowActivity(procInstanceId, currCurrActId);

        BoHelper.listDtoToBO("ACTIVITY_LIST", acts, FlowActivity.class, dict);

        return 0;
    }

    /**
     * 终止流程
     *
     * @param dict
     * @throws BaseAppException
     */
    public void terminateProcess(DynamicDict dict) throws BaseAppException {

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        String userName = null;
        if (uboss_session != null) {

            userName = uboss_session == null ? null : uboss_session
                    .getString("user-name");
        }

        String holderId = dict.getString("HOLDER_ID");
        String reason = dict.getString("REASON");
        BpmServiceImpl.getInstance().terminateProcess(holderId, userName,
                reason);
    }

    /**
     * 挂起流程
     *
     * @param dict
     * @throws BaseAppException
     */
    public void suspendProcess(DynamicDict dict) throws BaseAppException {

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        String userName = null;
        if (uboss_session != null) {

            userName = uboss_session == null ? null : uboss_session
                    .getString("user-name");
        }

        String holderId = dict.getString("HOLDER_ID");
        String reason = dict.getString("REASON");
        BpmServiceImpl.getInstance().suspendProcessByHolderId(holderId,
                userName, reason);
    }

    /**
     * 接挂流程
     *
     * @param dict
     * @throws BaseAppException
     */
    public void unSuspendProcess(DynamicDict dict) throws BaseAppException {

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        String userName = null;
        if (uboss_session != null) {

            userName = uboss_session == null ? null : uboss_session
                    .getString("user-name");
        }

        String holderId = dict.getString("HOLDER_ID");
        String reason = dict.getString("REASON");
        BpmServiceImpl.getInstance().unSuspendProcessByHolderId(holderId,
                userName, reason);
    }

    /**
     * 查询流程轨迹
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcessTrack(DynamicDict dict) throws BaseAppException {
        String processInstanceId = dict.getString("PROCESS_INSTANCE_ID");

        if (StringUtil.isEmpty(processInstanceId)) {
            String holderId = dict.getString("HOLDER_ID");

            if (StringUtil.isEmpty(holderId)) {

                String holderNo = dict.getString("HOLDER_NO");
                List<TaskListInfo> taskList = BpmServiceImpl.getInstance()
                        .qryProcessTrackByHolderId(holderNo);

                BoHelper.listDtoToBO("TASK_LIST", taskList, TaskListInfo.class,
                        dict);
            } else {

                List<TaskListInfo> taskList = BpmServiceImpl.getInstance()
                        .qryProcessTrackByHolderId(holderId);

                BoHelper.listDtoToBO("TASK_LIST", taskList, TaskListInfo.class,
                        dict);
            }
        } else {
            List<TaskListInfo> taskList = BpmServiceImpl.getInstance()
                    .qryProcessTrack(processInstanceId);

            BoHelper.listDtoToBO("TASK_LIST", taskList, TaskListInfo.class,
                    dict);
        }

        return 0;
    }

    /**
     * 查询流程变量
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcessVar(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {
        String varName = dict.getString("VAR_NAME");
        String processInstanceId = dict.getString("PROCESS_INSTANCE_ID");

        if (StringUtil.isEmpty(processInstanceId)) {
            String holderId = dict.getString("HOLDER_ID");

            AssertUtil.isNotNull(holderId, "HOLDER_ID cannot be null");

            IBpmRunTimeDAO bpmRunTimeDAO = SgdDaoFactory.getDaoImpl(BpmRunTimeDAOMySQL.class);
            processInstanceId = bpmRunTimeDAO.qryProcInstIdByHolderId(holderId);
        }

        List<DynamicDict> varList = BpmServiceImpl.getInstance().qryProcessVar(
                processInstanceId, varName);
        dict.valueMap.clear();
        dict.set("VAR_LIST", varList);
        return 0;
    }

    /**
     * 修改流程变量
     *
     * @throws Exception
     */
    public void updateProcessVar(DynamicDict bo) throws Exception {

        List<DynamicDict> variablesDict = bo.getList("varMap");
        System.out.println(variablesDict.toString());
        String pid = variablesDict.get(0).getString("processInstanceId");
        Map<String, String> variables = new HashMap<String, String>();
        for (int i = 0; i < variablesDict.size(); i++) {
            variables.put(variablesDict.get(i).getString("varName"),
                    variablesDict.get(i).getString("Value"));
        }
        this.setVar(pid, variables);
    }

    public void setVar(String id, Map<String, String> variables)
            throws Exception {
        Task task = null;
        TaskService taskService = ProcessServiceManager.getManager()
                .getServiceProvider().getTaskService();
        BpmServiceImpl serviceimpl = BpmServiceImpl.getInstance();
        // task
        // =taskService.createTaskQuery().orderByProcessInstanceId().processInstanceId(id).singleResult();
        RuntimeService r = ProcessServiceManager.getManager()
                .getServiceProvider().getRuntimeService();
        String eid = r.createExecutionQuery().processInstanceId(id)
                .singleResult().getId();
        r.setVariable(eid, "type", "2");
        List<DynamicDict> bos = serviceimpl.getNextActivityTask(id);
    }

    public static void main(String[] args) throws Exception {
        String id = "122510";
        Map<String, Object> va = new HashMap<String, Object>();
        va.put("type", 4);
        BpmClient b = new BpmClient();
        // b.setVar(id,va);
    }

    /**
     * 查询流程环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcessAct(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {
        String processInstanceId = dict.getString("PROCESS_INSTANCE_ID");

        if (StringUtil.isEmpty(processInstanceId)) {
            String holderId = dict.getString("HOLDER_ID");

            AssertUtil.isNotNull(holderId, "HOLDER_ID cannot be null");

            IBpmRunTimeDAO bpmRunTimeDAO = SgdDaoFactory.getDaoImpl(BpmRunTimeDAOMySQL.class);
            processInstanceId = bpmRunTimeDAO.qryProcInstIdByHolderId(holderId);
        }

        String taskListId = dict.getString("TASK_LIST_ID");
        List<FlowActivity> activityList = BpmServiceImpl.getInstance()
                .qryProcessAct(processInstanceId, taskListId);

        BoHelper.listDtoToBO("ACTIVITY_LIST", activityList, FlowActivity.class,
                dict);

        return 0;
    }

    /**
     * 查询所有仿真的流程版本
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryAllSimProcessVersion(DynamicDict dict)
            throws BaseAppException, InstantiationException, IllegalAccessException {

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        long count = flowManagerDao.qryAllSimProcessVersionCount(dict);

        if (count > 0) {
            BoHelper.listDtoToBO("PROCESS_DEF_LIST",
                    flowManagerDao.qryAllSimProcessVersion(dict),
                    ProcessDefView.class, dict);
        }

        dict.set("PROCESS_DEF_LIST_COUNT", count);

        return 0;
    }

    /**
     * 流程回撤
     *
     * @param dict
     * @throws BaseAppException
     */
    public int backProcess(DynamicDict dict) throws BaseAppException {

        ProcessBackInfo processBackInfo = (ProcessBackInfo) BoHelper.boToDto(dict,
                ProcessBackInfo.class);

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        processBackInfo.setUserId(uboss_session.getLong("user-id"));
        processBackInfo.setBackUser(uboss_session.getString("user-name"));

        List<DynamicDict> taskList = BpmServiceImpl.getInstance().backProcess(
                processBackInfo);

        if (taskList != null) {
            dict.set("TASK_LIST", taskList);
        }

        return 0;
    }

    /**
     * 任务回退
     *
     * @param dict
     * @throws BaseAppException
     */
    public int backTask(DynamicDict dict) throws BaseAppException {

        ProcessBackInfo processBackInfo = (ProcessBackInfo) BoHelper.boToDto(dict,
                ProcessBackInfo.class);

        DynamicDict uboss_session = dict.getBO("zsmart_session");
        processBackInfo.setUserId(uboss_session.getLong("user-id"));
        processBackInfo.setBackUser(uboss_session.getString("user-name"));

        List<DynamicDict> taskList = BpmServiceImpl.getInstance().backTask(
                processBackInfo);

        if (taskList != null) {
            dict.set("TASK_LIST", taskList);
        }

        return 0;
    }

    /**
     * 查询上一个环节
     *
     * @param dict
     * @throws BaseAppException
     */
    public int queryPrevActvities(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {

        String taskListId = dict.getString("TASK_LIST_ID");
        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);
        TaskListEntity taskListEntity = taskListDAO
                .selectTaskListDetail(taskListId);

        if (taskListEntity == null) {

            ExceptionHandler
                    .publish("system can not find this taskList, [TASK_LIST_ID]:"
                            + taskListId);
        } else {

            String currActId = taskListEntity.getActId();
            String procInstanceId = taskListEntity.getProcInstId();

            List<FlowActivity> acts = BpmServiceImpl.getInstance()
                    .findPreOneActs(currActId, procInstanceId);

            BoHelper.listDtoToBO("PRE_ACTS", acts, FlowActivity.class, dict);
        }

        return 0;
    }

    /**
     * 查询流程变量
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryFlowRuntimeVar(DynamicDict dict) throws BaseAppException {

        String holderId = dict.getString("HOLDER_ID");

        Map<String, Object> vars = BpmServiceImpl.getInstance()
                .qryProcessRtVar(holderId);
        Iterator<Map.Entry<String, Object>> iterator = vars.entrySet()
                .iterator();

        while (iterator.hasNext()) {

            Map.Entry<String, Object> entry = iterator.next();
            dict.set(entry.getKey(), entry.getValue());
        }

        return 0;
    }

    /**
     * 查询流程
     *
     * @param dict
     * @throws BaseAppException
     */
    public int qryProcess(DynamicDict dict) throws BaseAppException, InstantiationException, IllegalAccessException {
        String holderId = dict.getString("HOLDER_ID");

        if (StringUtil.isNotEmpty(holderId)) {

            ITaskHolderDAO dao = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
            TaskHolderEntity entry = dao.select(holderId);

            BoHelper.dtoToBO(entry, dict);
        }

        return 0;
    }

    /**
     * 查询流程的模板详情 by procDefID author : L .
     *
     * @param bo
     * @throws BaseAppException
     */
    public void selDeployId(DynamicDict bo) throws BaseAppException, InstantiationException, IllegalAccessException {
        String id = bo.getString("pro_def_id");
        IProcessTemplateDAO dao = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
        BoHelper.dtoToBO(dao.selDeployId(id), bo);
    }

    public void setVar() throws Exception {

    }
}
