package com.ztesoft.uboss.bpm.setup.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.ButtonQueryCondition;
import com.ztesoft.uboss.bpm.setup.model.TaskEventDto;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.BoHelper;
import com.ztesoft.zsmart.core.utils.SeqUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;

import java.util.List;

/**
 * 流程模板管理类
 *
 * @author ma.dongxin
 */
public class TaskRepoManager {
    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());

    private static TaskRepoManager instance = new TaskRepoManager();

    public static TaskRepoManager getInstance() {
        return instance;
    }

    /**
     * 生成流程模板和流程类型树
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryFlowTree(DynamicDict bo) throws BaseAppException {
        logger.info("init flowtree..");
        Long catgId = bo.getLong("CATG_ID");
        Long parentCatgId = bo.getLong("PARENT_CATG_ID");
        List<DynamicDict> catgList = getFlowManagerDAO().qryProcCatg(catgId, parentCatgId, false);
        List<DynamicDict> procDefTypeList = getFlowManagerDAO().qryProcDefType(null, null);
        catgList.addAll(procDefTypeList);

        bo.set("tree", catgList);
    }

    /**
     * 查询流程 目录树
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryCatgTree(DynamicDict bo) throws BaseAppException {
        logger.info("init flowtree..");
        List<DynamicDict> catgList = getFlowManagerDAO().qryProcCatg(null, null, false);
//		List<DynamicDict> catgTreeList = new ArrayList<DynamicDict>();
//		for(Map.Entry<String, DynamicDict> entry: catgMap.entrySet()) {  
//			DynamicDict catg = entry.getValue();
//	//		System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");  
//			if(catg.getString("PARENT_CATG_ID") == null){
//				catgTreeList.add(catg);
//			}else{
//				DynamicDict parentCatg = catgMap.get(catg.getString("PARENT_CATG_ID"));
//				parentCatg.add("children", catg);
//			}
//		} 

        bo.set("tree", catgList);
    }

    /**
     * 查询不跟流程模板关联的按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryBtnListNotInTemplate(DynamicDict bo) throws BaseAppException {
        logger.info("init qryBtnListNotInTemplate..");
        ButtonQueryCondition condition = (ButtonQueryCondition) BoHelper.boToDto(bo, ButtonQueryCondition.class);
        List<DynamicDict> buttonList = getTaskTemplateDAO().qryBtnListNotInTemplate(bo, condition);
        bo.set("buttonList", buttonList);
    }

    public void confTaskTemplateEvent(long templateId, List<TaskEventDto> events) throws BaseAppException {

        getTaskTemplateDAO().delTaskTempEvent(templateId);
        getTaskTemplateDAO().addTaskTempEvent(events);
    }

    public List<TaskEventDto> qryTaskTemplateEvent(long templateId) throws BaseAppException {

        return getTaskTemplateDAO().qryTaskTempEvent(templateId);
    }


    /**
     * 删除流程模板关联按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void delBtnFromTaskTemplate(DynamicDict bo) throws BaseAppException {
        logger.info("init delBtnFromTaskTemplate..");
        Long templateId = bo.getLong("TEMPLATE_ID");
        getTaskTemplateDAO().deleteTaskBtnByTaskId(templateId);
    }

    /**
     * 给流程模板添加关联按钮
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addBtnToTaskTemplate(DynamicDict bo) throws BaseAppException {
        logger.info("init addBtnToTaskTemplate..");
        getTaskTemplateDAO().insertTaskBtns(bo);
    }

    /**
     * 查询任务模板
     *
     * @param bo 根据模板id或者流程类型id
     * @throws BaseAppException
     */
    public void qryTaskTempList(DynamicDict bo) throws BaseAppException {
//		Long tempID = bo.getLong("TEMPLATE_ID");
//		Long procTypeID = bo.getLong("PROC_DEF_TYPE_ID");
//		String taskTemplateCode = bo.getString("TASK_TEMPLATE_CODE");
        //List<DynamicDict> taskList = getFlowManagerDAO().qryTaskTempList(tempID,procTypeID,taskTemplateCode);
        List<DynamicDict> taskList = getFlowManagerDAO().qryTaskTempList(bo);
        bo.set("taskList", taskList);
    }

    public long qryTaskTempListCount(DynamicDict bo) throws BaseAppException {

        long count = getFlowManagerDAO().qryTaskTempListCount(bo);
        bo.set("taskList_count", count);

        return count;
    }

    /**
     * 查询会签类型列表
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qrySiginTypeList(DynamicDict bo) throws BaseAppException {
        List<DynamicDict> siginTypeList = getFlowManagerDAO().qrySiginTypeList();
        bo.set("siginTypeList", siginTypeList);
    }

    /**
     * 增加或修改新任务模板，根据前台传的action判断
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addTaskTemp(DynamicDict bo) throws BaseAppException {
        String taskType = bo.getString("TASK_TYPE");
        String actionType = bo.getString("ADD_OR_EDIT");
        bo.setValueByName("CURR_DATE", DateUtil.getCurrentDate());
        if (actionType.equals("add")) {
            Long taskTempId = SeqUtil.getBackServiceDBUtil().getMaxValue("BPM_TASK_TEMPLATE", "TEMPLATE_ID");
            bo.set("TEMPLATE_ID", taskTempId);
            getFlowManagerDAO().insertTaskTemp(bo);
            if (taskType.equalsIgnoreCase("U")) {
                getFlowManagerDAO().addUserTask(bo);
            }
            if (taskType.equalsIgnoreCase("S")) {
                getFlowManagerDAO().addServiceTask(bo);
            }
        }
        if (actionType.equals("edit")) {
            getFlowManagerDAO().updateTaskTemp(bo);
            if (taskType.equalsIgnoreCase("U")) {
                getFlowManagerDAO().updateUserTask(bo);
            }
            if (taskType.equalsIgnoreCase("S")) {
                getFlowManagerDAO().updateServiceTask(bo);
            }
        }
        if (actionType.equals("delete")) {
            bo.set("STATE", "X");
            getFlowManagerDAO().updateTaskTemp(bo);
        }

    }

    public void confTaskTemplateEvent(List<TaskEventDto> taskEventDtos) {

    }

    private IFlowManagerDAO flowManagerDao = null;

    private IFlowManagerDAO getFlowManagerDAO() throws BaseAppException {
        if (flowManagerDao == null) {
            try {
                flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
            } catch (Exception ex) {
                ExceptionHandler.publish("S-FLOW-00011", ex.getCause());
            }
        }
        return flowManagerDao;
    }

    private ITaskTemplateDAO taskTemplateDAO = null;

    private ITaskTemplateDAO getTaskTemplateDAO() throws BaseAppException {
        if (taskTemplateDAO == null) {
            taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        }
        return taskTemplateDAO;
    }
}
