package com.ztesoft.uboss.bpm.taskcenter.dao;

import com.ztesoft.uboss.bpm.runtime.beans.ProcessVerInfo;
import com.ztesoft.uboss.bpm.runtime.persistence.TaskHolderEntity;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.model.BpmHoderLogDto;

import java.util.List;


public interface ITaskHolderDAO {

    void save(TaskHolderEntity taskHolderEntity) throws BaseAppException;

    long getHolderCount() throws BaseAppException;

    long getSimuHolderCount() throws BaseAppException;

    void completeHolder(String holderId) throws BaseAppException;

    void rollBackHolder(String holderId, Long userId) throws BaseAppException;

    void insertHolderLog(BpmHoderLogDto logDto) throws BaseAppException;

    void updateHolderState(String holderId, String state, String userName) throws BaseAppException;

    String selRollBackCfg(String procDefId) throws BaseAppException;

    Long getProcessVerId(String processDefinitionId) throws BaseAppException;

    String selectTaskHolerId(String taskHolderId) throws BaseAppException;

    ProcessVerInfo getProcessVer(String processDefinitionId) throws BaseAppException;

    TaskHolderEntity select(String holderId) throws BaseAppException;

    TaskHolderEntity selectByNo(String holderNo) throws BaseAppException;

    void addTacheVar(String processDefinitionId, List<DynamicDict> vars) throws BaseAppException;

    //liyb add
    List<ServiceParamDto> selectTacheVarByDeployId(String deployId, Long templateId, String tacheId) throws BaseAppException;

}
