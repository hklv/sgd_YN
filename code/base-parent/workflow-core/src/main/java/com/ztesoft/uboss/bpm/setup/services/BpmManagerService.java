package com.ztesoft.uboss.bpm.setup.services;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskHolderEntity;
import com.ztesoft.uboss.bpm.setup.bll.TemplateManager;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.ProcessTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmProcessVarDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessTemplateVersion;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.*;
import org.activiti.uboss.ext.ServiceParamDto;
import org.apache.commons.lang.StringEscapeUtils;
import utils.UbossActionSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author ma.dongxin
 * @since 2012/9/11
 */
public class BpmManagerService extends UbossActionSupport {

    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());

    private static TemplateManager templateManager;

    public BpmManagerService() {
        this.templateManager = new TemplateManager();
    }

    public void qryFlowCatg(DynamicDict bo) throws BaseAppException {
        Long catgId = bo.getLong("CATG_ID");
        Long parentCatgId = bo.getLong("PARENT_CATG_ID");
        List<DynamicDict> catgList = getFlowManagerDAO().qryProcCatg(catgId,
                parentCatgId, false);
        bo.set("tree", catgList);
    }

    /**
     * 返回一个树状结构的流程目录
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryFlowCatgTree(DynamicDict bo) throws BaseAppException {
        List<DynamicDict> catgList = getFlowManagerDAO().qryProcCatgTree();
        bo.set("tree", catgList);
    }

    /**
     * 返回一个树状结构的流程目录
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryFlowCatgDetail(DynamicDict bo) throws BaseAppException {
        DynamicDict catg = getFlowManagerDAO().qryProcCatgDetail(bo.getLong("CAT_ID"));
        bo.set("CATG_DETAIL", catg);
    }

    public void qryFlowTree(DynamicDict bo) throws BaseAppException {
        logger.info("init flowtree..");
        List<DynamicDict> catgList = getFlowManagerDAO().qryProcCatgTree();
        List<DynamicDict> procDefTypeList = getFlowManagerDAO().qryProcDefType(
                null, null);
        List<DynamicDict> procTempList = getFlowManagerDAO().qryProcTemp(null,
                null);
        List<DynamicDict> procVersionList = getFlowManagerDAO().qryProcVersion(
                null, null);
        if (catgList == null) {
            catgList = new ArrayList<DynamicDict>();
        }
        catgList.addAll(procDefTypeList);
        catgList.addAll(procTempList);
        catgList.addAll(procVersionList);

        bo.set("tree", catgList);
    }

    /**
     * 查询流程模板，同时查询ative状态的版本
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryProcTemp(DynamicDict bo) throws BaseAppException {
        Long procDefTypeId = bo.getLong("PROC_DEF_TYPE_ID");
        Long procTempId = bo.getLong("PROC_TEMP_ID");
        List<DynamicDict> temps = getFlowManagerDAO().qryProcTemp(procTempId,
                procDefTypeId);
        bo.set("tempList", temps);
    }

    /**
     * 查询流程变量定义
     *
     * @param dict
     * @throws BaseAppException
     */
    public int queryPorcessVarDef(DynamicDict dict) throws BaseAppException {
        String deployId = dict.getString("DEPLOY_ID");
        BoHelper.listDtoToBO("PROC_DEF_VAR_LIST", qryProcVarList(deployId), BpmProcessVarDto.class, dict);
        return 0;
    }

    /**
     * 查询流程变量列表
     *
     * @param deployId 流程部署ID
     * @return
     * @throws BaseAppException
     */
    public List<BpmProcessVarDto> qryProcVarList(String deployId) throws BaseAppException {
        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        return flowManagerDao.qryProcessVarDefByDeployId(deployId);

    }

    /**
     * 查询流程变量列表（这个接口用在新的portal中）(add by hklv)
     *
     * @param dict 参数 流程版本ID
     * @return
     * @throws BaseAppException
     */
    public int qryProcVarList(DynamicDict dict) throws BaseAppException {
        BoHelper.listDtoToBO("PROC_DEF_VAR_LIST", qryProcVarList(getDeployId(dict)), BpmProcessVarDto.class, dict);
        return 0;
    }

    /**
     * 查询流程环节变量定义
     *
     * @param dict
     * @throws BaseAppException
     */
    public int queryProcessTacheVar(DynamicDict dict) throws BaseAppException {
        String deployId = dict.getString("DEPLOY_ID");

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        List<ServiceParamDto> varList = flowManagerDao
                .qryProcessTacheVarDefByDeployId(deployId);

        Set<String> templateIdSet = new HashSet<String>();
        for (ServiceParamDto dto : varList) {

            templateIdSet.add(dto.getTacheId());
        }

        List<DynamicDict> templateIds = new ArrayList<DynamicDict>();

        for (String id : templateIdSet) {

            DynamicDict dynamicDict = new DynamicDict();
            dynamicDict.set("TACHE_ID", id);
            templateIds.add(dynamicDict);
        }

        BoHelper.listDtoToBO("PROC_TACHE_VAR_LIST", varList,
                ServiceParamDto.class, dict);

        dict.set("PROC_TEMPLATE_ID_LIST", templateIds);

        return 0;
    }

    /**
     * 查询流程回退设置定义
     *
     * @param dict
     * @throws BaseAppException
     */
    public int queryPorcessBackCfgDef(DynamicDict dict) throws BaseAppException {
        String deployId = dict.getString("DEPLOY_ID");

        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);

        List<TaskBackReasonInfo> varList = flowManagerDao
                .qryProcessBackCfgByDeployId(deployId);

        if (varList != null) {

            Set<String> srcActSet = new HashSet<String>();
            List<DynamicDict> srcActList = new ArrayList<DynamicDict>();
            for (TaskBackReasonInfo back : varList) {

                srcActSet.add(back.getSrcActId());
            }

            for (String actId : srcActSet) {

                DynamicDict dynamicDict = new DynamicDict();
                dynamicDict.set("SRC_TACHE_ID", actId);
                srcActList.add(dynamicDict);
            }

            dict.set("BACK_SRC_TACHE_ID_LIST", srcActList);
        }

        BoHelper.listDtoToBO("PROC_DEF_BACK_CFG_LIST", varList,
                TaskBackReasonInfo.class, dict);

        return 0;
    }

    /**
     * 查询流程类型定义
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryProcDef(DynamicDict bo) throws BaseAppException {
        Long procDefTypeId = bo.getLong("PROC_DEF_TYPE_ID");
        Long catgId = bo.getLong("CATG_ID");
        List<DynamicDict> procDefList = getFlowManagerDAO().qryProcDefType(
                procDefTypeId, catgId);
        bo.set("procDefList", procDefList);
    }

    /**
     * 创建目录
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addCatg(DynamicDict bo) throws BaseAppException {
        Long catgId = SeqUtil.getBackServiceDBUtil().getMaxValue("BPM_PROC_CATG",
                "CATG_ID");
        bo.set("CATG_ID", catgId);
        getFlowManagerDAO().insertCatg(bo);
    }

    /**
     * 修改目录
     *
     * @param bo
     * @throws BaseAppException
     */
    public void updateCatg(DynamicDict bo) throws BaseAppException {
        getFlowManagerDAO().updateCatg(bo);
    }

    /**
     * 删除目录
     *
     * @param bo
     * @throws BaseAppException
     */
    public void deleteCatg(DynamicDict bo) throws BaseAppException {
        List<DynamicDict> subCatgs = getFlowManagerDAO().qryProcCatg(null,
                bo.getLong("CATG_ID"), false);
        if (subCatgs.size() > 0) {
            ExceptionHandler.publish("该目录存在子目录,不能删除!",
                    ExceptionHandler.BUSS_ERROR);
        }
        List<DynamicDict> subProcDefs = getFlowManagerDAO().qryProcDefType(
                null, bo.getLong("CATG_ID"));
        if (subProcDefs.size() > 0) {
            ExceptionHandler.publish("该目录存在流程类型定义,不能删除!",
                    ExceptionHandler.BUSS_ERROR);
        }
        getFlowManagerDAO().delCatg(bo);
    }

    /**
     * 创建流程类型
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addPorcessType(DynamicDict bo) throws BaseAppException {
        Long procTypeId = SeqUtil.getBackServiceDBUtil().getMaxValue(
                "BPM_PROC_DEF_TYPE", "PROC_DEF_TYPE_ID");
        bo.set("PROC_DEF_TYPE_ID", procTypeId);
        bo.setValueByName("STATE_DATE", DateUtil.getCurrentDate());
        bo.setValueByName("CREATE_DATE", DateUtil.getCurrentDate());
        getFlowManagerDAO().insertProcessType(bo);
    }

    /**
     * 修改流程类型
     *
     * @param bo
     * @throws BaseAppException
     */
    public void updatePorcessType(DynamicDict bo) throws BaseAppException {
        bo.setValueByName("STATE_DATE", DateUtil.getCurrentDate());
        getFlowManagerDAO().updateProcessType(bo);
    }

    /**
     * 删除流程类型
     *
     * @param bo
     * @throws BaseAppException
     */
    public void deletePorcessType(DynamicDict bo) throws BaseAppException {
        List<DynamicDict> processTemps = getFlowManagerDAO().qryProcTemp(null,
                bo.getLong("PROC_DEF_TYPE_ID"));
        if (processTemps.size() > 0) {
            ExceptionHandler.publish("该类型下有流程,不能删除!",
                    ExceptionHandler.BUSS_ERROR);
        }
        bo.setValueByName("STATE_DATE", DateUtil.getCurrentDate());
        getFlowManagerDAO().deleteProcessType(bo);
    }

    /**
     * 新建流程模板
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addProcTemp(DynamicDict bo) throws BaseAppException {
        templateManager.addProcTemp(bo);
    }

    /**
     * 删除流程模板
     *
     * @param bo
     * @throws BaseAppException
     */
    public void delProcTemp(DynamicDict bo) throws BaseAppException {
        getFlowManagerDAO().delProcTemp(bo);
    }

    public void addProcVersion(DynamicDict bo) throws BaseAppException {
        templateManager.addProcVersion(bo);
    }

    /**
     * 存储流程图
     *
     * @param dict
     * @throws BaseAppException
     */
    public ProcessTemplateVersion deployProcessDef(DynamicDict dict)
            throws BaseAppException {

        ProcessTemplateVersion version = (ProcessTemplateVersion) BoHelper.boToDto(dict,
                ProcessTemplateVersion.class);

        AssertUtil.isNotNull(version.getBpmnContent(),
                "BpmnContent cannot be null");
        version.setProcessVerId(dict.getLong("ID"));
        AssertUtil.isNotNull(version.getProcessVerId(), "Id cannot be null");
        version.setBpmnContent(StringEscapeUtils.unescapeXml(version
                .getBpmnContent()));
        templateManager.deployProcessDef(version);
        return version;
    }

    /**
     * 查询流程版本列表
     *
     * @param dict 流程版本ID 流程模板ID
     * @return
     * @throws BaseAppException
     */
    public List<DynamicDict> qryProcVersionList(DynamicDict dict) throws BaseAppException {
        Long procVersionId = dict.getLong("PROCESS_VER_ID");
        Long procTempId = dict.getLong("PROC_TEMP_ID");
        return getFlowManagerDAO().qryProcVersion(
                procVersionId, procTempId);
    }

    /**
     * 获取流程部署Id
     *
     * @param dict 流程部署Id
     * @return
     * @throws BaseAppException
     */
    public String getDeployId(DynamicDict dict) throws BaseAppException {
        List<DynamicDict> versionList = qryProcVersionList(dict);
        DynamicDict version = versionList.get(0);
        return (String) version.get("DEPLOY_ID");
    }

    /**
     * 新增流程变量
     *
     * @param dict PROCESS_VER_ID 流程版本ID，VAR_DATA 流程变量
     * @throws BaseAppException
     */
    public void addProcessVariables(DynamicDict dict) throws BaseAppException {
        templateManager.addProcessVariables(getDeployId(dict), dict);
    }

    /**
     * 删除流程变量
     *
     * @param dict 流程变量Id
     * @throws BaseAppException
     */
    public void delProcVariable(DynamicDict dict) throws BaseAppException {
        templateManager.delProcVariable((String) dict.get("ID_"));
    }

    /**
     * 流程新增异常设置
     *
     * @param dict PROCESS_VER_ID 流程版本ID，BACK_CFG_DATA 流程异常设置
     * @throws BaseAppException
     */
    public void addProcessBackCfg(DynamicDict dict) throws BaseAppException {
        templateManager.addProcessBackCfg(getDeployId(dict), dict.getList("BACK_CFG_DATA"));
    }

    /**
     * 新增环节变量
     *
     * @param dict PROCESS_VER_ID 流程版本ID，SERVICE_VAR_DATA 流程环节变量
     * @throws BaseAppException
     */
    public void addTacheVar(DynamicDict dict) throws BaseAppException {
        templateManager.addTacheVar(getDeployId(dict), dict.getList("SERVICE_VAR_DATA"));
    }

    public void qryBpmContent(DynamicDict dict) throws BaseAppException {
        String id = dict.getString("DEPLOY_ID");

        if (StringUtil.isEmpty(id)) {

            IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);

            String bizKey = dict.getString("PROC_BIZ_KEY");

            if (StringUtil.isEmpty(bizKey)) {

                ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
                String holderId = dict.getString("HOLDER_ID");

                if (StringUtil.isEmpty(holderId)) {//根据流程单号查流程定义图

                    String holderNo = dict.getString("HOLDER_NO");

                    if (StringUtil.isNotEmpty(holderNo)) {

                        TaskHolderEntity entry = taskHolderDAO.selectByNo(holderNo);

                        if (entry == null) {

                            ExceptionHandler.publish("system can not found this flow, HOLDER_NO:" + holderNo);
                        }

                        ProcessTemplateVersion version = processTemplateDAO.selProcessTemplateVersion(entry.getProcessVerId());

                        if (version == null) {

                            ExceptionHandler.publish("system can not found an active flow version, PROC_BIZ_KEY:" + entry.getBusinessKey());
                        }

                        id = version.getDeployId();
                    }

                } else {//根据流程单ID查流程定义图

                    TaskHolderEntity entry = taskHolderDAO.select(holderId);

                    if (entry == null) {

                        ExceptionHandler.publish("system can not found this flow, HOLDER_ID:" + holderId);
                    }

                    ProcessTemplateVersion version = processTemplateDAO.selProcessTemplateVersion(entry.getProcessVerId());

                    if (version == null) {

                        ExceptionHandler.publish("system can not found an active flow version, PROC_BIZ_KEY:" + entry.getBusinessKey());
                    }

                    id = version.getDeployId();
                }
            } else {//根据流程定义KEY查流程定义图

                ProcessTemplateVersion version = processTemplateDAO.selActiveProcessVer(bizKey);

                if (version == null) {

                    ExceptionHandler.publish("system can not found an active flow version, PROC_BIZ_KEY:" + bizKey);
                }

                id = version.getDeployId();
            }
        }

        AssertUtil.isNotNull(id, "PROC_BIZ_KEY or ID cannot be null");
        String xml = templateManager.qryBpmContent(id);
        dict.set("BPM_XML", xml);
    }

    /**
     * 查询流程版本
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryProcVersion(DynamicDict bo) throws BaseAppException {
        bo.set("procVerList", qryProcVersionList(bo));
    }

    /**
     * 启用或者禁用流程版本
     *
     * @param bo
     * @throws BaseAppException
     */
    public void actOrDisProcVersion(DynamicDict bo) throws BaseAppException {
        getFlowManagerDAO().actOrDisProcVersion(bo.getString("VER_STATE"),
                bo.getLong("PROCESS_VER_ID"));
    }

    /**
     * 导入流程
     *
     * @param bo
     * @throws BaseAppException
     */
    public void importFlow(DynamicDict bo) throws BaseAppException {
        templateManager.importFlow(bo);

    }

    /**
     * 流程版本另存为
     *
     * @param bo
     * @throws BaseAppException
     */
    public void saveAsNewVersion(DynamicDict bo) throws BaseAppException {
        templateManager.saveAsNewVersion(bo);

    }

    /**
     * 流程版本绑定表单
     *
     * @param bo
     * @throws BaseAppException
     */
    public void bindFormToFlowVer(DynamicDict bo) throws BaseAppException {
        templateManager.bindFormToFlowVer(bo);
    }

    /**
     * 统计工单状态
     *
     * @param bo
     * @throws BaseAppException
     */
    public void taskStateStatistic(DynamicDict bo) throws BaseAppException {
        templateManager.taskStateStatistic(bo);
    }

    /**
     * 查询流程环节变量设置 add by liyb
     *
     * @param bo
     * @throws BaseAppException
     */
    public void qryActTacheVar(DynamicDict bo) throws BaseAppException {

        String templdateID = bo.getString("TEMPLATE_ID");

        ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
        List<ServiceParamDto> serviceParamList = taskTemplateDAO.qrySerParamByTemplatId(Long.valueOf(templdateID));
        BoHelper.listDtoToBO("SERVICE_VAR_DATA", serviceParamList, ServiceParamDto.class, bo);
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
}
