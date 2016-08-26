package com.ztesoft.uboss.bpm.taskcenter.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.uboss.model.BpmServiceTaskDto;
import org.activiti.uboss.model.BpmTaskAssginDto;
import org.activiti.uboss.model.BpmTaskSiginType;

import java.util.List;


public class TaskListDAO extends BusiBaseDAO implements ITaskListDAO {

    public void save(TaskListEntity taskListEntity) throws BaseAppException {
        StringBuilder insertSQL = new StringBuilder();
        insertSQL
                .append("INSERT INTO BPM_TASK_LIST (TASK_LIST_ID, HOLDER_ID, TASK_TYPE,");
        insertSQL
                .append(" PROC_INST_ID, EXECUTION_ID, TASK_ID, ACT_ID, TASK_NAME, PARENT_TASK_ID, OWNER,");
        insertSQL
                .append(" USER_ID, ROLE_ID, JOB_ID, ORG_ID, DIRECTION, DESCRIPTION, START_TIME, END_TIME, DURATION,");
        insertSQL
                .append(" PRIORITY, DUE_DATE, CREATE_DATE, STATE, STATE_DATE, STATE_REASON, TEMPLATE_ID, ACT_INST_ID)");
        insertSQL
                .append(" VALUES (:TASK_LIST_ID, :HOLDER_ID, :TASK_TYPE, :PROC_INST_ID,");
        insertSQL
                .append(" :EXECUTION_ID, :TASK_ID, :ACT_ID, :TASK_NAME, :PARENT_TASK_ID, :OWNER, :USER_ID, :ROLE_ID,");
        insertSQL
                .append(" :JOB_ID, :ORG_ID, :DIRECTION, :DESCRIPTION, :START_TIME, :END_TIME, :DURATION,");
        insertSQL
                .append(" :PRIORITY, :DUE_DATE, :CREATE_DATE, :STATE, :STATE_DATE, :STATE_REASON, :TEMPLATE_ID,:ACT_INST_ID)");

        this.updateObject(insertSQL.toString(), taskListEntity);
    }

    public void saveParam(TaskListEntity e) throws BaseAppException {
        String updateSQL = "UPDATE BPM_TASK_LIST SET TASK_PARAM1=?,TASK_PARAM2=?,TASK_PARAM3=?,TASK_PARAM4=?,TASK_PARAM5=?,TASK_PARAM6=? "
                + " WHERE TASK_LIST_ID=?";

        this.updateObject(updateSQL, e.getTaskParam1(), e.getTaskParam2(),
                e.getTaskParam3(), e.getTaskParam4(), e.getTaskParam5(),
                e.getTaskParam6(), e.getTaskListId());
    }

    public void updateTaskState(String taskListId, String state)
            throws BaseAppException {
        String updateSQL = "UPDATE BPM_TASK_LIST SET STATE=?, STATE_DATE=? WHERE TASK_LIST_ID=?";

        this.updateObject(updateSQL, state, DateUtil.GetDBDateTime(), taskListId);
    }

    public BpmServiceTaskDto getServiceName(String taskTemplateId) throws BaseAppException {
        String getServNameSQL = "SELECT SERVICE_NAME, TEMPLATE_ID FROM BPM_SERV_TASK WHERE  TEMPLATE_ID = ?";

        return this.selectObject(getServNameSQL, BpmServiceTaskDto.class,
                Long.valueOf(taskTemplateId));
    }

    public BpmTaskAssginDto selectTaskCandidate(String taskTemplateId)
            throws BaseAppException {

        String selSql = "SELECT USER_ID, ORG_ID, ROLE_ID, JOB_ID, SERVICE_NAME FROM BPM_USER_TASK WHERE TEMPLATE_ID = ?";

        return this.selectObject(selSql, BpmTaskAssginDto.class,
                Long.valueOf(taskTemplateId));
    }

    public String selectTaskId(String taskListId) throws BaseAppException {

        String selSql = "SELECT TASK_ID FROM BPM_TASK_LIST WHERE TASK_LIST_ID=?";

        return this.selectObject(selSql, String.class, taskListId);
    }

    public TaskListEntity selectTaskListDetail(String taskListId)
            throws BaseAppException {

        String selSql = "SELECT * FROM BPM_TASK_LIST WHERE TASK_LIST_ID=?";

        return this.selectObject(selSql, TaskListEntity.class, taskListId);
    }

    public BpmTaskSiginType selectTaskSiginType(String taskTemplateId)
            throws BaseAppException {
        String sql = "SELECT A.* FROM BPM_TASK_SIGIN_TYPE A, BPM_USER_TASK B WHERE B.TEMPLATE_ID = ? AND A.SIGIN_TYPE = B.SIGIN_TYPE";

        return this.selectObject(sql, BpmTaskSiginType.class,
                Long.valueOf(taskTemplateId));
    }

