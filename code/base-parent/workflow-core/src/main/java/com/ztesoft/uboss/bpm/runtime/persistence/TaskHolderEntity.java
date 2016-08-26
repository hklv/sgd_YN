package com.ztesoft.uboss.bpm.runtime.persistence;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.runtime.beans.ProcessVerInfo;
import com.ztesoft.uboss.bpm.runtime.constant.TaskHolderConstant;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.utils.StringUtil;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.model.BpmHoderLogDto;
import org.activiti.uboss.model.HolderChangeInfo;
import org.activiti.uboss.model.ZFlowDef;
import org.activiti.uboss.task.ITaskHolderEntity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.sql.Date;
import java.util.UUID;


public class TaskHolderEntity implements ITaskHolderEntity {
    //
    private String id;
    //
    private Long processVerId;

    private String holderNo;
    //
    private String procInstId;
    //
    private String businessKey;

    private String holderState;

    private Date holderStateDate;
    //
    private Date startTime;
    //
    private Date endTime;
    //
    private String deleteReason;

    private String processName;

    private String simuFlag;

    private Long creatorId;

    private String lastModifyUser;


    private String findHodlerNo(String verState) throws BaseAppException {

        String date = DateUtil.date2String(DateUtil.GetDBDateTime(), DateUtil.DATETIME_FORMAT_2);
        date += StringUtil.padLeft(String.valueOf(new java.util.Random().nextInt(999)), 3, '0');

        // 在房展状态的流程在流程单里面打上标记
        if ("S".equals(verState)) {
            this.simuFlag = "1";
            //Long cnt = taskHolderDAO.getSimuHolderCount();

            String maxNo = "SIMU" + date;
            return maxNo;
        } else {
            this.simuFlag = "0";
            //Long cnt = taskHolderDAO.getHolderCount();
            String maxNo = date;

            return maxNo;
        }
    }

    public TaskHolderEntity createEntity(ActivityExecution execution)
            throws BaseAppException {

        ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);

        this.id = UUID.randomUUID().toString();

        ProcessDefinitionEntity pd = (ProcessDefinitionEntity) execution
                .getActivity().getProcessDefinition();
        ProcessVerInfo processVerInfo = taskHolderDAO
                .getProcessVer(pd.getKey());

        if (processVerInfo != null && processVerInfo.getProcessVerId() != null) {

            this.processVerId = Long
                    .parseLong(processVerInfo.getProcessVerId());
            this.procInstId = execution.getProcessInstanceId();
            this.businessKey = execution.getProcessBusinessKey();

            this.startTime = DateUtil.getNowDate();
            this.holderState = "A";
            this.holderStateDate = this.startTime;

            this.processName = processVerInfo.getProcessName();

            /** @TODO call script to get actual no */
            this.holderNo = findHodlerNo(processVerInfo.getVerState());

            //流程启动人
            creatorId = (Long) execution.getVariable(ZFlowDef.BPM_HOLDER_USER_ID);

            taskHolderDAO.save(this);
            execution.setVariable(ZFlowDef.BPM_TASK_HOLDER_ID, this.id);
            execution.setVariable(ZFlowDef.BPM_HOLDER_USER, this.id);


            //记日志
            BpmHoderLogDto logDto = new BpmHoderLogDto();
            logDto.setLogId(UUID.randomUUID().toString());
            logDto.setAction(TaskHolderConstant.HOLDER_ACTION_CREATE);
            logDto.setHolderId(this.id);
            logDto.setProcessName(this.processName);
            logDto.setActionTime(this.startTime);
            logDto.setHolderNo(this.holderNo);
            logDto.setOperator((String) execution.getVariable(ZFlowDef.BPM_HOLDER_USER));

            taskHolderDAO.insertHolderLog(logDto);
        }

