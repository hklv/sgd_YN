package com.ztesoft.uboss.bpm.setup.dao;

import com.ztesoft.uboss.bpm.runtime.beans.ProcessBackInfo;
import com.ztesoft.uboss.bpm.setup.model.*;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import org.activiti.uboss.ext.ServiceParamDto;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface IFlowManagerDAO {

    /**
     * 查询流程目录，可以根据id或者父id查询。不传入id则查询全部可用的目录,传入iftop标识只查询顶级目录
     *
     * @param catgId
     * @param parentCatgId * @param ifTop
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> qryProcCatg(Long catgId, Long parentCatgId, boolean ifTop) throws BaseAppException;

    Map<String, DynamicDict> qryCatgMap(Long catgId, Long parentCatgId, boolean ifTop) throws BaseAppException;

    /**
     * 返回一个树状结构的流程目录树
     * * @param ifTop
     *
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> qryProcCatgTree() throws BaseAppException;

    DynamicDict qryProcCatgDetail(Long catId) throws BaseAppException;

    /**
     * 查询流程类型定义
     *
     * @param defTypeId
     * @param catgId
     * @return
     * @throws BaseAppException
     */
    List<DynamicDict> qryProcDefType(Long defTypeId, Long catgId) throws BaseAppException;

    List<DynamicDict> qryProcTemp(Long procTempId, Long procDefTypeId) throws BaseAppException;

    List<DynamicDict> qryProcVersion(Long procVerId, Long procTempId) throws BaseAppException;

    void insertProcTemp(DynamicDict dict) throws BaseAppException;

    void insertCatg(DynamicDict dict) throws BaseAppException;

    void updateCatg(DynamicDict dict) throws BaseAppException;

    void delCatg(DynamicDict dict) throws BaseAppException;

    void insertProcessType(DynamicDict dict) throws BaseAppException;

    void updateProcessType(DynamicDict dict) throws BaseAppException;

    void deleteProcessType(DynamicDict dict) throws BaseAppException;

    void insertProcVersion(DynamicDict dict) throws BaseAppException;

    void updateProcTemp(DynamicDict dict) throws BaseAppException;

    void delProcTemp(DynamicDict dict) throws BaseAppException;

    void updateProcVersion(DynamicDict dict) throws BaseAppException;

    void actOrDisProcVersion(String state, Long procTempId) throws BaseAppException;

    void modTaskState_A(long userId, String taskListId) throws BaseAppException;

    void modTaskState_A(String taskListId) throws BaseAppException;

    void modTaskState_C(String taskListId, Date cTime, String taskResult, Long userId) throws BaseAppException;

    void modTaskState_H(String taskListId, String suspendReason) throws BaseAppException;

    void modTaskState_D(String taskListId, String deleteReason) throws BaseAppException;

    void updateTaskListState(String taskListId, String state) throws BaseAppException;

    void insertTaskTemp(DynamicDict bo) throws BaseAppException;

    void addServiceTask(DynamicDict bo) throws BaseAppException;

    void addUserTask(DynamicDict bo) throws BaseAppException;

    void updateTaskTemp(DynamicDict bo) throws BaseAppException;

    void updateServiceTask(DynamicDict bo) throws BaseAppException;

    void updateUserTask(DynamicDict bo) throws BaseAppException;

    List<DynamicDict> qrySiginTypeList() throws BaseAppException;

    List<DynamicDict> qryTaskTempList(Long tempID, Long procTypeID, String taskTemplateCode)
            throws BaseAppException;

    List<DynamicDict> qryTaskTempList(DynamicDict conditionDict) throws BaseAppException;

    List<DynamicDict> qryPhotoTemplate(DynamicDict conditionDict) throws BaseAppException;

    long qryTaskTempListCount(DynamicDict conditionDict) throws BaseAppException;

    List<ProcessDefView> qryAllProcessDef()
            throws BaseAppException;

    List<ProcessDefView> qryAllSimProcessVersion(DynamicDict dynamicDict)
            throws BaseAppException;

    long qryAllSimProcessVersionCount(DynamicDict dynamicDict)
            throws BaseAppException;

    List<BpmProcessVarDto> qryProcessVarDef(String procDefId)
            throws BaseAppException;

    List<BpmProcessVarDto> qryProcessVarDefByProcKey(String procKey)
            throws BaseAppException;

    List<BpmProcessVarDto> qryProcessVarDefByDeployId(String deployId)
            throws BaseAppException;

    List<ServiceParamDto> qryProcessTacheVarDefByDeployId(String deployId) throws BaseAppException;

    List<TaskBackReasonInfo> qryProcessBackCfgByDeployId(String deployId) throws BaseAppException;

    void modTaskState(String taskListId, String taskState, Long userId, String stateReason) throws BaseAppException;

    void bindFormToFlowVer(Long flowVerId, Long formId) throws BaseAppException;

    HolderNoIdDto qryProcessHolderNum(String processInstanceId) throws BaseAppException;

    List<TaskStatisDto> taskStateStatistic() throws BaseAppException;

    BpmBackHolderDto selBackHolder(String backId) throws BaseAppException;

    void insertBackHolder(ProcessBackInfo processBackInfo) throws BaseAppException;

    void completeBackHolder(String backId) throws BaseAppException;

    void backTaskList(String taskListId, Long userId) throws BaseAppException;

    List<DynamicDict> qryTemplateType() throws BaseAppException;

    List<BpmTaskTemplateDto> qryCustomTemplate() throws BaseAppException;
}
