package com.ztesoft.uboss.bpm.taskcenter.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.runtime.beans.ProcessVerInfo;
import com.ztesoft.uboss.bpm.runtime.constant.TaskHolderConstant;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskHolderEntity;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.jdbc.ParamMap;
import com.ztesoft.zsmart.core.jdbc.ParamObject;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.utils.StringUtil;
import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.model.BpmHoderLogDto;

import java.sql.Date;
import java.util.List;


public class TaskHolderDAO extends BusiBaseDAO implements ITaskHolderDAO {

    public void save(TaskHolderEntity taskHolderEntity) throws BaseAppException {
        StringBuilder inserSql = new StringBuilder();
        inserSql.append("INSERT INTO BPM_TASK_HOLDER (HOLDER_ID, HOLDER_NO, PROCESS_VER_ID, PROCESS_NAME, PROC_INST_ID, BUSINESS_KEY, HOLDER_STATE, HOLDER_STATE_DATE, START_TIME, SIMU_FLAG, CREATOR_ID)");
        inserSql.append(" VALUES (:ID, :HOLDER_NO, :PROCESS_VER_ID, :PROCESS_NAME, :PROC_INST_ID, :BUSINESS_KEY, :HOLDER_STATE, :HOLDER_STATE_DATE, :START_TIME, :SIMU_FLAG,:CREATOR_ID)");

        this.updateObject(inserSql.toString(), taskHolderEntity);
    }

    public long getHolderCount() throws BaseAppException {
        String getCountSql = "SELECT COUNT(1) CNT FROM BPM_TASK_HOLDER WHERE (SIMU_FLAG='0' OR SIMU_FLAG IS NULL)";
        return this.selectCount(getCountSql, null);
    }

    public long getSimuHolderCount() throws BaseAppException {
        String getCountSql = "SELECT COUNT(1) CNT FROM BPM_TASK_HOLDER WHERE SIMU_FLAG='1'";
        return this.selectCount(getCountSql, null);
    }

    public void completeHolder(String holderId) throws BaseAppException {
        String completedSQL = "UPDATE BPM_TASK_HOLDER SET HOLDER_STATE = 'C', END_TIME=? WHERE HOLDER_ID=?";

        this.updateObject(completedSQL, DateUtil.getNowDate(), holderId);
    }

    public Long getProcessVerId(String processDefinitionId)
            throws BaseAppException {

        String selVerSQL = "SELECT PROCESS_VER_ID FROM BPM_PROCESS_TEMP_VER WHERE  PROC_DEF_ID = ?";

        return this.selectObject(selVerSQL, Long.class, processDefinitionId);
    }

    public ProcessVerInfo getProcessVer(String processDefinitionId)
            throws BaseAppException {

        String selVerSQL = "SELECT A.PROCESS_NAME,B.PROCESS_VER_ID,B.VER_STATE FROM BPM_PROCESS_TEMP A, BPM_PROCESS_TEMP_VER B WHERE B.PROC_DEF_ID = ? AND A.PROC_TEMP_ID = B.PROC_TEMP_ID";

        return this.selectObject(selVerSQL, ProcessVerInfo.class,
                processDefinitionId);
    }

    @Override
    public String selectTaskHolerId(String taskHolderId)
            throws BaseAppException {

        String selVerSQL = "SELECT PROC_INST_ID FROM BPM_TASK_HOLDER WHERE HOLDER_ID = ?";

        return this.selectObject(selVerSQL, String.class, taskHolderId);
    }

    @Override
    public void rollBackHolder(String holderId, Long userId)
            throws BaseAppException {

        String completedSQL = "UPDATE BPM_TASK_HOLDER SET HOLDER_STATE = 'T', END_TIME=?， LAST_MODIFITER=? WHERE HOLDER_ID=?";

        this.updateObject(completedSQL, DateUtil.getNowDate(), holderId, userId);
    }

    @Override
    public String selRollBackCfg(String procDefId) throws BaseAppException {

        String selSQL = "SELECT B.RETRACEMENT_NOTICE_ ACT_RE_PROCDEF A, ACT_RE_RETRACEMENT_CFG B WHERE A.KEY_ = ? AND A.ID_ = B.ID_";

        return this.selectObject(selSQL, String.class, procDefId);
    }

    @Override
    public void insertHolderLog(BpmHoderLogDto logDto) throws BaseAppException {

        String insertSql = "INSERT INTO BPM_HOLDER_LOG (LOG_ID, HOLDER_ID, HOLDER_NO, PROCESS_NAME, ACTION, ACTION_TIME, OPERATOR, COMMENTS)"
                + " VALUES (:LOG_ID, :HOLDER_ID, :HOLDER_NO, :PROCESS_NAME, :ACTION, :ACTION_TIME, :OPERATOR, :COMMENTS)";

        this.updateObject(insertSql, logDto);
    }

