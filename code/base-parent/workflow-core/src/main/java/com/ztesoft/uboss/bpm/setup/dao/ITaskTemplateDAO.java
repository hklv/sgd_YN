package com.ztesoft.uboss.bpm.setup.dao;

import java.util.List;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import org.activiti.uboss.ext.ServiceParamDto;

import com.ztesoft.uboss.bpm.setup.model.ButtonQueryCondition;
import com.ztesoft.uboss.bpm.setup.model.TaskEventDto;
import com.ztesoft.uboss.bpm.taskcenter.model.TaskBackReasonInfo;


public interface ITaskTemplateDAO {
	/**
	 * 查询按钮库列表
	 * @param bo
	 * @throws BaseAppException
	 */
	List<DynamicDict>  qryButtonList(DynamicDict bo, ButtonQueryCondition condition) throws BaseAppException;
	
	/**
	 * 新增按钮
	 * @param bo
	 * @throws BaseAppException
	 */
	int addButton(DynamicDict bo) throws BaseAppException;
	
	/**
	 * 修改按钮
	 * @param bo
	 * @throws BaseAppException
	 */
	int updateButton(DynamicDict bo) throws BaseAppException;
	
	/**
	 * 删除按钮
	 * @param bo
	 * @throws BaseAppException
	 */
	int delButton(DynamicDict bo) throws BaseAppException;
	
	/**
	 * 根据流程模板查询按钮
	 * @param catgId
	 * @return
	 * @throws BaseAppException
	 */
	
	/**
	 * 查询不跟流程模板关联的按钮
	 * @param bo
	 * @param condition
	 * @return
	 * @throws BaseAppException
	 */
	List<DynamicDict> qryBtnListNotInTemplate(DynamicDict bo, ButtonQueryCondition condition) throws BaseAppException;
	
	 /**
     * 删除任务模板按钮<br>
     * @param taskId
     * @throws BaseAppException <br>
     */
	 int deleteTaskBtnByTaskId(Long taskId) throws BaseAppException;
    
    /**
     * 插入流程模板按钮
     * @param dict
     * @return
     * @throws BaseAppException
     */
	int [] insertTaskBtns(DynamicDict dict)throws BaseAppException;
    
    /**
     * 查询工单关联按钮
     * @param templateId
     * @return
     * @throws BaseAppException
     */
	List<DynamicDict> qryBtnListByTemp(Long templateId)throws BaseAppException;
    
    int delTaskTempEvent(Long templateId)throws BaseAppException;
    
    void addTaskTempEvent(List<TaskEventDto> events)throws BaseAppException;
    
    List<TaskEventDto> qryTaskTempEvent(Long templateId)throws BaseAppException;
    
    List<TaskBackReasonInfo> qryTaskBackReason(Long templateId)throws BaseAppException;
    
    void addTaskBackReason(TaskBackReasonInfo taskBackReasonInfo)throws BaseAppException;
    
    void modTaskBackReason(TaskBackReasonInfo taskBackReasonInfo)throws BaseAppException;
    
    void delTaskBackReason(long backReasonId)throws BaseAppException;
    
    void addServiceVar(ServiceParamDto serviceVarDto)throws BaseAppException;
    
    void modServiceVar(ServiceParamDto serviceVarDto)throws BaseAppException;
    
    void delServiceVar(ServiceParamDto serviceVarDto)throws BaseAppException;
    
    List<ServiceParamDto> qrySerParamByTemplatId(Long templateId)throws BaseAppException;

	
}