        return this;
    }

    public void close(Object holderId) throws BaseAppException {
        if (holderId != null) {
//			ITaskHolderDAO taskHolderDAO = DAOFactory
//					.getDAO(ITaskHolderDAO.class);
//			taskHolderDAO.completeHolder(String.valueOf(holderId));

            HolderChangeInfo changeInfo = new HolderChangeInfo();
            changeInfo.setHolderId((String) holderId);
            changeInfo.setAction(TaskHolderConstant.HOLDER_ACTION_FINISH);

            changeHolder(changeInfo);
        }
    }

    /**
     * @return the
     */
    public String getId() {
        return id;
    }

    public String getHolderNo() {
        return holderNo;
    }

    public void setHolderNo(String holderNo) {
        this.holderNo = holderNo;
    }

    /**
     * @return the
     */
    public Long getProcessVerId() {
        return processVerId;
    }

    /**
     * @return the
     */
    public String getProcInstId() {
        return procInstId;
    }

    /**
     * @return the
     */
    public String getBusinessKey() {
        return businessKey;
    }

    public String getHolderState() {
        return holderState;
    }

    public void setHolderState(String holderState) {
        this.holderState = holderState;
    }

    public Date getHolderStateDate() {
        return holderStateDate;
    }

    public void setHolderStateDate(Date holderStateDate) {
        this.holderStateDate = holderStateDate;
    }

    /**
     * @return the
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @return the
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @return the
     */
    public String getDeleteReason() {
        return deleteReason;
    }

    /*
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * @param processVerId the processVerId to set
     */
    public void setProcessVerId(Long processVerId) {
        this.processVerId = processVerId;
    }

    /*
     * @param procInstId the procInstId to set
     */
    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    /*
     * @param businessKey the businessKey to set
     */
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    /*
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /*
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /*
     * @param deleteReason the deleteReason to set
     */
    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    /**
     * auto generate toString() method by apache commons-lang
     */
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * auto generate hashCode() method by apache commons-lang
     */
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * auto generate equals() method by apache commons-lang
     */
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getSimuFlag() {
        return simuFlag;
    }

    public void setSimuFlag(String simuFlag) {
        this.simuFlag = simuFlag;
    }

    @Override
    public void changeHolder(HolderChangeInfo changeInfo) throws BaseAppException {

        if (changeInfo != null) {

            if (StringUtil.isEmpty(changeInfo.getUserName())) {

                changeInfo.setUserName("system");
            }

            ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
            TaskHolderEntity entity = taskHolderDAO.select(changeInfo.getHolderId());

            String holderState = null;

            if (TaskHolderConstant.HOLDER_ACTION_BLOCK.equals(changeInfo.getAction())) {//挂起

                holderState = TaskHolderConstant.HOLDER_STATE_BLOCK;
            } else if (TaskHolderConstant.HOLDER_ACTION_UNBLOCK.equals(changeInfo.getAction())) {//接挂

                holderState = TaskHolderConstant.HOLDER_STATE_ACTIVE;
            } else if (TaskHolderConstant.HOLDER_ACTION_STOP.equals(changeInfo.getAction())) {//停止

                holderState = TaskHolderConstant.HOLDER_STATE_TERMINATE;
            } else if (TaskHolderConstant.HOLDER_ACTION_WITHDRAW.equals(changeInfo.getAction())) {//撤回

                holderState = TaskHolderConstant.HOLDER_STATE_WITH_DRAW;
            } else if (TaskHolderConstant.HOLDER_ACTION_WITHDRAW_COMPLETE.equals(changeInfo.getAction())) {//撤回结束

                holderState = TaskHolderConstant.HOLDER_STATE_TERMINATE;
            } else if (TaskHolderConstant.HOLDER_ACTION_FINISH.equals(changeInfo.getAction())) {//完成

                holderState = TaskHolderConstant.HOLDER_STATE_COMPLETE;
            } else if (TaskHolderConstant.HOLDER_ACTION_BACK.equals(changeInfo.getAction())) {//回退

                holderState = TaskHolderConstant.HOLDER_STATE_BACK;
            } else if (TaskHolderConstant.HOLDER_ACTION_BACK_COMPLETE.equals(changeInfo.getAction())) {//回退结束

                holderState = TaskHolderConstant.HOLDER_STATE_ACTIVE;
            }

            //修改状态
            taskHolderDAO.updateHolderState(changeInfo.getHolderId(), holderState, changeInfo.getUserName());

            //记日志
            BpmHoderLogDto logDto = new BpmHoderLogDto();
            logDto.setAction(changeInfo.getAction());
            logDto.setHolderId(entity.getId());
            logDto.setProcessName(entity.getProcessName());
            logDto.setActionTime(DateUtil.getNowDate());
            logDto.setHolderNo(entity.getHolderNo());
            logDto.setOperator(changeInfo.getUserName());

            logDto.setComments(changeInfo.getReason());
            logDto.setLogId(UUID.randomUUID().toString());

            taskHolderDAO.insertHolderLog(logDto);
        }
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

}