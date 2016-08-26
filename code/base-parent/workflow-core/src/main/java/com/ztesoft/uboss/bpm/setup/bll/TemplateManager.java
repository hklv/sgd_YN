package com.ztesoft.uboss.bpm.setup.bll;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.runtime.client.BpmConstants;
import com.ztesoft.uboss.bpm.runtime.client.ProcessServiceManager;
import com.ztesoft.uboss.bpm.runtime.client.ServiceProvider;
import com.ztesoft.uboss.bpm.setup.dao.IFlowManagerDAO;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.FlowManagerDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.ProcessTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.model.BpmBackCfgDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessTemplateVersion;
import com.ztesoft.uboss.bpm.setup.model.TaskStatisDto;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.uboss.bpm.utils.MyXMLDom4jUtils;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.exception.ExceptionHandler;
import com.ztesoft.zsmart.core.jdbc.DBUtilDecorator;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 流程模板管理，版本管理
 *
 * @author chen.gang71
 */
public class TemplateManager {
    private static ZSmartLogger logger = ZSmartLogger.getLogger(TemplateManager.class);

    private static final String RESOURCE_SUFFIX = ".bpmn";

    public static final String DEFAULT_VER = "1.0.0";

    private SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd-HHmmss");
    public static final Date DEFAULT_EXPIRED_DATE = DateUtil.string2SQLDate(
            "2038-01-19 00:00:00", DateUtil.DEFAULT_TIME_FORMAT);

