package com.ztesoft.uboss.bpm.setup.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.model.BpmBackCfgDto;
import com.ztesoft.uboss.bpm.setup.model.BpmTaskTemplateDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessTemplateVersion;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.ParamObject;
import com.ztesoft.zsmart.pot.utils.ParamMapHelper;
import org.activiti.uboss.model.BpmTaskEventDto;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class ProcessTemplateDAO extends BusiBaseDAO implements
        IProcessTemplateDAO {

    public ProcessTemplateVersion selProcessTemplateVersion(Long processVerId)
            throws BaseAppException {

        String selSql = "SELECT * FROM BPM_PROCESS_TEMP_VER where PROCESS_VER_ID = ?";

        return this.selectObject(selSql, ProcessTemplateVersion.class,
                processVerId);
    }

    public int[] insertActReProcDefVer(String processDefinitionId,
                                       List<Map<String, Object>> varMapList) throws BaseAppException {

        String delVarSql = "DELETE FROM ACT_RE_PROCDEF_VAR WHERE PROC_DEF_ID = ?";

        this.updateObject(delVarSql, processDefinitionId);

        String sqlStr = "insert into ACT_RE_PROCDEF_VAR (ID_, PROC_DEF_ID, VAR_NAME, VAR_TYPE, DEFAULT_VALUE, VAR_COMMENTS)"
                + " VALUES (:ID_, :PROC_DEF_ID, :VAR_NAME, :VAR_TYPE, :DEFAULT_VALUE, :VAR_COMMENTS)";

        ParamObject[] paraList = ParamObject.newParamObjectList(6,
                varMapList.size());

        for (int i = 0; i < varMapList.size(); i++) {

            paraList[0].setBatchElement("ID_", (Long) varMapList.get(i).get("ID_"), i);
            paraList[1].setBatchElement("PROC_DEF_ID", processDefinitionId, i);
            paraList[2].setBatchElement("VAR_NAME", (String) varMapList.get(i)
                    .get("VAR_NAME"), i);
            paraList[3].setBatchElement("VAR_TYPE", (String) varMapList.get(i)
                    .get("VAR_TYPE"), i);
            paraList[4].setBatchElement("DEFAULT_VALUE", (String) varMapList
                    .get(i).get("DEFAULT_VALUE"), i);
            paraList[5].setBatchElement("VAR_COMMENTS", (String) varMapList
                    .get(i).get("VAR_COMMENTS"), i);
        }

        return executeBatch(sqlStr, ParamMap.wrap(paraList));
    }

    /**
     * 新增或编辑流程变量
     *
     * @param processDefinitionId 流程定义Id
     * @param varMap              流程变量
     * @return
     * @throws BaseAppException
     */
    public int updateProcDefVar(String processDefinitionId, Map<String, Object> varMap) throws BaseAppException {
        ParamMap pm = new ParamMap();
        Long procVerId = Long.parseLong(String.valueOf(varMap.get("ID_")));
        ParamMapHelper.setValue(pm, "ID_", procVerId);
        ParamMapHelper.setValue(pm, "PROC_DEF_ID", processDefinitionId);
        ParamMapHelper.setValue(pm, "VAR_NAME", (String) varMap.get("VAR_NAME"));
        ParamMapHelper.setValue(pm, "VAR_TYPE", (String) varMap.get("VAR_TYPE"));
        ParamMapHelper.setValue(pm, "DEFAULT_VALUE", (String) varMap.get("DEFAULT_VALUE"));
        ParamMapHelper.setValue(pm, "VAR_COMMENTS", (String) varMap.get("VAR_COMMENTS"));
        String sqlStr;
        if (getProcVarById(procVerId) == null) {
            sqlStr = "insert into ACT_RE_PROCDEF_VAR (ID_, PROC_DEF_ID, VAR_NAME, VAR_TYPE, DEFAULT_VALUE, VAR_COMMENTS)"
                    + " VALUES (:ID_, :PROC_DEF_ID, :VAR_NAME, :VAR_TYPE, :DEFAULT_VALUE, :VAR_COMMENTS)";
            return this.executeUpdate(sqlStr, pm);

        } else {
            sqlStr = "update ACT_RE_PROCDEF_VAR set VAR_NAME = :VAR_NAME, VAR_TYPE = :VAR_TYPE, DEFAULT_VALUE = :DEFAULT_VALUE, VAR_COMMENTS = :VAR_COMMENTS where ID_= :ID_";
            return this.executeUpdate(sqlStr, pm);
        }
    }

    public int delProcVariable(String id) throws BaseAppException {
        String delVarSql = "DELETE FROM ACT_RE_PROCDEF_VAR WHERE ID_ = ?";
        return this.updateObject(delVarSql, id);
    }

    /**
     * 根据Id查询流程变量
     *
     * @param Id 流程变量Id
     * @return 流程变量
     * @throws BaseAppException
     */
    public String getProcVarById(Long Id) throws BaseAppException {
        return this.selectObject("SELECT A.var_name FROM ACT_RE_PROCDEF_VAR A WHERE A.ID_ = ?", String.class, Id);
    }

    public void updateBpmProcessTempVer(String deployId,
                                        String processDefinitionId, Long newVerionId)
            throws BaseAppException {

        String updateSql = "UPDATE BPM_PROCESS_TEMP_VER SET DEPLOY_ID=?,PROC_DEF_ID=? WHERE PROCESS_VER_ID=?";

        this.updateObject(updateSql, deployId, processDefinitionId, newVerionId);

    }

    public void removeHisFlow(String key, int version)
            throws BaseAppException {

        this.updateObject("delete from ACT_RE_BACK_CFG  where PROC_DEF_ID_ in (select id_ from act_re_procdef where key_=? and version_<?)", key, version);
        this.updateObject("delete from ACT_RE_TACHE_VAR  where ID in (select id_ from act_re_procdef where key_=? and version_<?)", key, version);
        this.updateObject("delete from act_re_procdef_var  where proc_def_id in (select id_ from act_re_procdef where key_=? and version_<?)", key, version);
        this.updateObject("delete from act_ru_task  where proc_def_id_ in  (select id_ from act_re_procdef where key_=? and version_<?)", key, version);
        this.updateObject("delete from act_ge_bytearray  where deployment_id_ in (select deployment_id_ from act_re_procdef where key_=? and version_<?)", key, version);
        this.updateObject("delete from act_re_deployment  where id_ in (select deployment_id_ from act_re_procdef where key_=? and version_<?)", key, version);
        this.updateObject("delete from act_re_procdef where key_=? and version_<?", key, version);

    }

    //	@Override
    public ProcessTemplateVersion selActiveProcessVer(String procBizKey)
            throws BaseAppException {

        String selectSql = "SELECT A.PROCESS_VER_ID, A.PROC_DEF_ID, A.DEPLOY_ID, A.PROC_TEMP_ID FROM BPM_PROCESS_TEMP_VER A, BPM_PROCESS_TEMP B"
                + " WHERE A.VER_STATE = 'A' AND A.STATE = 'A' AND A.PROC_TEMP_ID = B.PROC_TEMP_ID AND B.BIZ_KEY = ? AND A.EFFECTIVE_DATE < ? AND A.EXPIRED_DATE > ?";

        Date now = DateUtil.GetDBDateTime();

        return this.selectObject(selectSql, ProcessTemplateVersion.class, procBizKey, now, now);
    }

    @Override
    public BpmTaskTemplateDto selTaskTempLate(long template_id)
            throws BaseAppException {

        String selectSql = "SELECT TEMPLATE_ID, TEMPLATE_NAME, CODE FROM BPM_TASK_TEMPLATE WHERE TEMPLATE_ID = ?";

        return this.selectObject(selectSql, BpmTaskTemplateDto.class, template_id);
    }

    @Override
    public String selProcdefId(String key) throws BaseAppException {

        String selectSql = "SELECT ID_ FROM ACT_RE_PROCDEF WHERE VERSION_ = (SELECT MAX(VERSION_) FROM ACT_RE_PROCDEF WHERE KEY_=?) AND KEY_=?";

        return this.selectObject(selectSql, String.class, key, key);
    }

    @Override
    public List<BpmTaskEventDto> selTaskTempLateEvent(long template_id)
            throws BaseAppException {

        String selectSql = "SELECT TEMPLATE_ID, SERVICE_NAME, EVENT_TYPE FROM BPM_TASK_EVENT WHERE TEMPLATE_ID = ?";

        return this.selectList(selectSql, BpmTaskEventDto.class, template_id);
    }

    @Override
    public int[] insertProcBackCfg(String processDefinitionId, List<BpmBackCfgDto> cfgs) throws BaseAppException {

        String delVarSql = "DELETE FROM ACT_RE_BACK_CFG WHERE PROC_DEF_ID_ = ?";

        this.updateObject(delVarSql, processDefinitionId);

        String sqlStr = "insert into ACT_RE_BACK_CFG (PROC_DEF_ID_, BACK_REASON_ID_, SRC_ACT_ID_, TAG_ACT_ID_, STATE_, STATE_DATE_)"
                + " VALUES (:PROC_DEF_ID, :BACK_REASON_ID, :SRC_ACT_ID, :TAG_ACT_ID, :STATE, :STATE_DATE)";

        ParamObject[] paraList = ParamObject.newParamObjectList(6, cfgs.size());
        Date now = DateUtil.GetDBDateTime();

        for (int i = 0; i < cfgs.size(); i++) {
            BpmBackCfgDto cfg = cfgs.get(i);

            paraList[0].setBatchElement("PROC_DEF_ID", processDefinitionId, i);
            paraList[1].setBatchElement("BACK_REASON_ID", cfg.getBackReasonId(), i);
            paraList[2].setBatchElement("SRC_ACT_ID", cfg.getSrcActId(), i);
            paraList[3].setBatchElement("TAG_ACT_ID", cfg.getTagActId(), i);
            paraList[4].setBatchElement("STATE", "A", i);
            paraList[5].setBatchElement("STATE_DATE", now, i);
        }

        return executeBatch(sqlStr, ParamMap.wrap(paraList));
    }

    @Override
    public String selProcDefId(String holderId) throws BaseAppException {

        String selSql = "SELECT B.PROC_DEF_ID FROM BPM_TASK_HOLDER A, BPM_PROCESS_TEMP_VER B"
                + " WHERE A.PROCESS_VER_ID = B.PROCESS_VER_ID AND A.HOLDER_ID = ?";

        return this.selectObject(selSql, String.class, holderId);
    }

    @Override
    public List<TaskBackReasonInfo> selProcBackCfg(Long templateId, String procDefId)
            throws BaseAppException {

        String selSql = "  SELECT A.BACK_REASON_ID, A.REASON_NAME, A.REASON_CODE FROM BPM_BACK_REASON A, ACT_RE_BACK_CFG B,"
                + "   (SELECT MAX(VERSION_) MAXVERSION_ FROM ACT_RE_PROCDEF WHERE KEY_ = ?) C, ACT_RE_PROCDEF D"
                + "   WHERE A.BACK_REASON_ID = B.BACK_REASON_ID_ AND A.STATE='A' AND B.STATE_='A' AND D.KEY_ = ? AND D.VERSION_ = C.MAXVERSION_"
                + "   AND A.TASK_TEMP_ID = ? AND B.PROC_DEF_ID_ = D.ID_";

        return this.selectList(selSql, TaskBackReasonInfo.class, procDefId, procDefId, templateId);
    }

    public ProcessTemplateVersion selDeployId(String id) throws BaseAppException {
        String sql = "select * from  BPM_PROCESS_TEMP_VER where  PROC_DEF_ID = ?";
        return super.selectObject(sql, ProcessTemplateVersion.class, id);
    }

}