    @Override
    public TaskHolderEntity select(String holderId) throws BaseAppException {

        StringBuilder selSql = new StringBuilder();
        selSql.append("SELECT HOLDER_ID as ID, HOLDER_NO, PROCESS_VER_ID, PROCESS_NAME, PROC_INST_ID, BUSINESS_KEY, HOLDER_STATE, HOLDER_STATE_DATE, START_TIME, SIMU_FLAG, CREATOR_ID FROM BPM_TASK_HOLDER");
        selSql.append(" WHERE HOLDER_ID = ?");

        return this.selectObject(selSql.toString(), TaskHolderEntity.class,
                holderId);
    }

    @Override
    public void updateHolderState(String holderId, String state, String userName)
            throws BaseAppException {

        Date now = DateUtil.GetDBDateTime();

        if (TaskHolderConstant.HOLDER_STATE_TERMINATE.equals(state)
                || TaskHolderConstant.HOLDER_STATE_COMPLETE.equals(state)) {

            String updateSql = "UPDATE BPM_TASK_HOLDER SET HOLDER_STATE =?, HOLDER_STATE_DATE =?, END_TIME = ?, LAST_MODIFY_USER=? WHERE HOLDER_ID = ?";

            this.updateObject(updateSql, state, now, now, userName, holderId);
        } else {

            String updateSql = "UPDATE BPM_TASK_HOLDER SET HOLDER_STATE =?, HOLDER_STATE_DATE =?, LAST_MODIFY_USER=? WHERE HOLDER_ID = ?";

            this.updateObject(updateSql, state, now, userName, holderId);
        }

    }

    @Override
    public void addTacheVar(String processDefinitionId, List<DynamicDict> vars)
            throws BaseAppException {

        String delVarSql = "DELETE FROM ACT_RE_TACHE_VAR WHERE ID = ?";

        this.updateObject(delVarSql, processDefinitionId);

        String sqlStr = "INSERT INTO ACT_RE_TACHE_VAR (ID, TACHE_ID,TASK_TEMPLATE_ID, VAR_CODE,VAR_VALUE,VAR_TYPE,SCOPE,COMMENTS)"
                + " VALUES (:ID, :TACHE_ID, :TASK_TEMPLATE_ID, :VAR_CODE,:VAR_VALUE,:VAR_TYPE,:SCOPE,:COMMENTS)";

        ParamObject[] paraList = ParamObject.newParamObjectList(8, vars.size());

        for (int i = 0; i < vars.size(); i++) {
            DynamicDict var = vars.get(i);

            paraList[0].setBatchElement("ID", processDefinitionId, i);
            paraList[1].setBatchElement("TASK_TEMPLATE_ID", var.getLong("TEMPLATE_ID") == null ? var.getLong("TASK_TEMPLATE_ID") : var.getLong("TEMPLATE_ID"), i);
            paraList[2].setBatchElement("TACHE_ID", var.getString("TACHE_ID"), i);
            paraList[3].setBatchElement("VAR_CODE", var.getString("VAR_CODE"),
                    i);

            if (StringUtil.isEmpty(var.getString("VAR_VALUE"))) {

                paraList[4].setBatchElement("VAR_VALUE", var.getString("DEFAULT_VALUE"), i);
            } else {

                paraList[4].setBatchElement("VAR_VALUE", var.getString("VAR_VALUE"), i);
            }


            paraList[5].setBatchElement("VAR_TYPE", var.getString("VAR_TYPE"),
                    i);
            paraList[6].setBatchElement("SCOPE", var.getString("SCOPE"), i);
            paraList[7].setBatchElement("COMMENTS", var.getString("COMMENTS"),
                    i);

        }

        executeBatch(sqlStr, ParamMap.wrap(paraList));
    }


    @Override
    public List<ServiceParamDto> selectTacheVarByDeployId(String deployId, Long templateId, String tacheId)
            throws BaseAppException {

        String selSql = "SELECT * FROM ACT_RE_TACHE_VAR A, ACT_RE_PROCDEF B WHERE A.ID = B.ID_ AND A.TASK_TEMPLATE_ID=? AND A.TACHE_ID=? and  B.DEPLOYMENT_ID_ = ?";

        return this.selectList(selSql, ServiceParamDto.class, templateId, tacheId, deployId);
    }

    @Override
    public TaskHolderEntity selectByNo(String holderNo) throws BaseAppException {

        StringBuilder selSql = new StringBuilder();
        selSql.append("SELECT HOLDER_ID ID, HOLDER_NO, PROCESS_VER_ID, PROCESS_NAME, PROC_INST_ID, BUSINESS_KEY, HOLDER_STATE, HOLDER_STATE_DATE, START_TIME, SIMU_FLAG, CREATOR_ID FROM BPM_TASK_HOLDER");
        selSql.append(" WHERE HOLDER_NO = ?");

        return this.selectObject(selSql.toString(), TaskHolderEntity.class, holderNo);
    }
}