    /**
     * 发布流程图
     *
     * @param newVersion
     * @throws BaseAppException
     */
    public void deployProcessDef(ProcessTemplateVersion newVersion) throws BaseAppException {

        IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
        ServiceProvider serviceProvider = ProcessServiceManager.getManager()
                .getServiceProvider();
        ProcessTemplateVersion oldVersion = processTemplateDAO
                .selProcessTemplateVersion(newVersion.getProcessVerId());

        String encoding = System.getProperty("file.encoding");
        logger.debug("File encoding = " + encoding);
        if (oldVersion != null) {

            String xml = newVersion.getBpmnContent();

            logger.debug("Before deal:\n" + xml);

            if (!xml.startsWith("<?xml")) {
                xml = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"
                        + xml;
            }
            if (xml.startsWith("<?xml version=\"1.0\"?>")) {
                xml = xml.replaceAll("version=\"1.0\"",
                        "version='1.0' encoding='" + encoding + "'");
            }

			/*
             * 解决firefox浏览器初次创建流程在保存的时候删除xmlns=
			 * "http://www.omg.org/spec/BPMN/20100524/MODEL"属性的问题开始
			 */
            if (xml.indexOf("xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"") == -1) {
                xml = xml
                        .replaceAll(
                                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"",
                                " xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
            }
            /*
             * 解决firefox浏览器初次创建流程在保存的时候删除xmlns=
			 * "http://www.omg.org/spec/BPMN/20100524/MODEL"属性的问题结束
			 */
            /* 处理子流程浏览器自动增加如下属性早晨工作流无法部署的问题开始 */
            xml = xml.replaceAll("xmlns=\"\"", "");
            xml = xml.replaceAll("xmlns:bpmndi=\"BPMN/20100524/DI\"", "");
            xml = xml.replaceAll("xmlns:omgdc=\"DD/20100524/DC\"", "");
            xml = xml.replaceAll("xmlns:omgdi=\"DD/20100524/DI\"", "");
            xml = xml.replaceAll("xmlns:activiti=\"activiti.org/bpmn\"", "");
            xml = xml.replaceAll("&lt;", "<");
            xml = xml.replaceAll("&gt;", ">");
            /* 处理子流程浏览器自动增加如下属性早晨工作流无法部署的问题结束 */
            // 向流程引擎发布
            // Document xmlDoc = XMLDom4jUtils.fromXML(xml,encoding);
            // String flowXML = xmlDoc.asXML();
            __checkBpmnXml__(newVersion);
            // doc.setXMLEncoding(encoding);
            // String flowXML = doc.asXML();
            logger.debug("After deal:\n" + xml);

            try {
                ProcessServiceManager processServiceMgr = ProcessServiceManager
                        .getManager();
                String deployId = processServiceMgr.getDeployService().deploy(
                        oldVersion.getProcTempId() + "-"
                                + sf.format(new java.util.Date())
                                + RESOURCE_SUFFIX, xml);

                ProcessDefinitionQuery procDefQuery = serviceProvider
                        .getRepositoryService().createProcessDefinitionQuery();

                ProcessDefinition def = procDefQuery.deploymentId(deployId)
                        .singleResult();

                if (def != null) {

                    // 回写版本表
                    processTemplateDAO.updateBpmProcessTempVer(deployId,
                            def.getKey(), newVersion.getProcessVerId());

                    removeHisFlow(def.getKey(), def.getVersion() - 1);
                }

            } catch (Exception ex) {
                throw new BaseAppException("BFM-BPM-00004", ex);
            }

        }
    }

    /**
     * 查询流程定义
     *
     * @param deployId 流程部署Id
     * @return
     */
    public ProcessDefinition qryProcessDef(String deployId) {
        ServiceProvider serviceProvider = ProcessServiceManager.getManager().getServiceProvider();
        ProcessDefinitionQuery procDefQuery = serviceProvider.getRepositoryService().createProcessDefinitionQuery();
        return procDefQuery.deploymentId(deployId).singleResult();
    }

    /**
     * 新增流程变量
     *
     * @param deployId       流程部署ID
     * @param processVarDict 流程变量
     * @throws BaseAppException
     */
    public void addProcessVariables(String deployId, DynamicDict processVarDict) throws BaseAppException {
        ProcessDefinition def = qryProcessDef(deployId);
        // 写流程变量
        if (def != null && processVarDict != null) {

            Map<String, Object> varMap = processVarDict.valueMap;
            if (varMap.get("ID_") == null || varMap.get("ID_").equals("")) {
                Long id_ = SeqUtil.getBackServiceDBUtil().getMaxValue("ACT_RE_PROCDEF_VAR", "ID_");
                varMap.put("ID_", id_++);
            }
            varMap.put("PROC_DEF_ID", def.getId());
            IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
            processTemplateDAO.updateProcDefVar(def.getId(), varMap);
        }
    }

    /**
     * 删除流程变量
     *
     * @param id 流程变量Id
     */
    public void delProcVariable(String id) throws BaseAppException {
        if (StringUtils.isNotEmpty(id)) {
            IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
            processTemplateDAO.delProcVariable(id);
        }
    }

    /**
     * 流程新增异常设置
     *
     * @param deployId    流程部署Id
     * @param backCfgDict 异常设置
     * @throws BaseAppException
     */
    public void addProcessBackCfg(String deployId, List<DynamicDict> backCfgDict) throws BaseAppException {
        ProcessDefinition def = qryProcessDef(deployId);
        if (def != null && backCfgDict != null && backCfgDict.size() > 0) {
            List<BpmBackCfgDto> cfgs = new ArrayList<BpmBackCfgDto>(backCfgDict.size());
            for (DynamicDict backCfg : backCfgDict) {
                BpmBackCfgDto bpmBackCfgDto = (BpmBackCfgDto) BoHelper.boToDto(backCfg, BpmBackCfgDto.class);
                bpmBackCfgDto.setProcDefId(def.getId());
                cfgs.add(bpmBackCfgDto);
            }
            IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
            processTemplateDAO.insertProcBackCfg(def.getId(), cfgs);
        }
    }

    /**
     * 新增环节变量
     *
     * @param deployId        流程部署Id
     * @param taskTempVarDict 环节变量
     * @throws BaseAppException
     */
    public void addTacheVar(String deployId, List<DynamicDict> taskTempVarDict) throws BaseAppException {
        ProcessDefinition def = qryProcessDef(deployId);
        // 写流程环节 变量
        if (def != null && taskTempVarDict != null && taskTempVarDict.size() > 0) {
            ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
            taskHolderDAO.addTacheVar(def.getId(), taskTempVarDict);
        }
    }

    private void removeHisFlow(String key, int Latestversion)
            throws BaseAppException {

        IProcessTemplateDAO processTemplateDAO = SgdDaoFactory.getDaoImpl(ProcessTemplateDAOMySQL.class);
        processTemplateDAO.removeHisFlow(key, Latestversion);
    }

    /**
     * 获取流程图xml
     *
     * @param deployId
     * @return
     * @throws IOException
     */
    public String qryBpmContent(String deployId) throws BaseAppException {
        ServiceProvider serviceProvider = ProcessServiceManager.getManager()
                .getServiceProvider();
        List<String> deployNames = serviceProvider.getRepositoryService()
                .getDeploymentResourceNames(deployId);
        String beloyName = null;
        if (deployNames != null && deployNames.size() > 0) {
            for (String n : deployNames) {
                if (n.endsWith("bpmn")) {
                    beloyName = n;
                    break;
                }
            }
            try {
                InputStream is = serviceProvider.getRepositoryService()
                        .getResourceAsStream(deployId, beloyName);
                byte[] content = new byte[is.available()];
                is.read(content);
                if (content.length > 4) {
                    logger.debug("Qry BPM Concent:{} {} {} {}", +content[0],
                            content[1], content[2], content[3]);
                }
                String encoding = System.getProperty("file.encoding");
                String xml = new String(content, encoding);

                if (xml != null && xml.indexOf("GBK") > 0) {
                    xml = new String(content, "GBK");
                } else if (xml.indexOf("UTF-8") > 0) {
                    xml = new String(content, "UTF-8");
                }
                logger.debug("Qry BPM Concent:\n" + xml);
                return xml;
            } catch (Exception ex) {
                ExceptionHandler.publish("BFM-BPM-00004", ex);
            }
        }

        return null;
    }

    /**
     * 检查bpmn xml的合法性。如错误则抛出BaseAppException异常
     *
     * @param version
     * @throws BaseAppException
     */
    private Document __checkBpmnXml__(ProcessTemplateVersion version)
            throws BaseAppException {
        String bpmnXml = version.getBpmnContent();
        if (StringUtils.isNotBlank(bpmnXml)) {
            String procDefId = version.getProcDefId();
            try {
                SAXReader saxReader = new SAXReader();

                Document document = saxReader.read(new StringReader(
                        bpmnXml));

                Element definitionElem = document.getRootElement();
                Element processElem = definitionElem.element("process");
                String processId = processElem.attributeValue("id");
                if (procDefId != null && !procDefId.equals(processId)) {
                    ExceptionHandler.publish("BFM-BPM-00005",
                            ExceptionHandler.BUSS_ERROR, procDefId, processId);
                }
                return document;
            } catch (DocumentException ex) {
                ExceptionHandler.publish("BFM-BPM-00004", ex);
            }
        }
        return null;
    }

    public void bindFormToFlowVer(DynamicDict bo) throws BaseAppException {
        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        Long flowVerId = bo.getLong("PROCESS_VER_ID");
        Long formId = bo.getLong("FORM_ID");
        if (flowVerId == null || formId == null) {
            ExceptionHandler.publish("BFM-BPM-00005");
        }
        flowManagerDao.bindFormToFlowVer(flowVerId, formId);
    }

    /**
     * 统计工单状态
     *
     * @param bo
     * @throws BaseAppException
     */
    public void taskStateStatistic(DynamicDict bo) throws BaseAppException {
        IFlowManagerDAO flowManagerDao = SgdDaoFactory.getDaoImpl(FlowManagerDAOMySQL.class);
        List<TaskStatisDto> list = flowManagerDao.taskStateStatistic();
        BoHelper.listDtoToBO("TASK_STATE_REPORT", list, TaskStatisDto.class, bo);
    }

    /**
     * 导入流程
     *
     * @param bo
     * @throws BaseAppException
     */
    public void importFlow(DynamicDict bo) throws BaseAppException {
        String encoding = System.getProperty("file.encoding");
        String filepath = bo.getString("FILE_PATH");
        String type = bo.getString("IMPORT_TYPE");
        String xml = null;
        try {
            xml = FileUtil.loadTxtFile(filepath);
        } catch (Exception e) {
            ExceptionHandler.publish("BFM-BPM-00005", e);
        }
        Document xmlDoc = XMLDom4jUtils.fromXML(xml, encoding);
        Element process = xmlDoc.getRootElement().element("process");
        Element definitions = xmlDoc.getRootElement().element("definitions");
        String processName = process.elementText("process_name");
        String processVersion = process.element("process_version").getText();
        Element processDefVarList = process.element("proc_def_var_list");
        for (Iterator iter = processDefVarList.elementIterator(); iter
                .hasNext(); ) {
            Element procDefVar = (Element) iter.next();
            String varName = procDefVar.element("var_name").getText();
            String varType = procDefVar.element("var_type").getText();
            String defaultVal = procDefVar.element("default_value").getText();
            String varComm = procDefVar.element("var_comments").getText();
        }

        bo.set("ADD_OR_EDIT", "add");
        bo.set("PROCESS_NAME", processName);
        bo.set("AUTHOR", "import");
        bo.set("STATE_DATE", DateUtil.getCurrentDate());
        bo.set("VER", processVersion);
        bo.set("import", "true");
        if ("flow".equals(type)) {
            addProcTemp(bo);
        } else if ("version".equals(type)) {
            bo.set("PROC_TEMP_ID", bo.get("PROC_TEMP_ID"));
            bo.set("EFFECTIVE_DATE", DateUtil.getCurrentDate());
            bo.set("EXPIRED_DATE", BpmConstants.VER_EXPIRED_DATE);
            addProcVersion(bo);
        }
        ProcessTemplateVersion version = new ProcessTemplateVersion();
        version.setProcessVerId(bo.getLong("PROCESS_VER_ID"));
        List<Element> list = xmlDoc.getRootElement().elements();

        Element processEle = list.get(1).element("process");
//		String uuid = UUID.randomUUID().toString().replaceAll("-", "")
//				.toUpperCase();
        String uuid = bo.getString("FLOW_UUID");
        processEle.addAttribute("id", uuid);
        processEle.addAttribute("name", uuid);
        Element bpmndiEle = list.get(1).element("BPMNDiagram");
        bpmndiEle.addAttribute("id", "BPMNDiagram_" + uuid);
        Element ele = bpmndiEle.element("BPMNPlane");
        ele.addAttribute("id", "BPMNPlane_" + uuid);
        ele.addAttribute("bpmnElement", uuid);
        xml = MyXMLDom4jUtils.element2XML(list.get(1), encoding);
        version.setBpmnContent(xml);
        deployProcessDef(version);
    }

    /**
     * 流程版本另存为新版本
     *
     * @param bo
     * @throws BaseAppException
     */
    public void saveAsNewVersion(DynamicDict bo) throws BaseAppException {

    }

    /**
     * 新建流程模板
     *
     * @param bo
     * @throws BaseAppException
     */
    public void addProcTemp(DynamicDict bo) throws BaseAppException {
        String actionType = bo.getString("ADD_OR_EDIT");
        bo.setValueByName("STATE_DATE", DateUtil.getCurrentDate());

        try {
            if (actionType.equals("add")) {
                if (DBUtilDecorator.isSameValue("BPM_PROCESS_TEMP",
                        "PROCESS_NAME", bo.getString("name"))) {
                    ExceptionHandler.publish("PROCESS_NAME_EXIST",
                            ExceptionHandler.BUSS_ERROR);
                    return;
                }
                if (DBUtilDecorator.isSameValue("BPM_PROCESS_TEMP", "BIZ_KEY",
                        bo.getString("BIZ_KEY"))) {
                    ExceptionHandler.publish("BIZ_KEY_EXIST",
                            ExceptionHandler.BUSS_ERROR);
                    return;
                }
                Long procTempId = SeqUtil.getBackServiceDBUtil().getMaxValue("BPM_PROCESS_TEMP", "PROC_TEMP_ID");
                logger.debug("add process id ===" + procTempId);
                bo.set("PROC_TEMP_ID", procTempId);
                bo.set("STATE", BpmConstants.STATE_ENABLE);
                getFlowManagerDAO().insertProcTemp(bo);
                Long procVerId = SeqUtil.getBackServiceDBUtil().getMaxValue(
                        "BPM_PROCESS_TEMP_VER", "PROCESS_VER_ID");
                bo.set("PROCESS_VER_ID", procVerId);
                bo.set("EFFECTIVE_DATE", DateUtil.getCurrentDate());
                bo.set("EXPIRED_DATE", BpmConstants.VER_EXPIRED_DATE);
                bo.set("VER_STATE", BpmConstants.VER_STATE_EDIT);
                bo.set("AUTHOR", bo.getLong("uboss_session.user-id"));
                if (StringUtil.isEmpty(bo.getString("import"))) {
                    bo.set("VER", BpmConstants.INIT_VER);
                }
                getFlowManagerDAO().insertProcVersion(bo);
            }
            if (actionType.equals("edit")) {
                if (DBUtilDecorator.isSameValue("BPM_PROCESS_TEMP",
                        "PROCESS_NAME", bo.getString("name"),
                        "PROC_TEMP_ID", bo.getLong("PROC_TEMP_ID"))) {
                    ExceptionHandler.publish("PROCESS_NAME_EXIST",
                            ExceptionHandler.BUSS_ERROR);
                    return;
                }
                if (DBUtilDecorator.isSameValue("BPM_PROCESS_TEMP", "BIZ_KEY",
                        bo.getString("BIZ_KEY"), "PROC_TEMP_ID",
                        bo.getLong("PROC_TEMP_ID"))) {
                    ExceptionHandler.publish("BIZ_KEY_EXIST",
                            ExceptionHandler.BUSS_ERROR);
                    return;
                }
                getFlowManagerDAO().updateProcTemp(bo);
            }
        } catch (Exception ex) {
            ExceptionHandler.publish("S-Flow-10001", ex);
        }
        // bo.set("procDefList", procDefList);
    }

    public void addProcVersion(DynamicDict bo) throws BaseAppException {
        String actionType = bo.getString("ADD_OR_EDIT");
        bo.setValueByName("STATE_DATE", DateUtil.getCurrentDate());
        bo.set("AUTHOR", bo.getLong("uboss_session.user-id"));
        try {
            if (actionType.equals("add")) {
                bo.set("STATE", BpmConstants.STATE_ENABLE);
                Long procVerId = SeqUtil.getBackServiceDBUtil().getMaxValue(
                        "BPM_PROCESS_TEMP_VER", "PROCESS_VER_ID");
                bo.set("PROCESS_VER_ID", procVerId);
                bo.set("VER_STATE", BpmConstants.VER_STATE_EDIT);
                getFlowManagerDAO().insertProcVersion(bo);
            }
            if (actionType.equals("edit")) {
                getFlowManagerDAO().updateProcVersion(bo);
            }
        } catch (Exception ex) {
            ExceptionHandler.publish("S-Flow-10002", ex);
        }
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

    public static void main(String[] args) throws
            IOException, BaseAppException {
        String filepath = "D:\\uboss\\BatchFile\\upload\\任务单流程1.0.0.xml";
        String xml = FileUtil.loadTxtFile(filepath);
        // System.out.println(xml);

        Document xmlDoc = XMLDom4jUtils.fromXML(xml, "UTF-8");
        List<Element> list = xmlDoc.getRootElement().elements();
        String processName = list.get(0).elementText("process_name");
        xml = list.get(1).asXML();
        System.out.println(xml);
        System.out.println("==" + processName);
    }

}