    public List<TaskListEntity> selectSubTaskList(String taskListId)
            throws BaseAppException {
        String selSql = "SELECT * FROM BPM_TASK_LIST WHERE PARENT_TASK_ID=? AND STATE <> 'C'";

        return this.selectList(selSql, TaskListEntity.class, taskListId);
    }

    public int updateTaskListOwner(String taskListId, Long userId)
            throws BaseAppException {

        String updateSql = "UPDATE BPM_TASK_LIST SET OWNER=? WHERE TASK_LIST_ID = ?";

        return this.updateObject(updateSql, userId, taskListId);
    }

    public List<Long> selectRoleUsers(Long roleId) throws BaseAppException {

        String selSql = "SELECT USER_ID FROM BFM_USER_ROLE WHERE ROLE_ID = ?";

        return this.selectList(selSql, Long.class, roleId);
    }

    public String qryHisAct(String hisTaskListId) throws BaseAppException {

        String sqlSql = "SELECT A.ACT_ID_ FROM ACT_HI_ACTINST A, BPM_TASK_LIST B WHERE A.ID_ = B.ACT_INST_ID AND B.TASK_LIST_ID = ?";

        return this.selectObject(sqlSql, String.class, hisTaskListId);
    }

    @Override
    public String qryTaskListByHolderId(String holderId, String templateCode)
            throws BaseAppException {

        String sqlSql = "SELECT TASK_LIST_ID FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B"
                + " WHERE A.HOLDER_ID = ? AND B.CODE = ? AND A.TEMPLATE_ID = B.TEMPLATE_ID";

        return this.selectObject(sqlSql, String.class, holderId, templateCode);
    }

    @Override
    public String qryTaskListByHolderNo(String holderNo, String templateCode)
            throws BaseAppException {

        String sqlSql = "SELECT TASK_LIST_ID FROM BPM_TASK_LIST A, BPM_TASK_HOLDER B, BPM_TASK_TEMPLATE C"
                + " WHERE A.HOLDER_ID = B.HOLDER_ID AND A.TEMPLATE_ID = C.TEMPLATE_ID AND C.CODE = ? AND B.HOLDER_NO = ?";

        return this.selectObject(sqlSql, String.class, templateCode, holderNo);
    }

    @Override
    public List<String> qryTaskListTemplateId(String HolderId)
            throws BaseAppException {

        String sqlSql = "SELECT B.CODE FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B"
                + " WHERE A.TEMPLATE_ID = B.TEMPLATE_ID AND A.HOLDER_ID = ? AND A.STATE IN ('I', 'A')";

        return this.selectList(sqlSql, String.class, HolderId);
    }

    @Override
    public List<String> qryTaskListTemplateIdByNo(String HolderNo)
            throws BaseAppException {

        String sqlSql = "SELECT B.CODE FROM BPM_TASK_LIST A, BPM_TASK_TEMPLATE B, BPM_TASK_HOLDER C"
                + " WHERE A.TEMPLATE_ID = B.TEMPLATE_ID AND A.HOLDER_ID = C.HOLDER_ID AND C.HOLDER_NO=? AND A.STATE IN ('I', 'A')";

        return this.selectList(sqlSql, String.class, HolderNo);
    }

    @Override
    public BpmServiceTaskDto getBackServiceName(String taskTemplateId)
            throws BaseAppException {

        String getServNameSQL = "SELECT SERVICE_NAME, TEMPLATE_ID FROM BPM_TASK_EVENT WHERE  TEMPLATE_ID = ? AND EVENT_TYPE = 'BACK'";

        return this.selectObject(getServNameSQL, BpmServiceTaskDto.class, Long.valueOf(taskTemplateId));
    }

    @Override
    public void compSignalTask(String taskListId, Long userId)
            throws BaseAppException {

        String updateSQL = "UPDATE BPM_TASK_LIST SET STATE='C', USER_ID = ?, STATE_DATE=? WHERE TASK_LIST_ID=?";

        this.updateObject(updateSQL, userId, DateUtil.GetDBDateTime(), taskListId);
    }

    @Override
    public TaskListEntity selTaskDetail(String taskId) throws BaseAppException {

        String selSql = "SELECT TASK_LIST_ID, HOLDER_ID, TASK_TYPE, PROC_INST_ID, EXECUTION_ID, TASK_ID, ACT_ID,"
                + "TASK_NAME, PARENT_TASK_ID, OWNER,"
                + " USER_ID, ROLE_ID, JOB_ID, ORG_ID, DIRECTION, DESCRIPTION, START_TIME, END_TIME, DURATION,"
                + " PRIORITY, DUE_DATE, CREATE_DATE, STATE, STATE_DATE, STATE_REASON, TEMPLATE_ID, ACT_INST_ID"
                + " FROM BPM_TASK_LIST where TASK_ID = ?";

        return this.selectObject(selSql, TaskListEntity.class, taskId);
    }


}
