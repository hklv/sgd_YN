package com.ztesoft.uboss.bpm.setup.dao;

import com.ztesoft.uboss.bpm.setup.model.BpmBackCfgDto;
import com.ztesoft.uboss.bpm.setup.model.BpmTaskTemplateDto;
import com.ztesoft.uboss.bpm.setup.model.ProcessTemplateVersion;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.uboss.model.BpmTaskEventDto;

import java.util.List;
import java.util.Map;


public interface IProcessTemplateDAO {


    ProcessTemplateVersion selProcessTemplateVersion(Long processVerId) throws BaseAppException;

    int[] insertActReProcDefVer(String processDefinitionId, List<Map<String, Object>> varMapList) throws BaseAppException;

    void updateBpmProcessTempVer(String deployId, String processDefinitionId, Long newVerionId) throws BaseAppException;

    void removeHisFlow(String procDefId, int version) throws BaseAppException;

    ProcessTemplateVersion selActiveProcessVer(String procBizKey) throws BaseAppException;

    String selProcdefId(String key) throws BaseAppException;

    BpmTaskTemplateDto selTaskTempLate(long template_id) throws BaseAppException;

    List<BpmTaskEventDto> selTaskTempLateEvent(long template_id) throws BaseAppException;

    int[] insertProcBackCfg(String processDefinitionId, List<BpmBackCfgDto> cfgs) throws BaseAppException;

    String selProcDefId(String holderId) throws BaseAppException;

    List<TaskBackReasonInfo> selProcBackCfg(Long templateId, String procDefId) throws BaseAppException;

    ProcessTemplateVersion selDeployId(String id) throws BaseAppException;

    int updateProcDefVar(String processDefinitionId, Map<String, Object> varMap) throws BaseAppException;

    int delProcVariable(String id) throws BaseAppException;
}
