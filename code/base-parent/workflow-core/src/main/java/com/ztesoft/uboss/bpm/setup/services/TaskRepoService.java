package com.ztesoft.uboss.bpm.setup.services;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.setup.bll.TaskRepoManager;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmTaskTemplateDto;
import com.ztesoft.uboss.bpm.setup.model.TaskEventDto;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.uboss.ext.ServiceParamDto;
import utils.UbossActionSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TaskRepoService extends UbossActionSupport {

    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());


    /**
     * 初始化流程目录树
     *
     * @param bo
     * @throws BaseAppException
     */
    public void initCatgTree(DynamicDict bo) throws BaseAppException {
        logger.info("init catg tree service .");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        taskRepoManager.qryFlowTree(bo);
    }

    /**
     * 查询流程目录树
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryCatgTree(DynamicDict bo) throws BaseAppException {
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        taskRepoManager.qryCatgTree(bo);
    }

    /**
     * 查询任务模板列表
     *
     * @param bo 根据流程类型
     * @throws BaseAppException
     */
    public void qryTaskTempList(DynamicDict bo) throws BaseAppException {
        logger.info("query task temp list service.");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();

        long count = taskRepoManager.qryTaskTempListCount(bo);

        if (count > 0) {

            taskRepoManager.qryTaskTempList(bo);
        }
    }

    /**
     * 查询任务模板列表
     *
     * @param bo 根据流程类型
     * @throws BaseAppException
     */
    public void qryPhotoTaskTemplate(DynamicDict bo) throws BaseAppException {
        logger.info("query task temp list service.");

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        List<DynamicDict> taskList = flowManagerDao.qryTaskTempList(bo);
        bo.set("taskList", taskList);
    }

    /**
     * 初始化会签类型列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void initSiginType(DynamicDict bo) throws BaseAppException {
        logger.info("init sigintype service.");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        taskRepoManager.qrySiginTypeList(bo);
    }

    /**
     * 增加或修改任务模板
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addTaskTemp(DynamicDict bo) throws BaseAppException {
        logger.info("addTaskTemp .");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        taskRepoManager.addTaskTemp(bo);
    }

    /**
     * 给流程模板添加关联按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addBtnToTaskTemplate(DynamicDict bo) throws BaseAppException {
        logger.info("addBtnToTaskTemplate .");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        taskRepoManager.delBtnFromTaskTemplate(bo);
        taskRepoManager.addBtnToTaskTemplate(bo);
    }

    /**
     * 查询不跟流程模板关联的按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryBtnListNotInTemplate(DynamicDict bo) throws BaseAppException {
        logger.info("qryButtonListByTask .");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        taskRepoManager.qryBtnListNotInTemplate(bo);
    }


    /**
     * 配置任务模板事件
     *
     * @param bo
     * @throws BaseAppException
     */
    public void confTaskTemplateEvent(DynamicDict bo) throws BaseAppException {
        logger.info("confTaskTemplateEvent .");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();
        List<TaskEventDto> events = new ArrayList<TaskEventDto>();
        List<Object> objects = BoHelper.boToListDto(bo, "TASK_TEMPLATE_EVENTS", TaskEventDto.class);
        for (Object o : objects) {
            events.add((TaskEventDto) o);
        }
        taskRepoManager.confTaskTemplateEvent(bo.getLong("TASK_TEMPLATE_ID"), events);
    }

    /**
     * 查询配置任务模板事件
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTaskTemplateEvent(DynamicDict bo) throws BaseAppException {
        logger.info("confTaskTemplateEvent .");
        TaskRepoManager taskRepoManager = TaskRepoManager.getInstance();

        BoHelper.listDtoToBO("TASK_TEMPLATE_EVENTS", taskRepoManager.qryTaskTemplateEvent(bo.getLong("TASK_TEMPLATE_ID")), TaskEventDto.class, bo);
    }

    /**
     * 查询配置任务模板回退原因
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTaskTemplateBackReason(DynamicDict bo) throws BaseAppException {
        logger.info("qryTaskTemplateBackReason .");

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        List<TaskBackReasonInfo> backReasons = taskTemplateDAO.qryTaskBackReason(bo.getLong("TEMPLATE_ID"));

        BoHelper.listDtoToBO("TASK_TEMPLATE_BACK_REASONS", backReasons, TaskBackReasonInfo.class, bo);

    }

    /**
     * 新增任务模板回退原因
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addTaskTemplateBackReason(DynamicDict bo) throws BaseAppException {
        logger.info("addTaskTemplateBackReason .");

        TaskBackReasonInfo taskBackReasonInfo = (TaskBackReasonInfo) BoHelper.boToDto(bo, TaskBackReasonInfo.class);

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        taskTemplateDAO.addTaskBackReason(taskBackReasonInfo);

        BoHelper.dtoToBO(taskBackReasonInfo, bo);
    }

    /**
     * 修改任务模板回退原因
     *
     * @param bo
     * @throws BaseAppException
     */
    public void modTaskTemplateBackReason(DynamicDict bo) throws BaseAppException {
        logger.info("modTaskTemplateBackReason .");

        TaskBackReasonInfo taskBackReasonInfo = (TaskBackReasonInfo) BoHelper.boToDto(bo, TaskBackReasonInfo.class);

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        taskTemplateDAO.modTaskBackReason(taskBackReasonInfo);
    }

    /**
     * 删除任务模板回退原因
     *
     * @param bo
     * @throws BaseAppException
     */
    public void delTaskTemplateBackReason(DynamicDict bo) throws BaseAppException {
        logger.info("delTaskTemplateBackReason .");

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        taskTemplateDAO.delTaskBackReason(bo.getLong("BACK_REASON_ID"));
    }

    /**
     * 查询服务参数
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryServiceVar(DynamicDict bo) throws BaseAppException {

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        List<ServiceParamDto> vars = taskTemplateDAO.qrySerParamByTemplatId(bo.getLong("TEMPLATE_ID"));

        BoHelper.listDtoToBO("TASK_TEMPLATE_SERVICE_VARS", vars, ServiceParamDto.class, bo);

    }

    /**
     * 新增服务参数
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addServiceVar(DynamicDict bo) throws BaseAppException {

        ServiceParamDto serviceVarDto = (ServiceParamDto) BoHelper.boToDto(bo, ServiceParamDto.class);

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        taskTemplateDAO.addServiceVar(serviceVarDto);

        BoHelper.dtoToBO(serviceVarDto, bo);
    }

    /**
     * 修改服务参数
     *
     * @param bo
     * @throws BaseAppException
     */
    public void modServiceVar(DynamicDict bo) throws BaseAppException {

        ServiceParamDto serviceVarDto = (ServiceParamDto) BoHelper.boToDto(bo, ServiceParamDto.class);

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        taskTemplateDAO.modServiceVar(serviceVarDto);
    }

    /**
     * 删除服务参数
     *
     * @param bo
     * @throws BaseAppException
     */
    public void delServiceVar(DynamicDict bo) throws BaseAppException {
        ServiceParamDto serviceVarDto = (ServiceParamDto) BoHelper.boToDto(bo, ServiceParamDto.class);
        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        taskTemplateDAO.delServiceVar(serviceVarDto);
    }

    /**
     * 查询模板类型
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryTemplateType(DynamicDict bo) throws BaseAppException {

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        bo.set("TEMPLATE_TYPE_LIST", flowManagerDao.qryTemplateType());
    }

    /**
     * 查询模板类型
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryAllCustomTemplate(DynamicDict bo) throws BaseAppException {

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        bo.set("TEMPLATE_TYPE_LIST", flowManagerDao.qryTemplateType());

        BoHelper.listDtoToBO("TASK_TEMPLATE_LIST", flowManagerDao.qryCustomTemplate(), BpmTaskTemplateDto.class, bo);
    }

    /**
     * 删除自定义图元
     *
     * @param bo
     * @throws BaseAppException
     */
    public void removeTemplatePic(DynamicDict bo) throws BaseAppException {

        String picPath = bo.getString("PIC_PATH");
        File delFile = new File(picPath);
        delFile.delete();
    }
}
