package com.ztesoft.uboss.bpm.taskcenter.dao;

import java.util.List;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.uboss.model.BpmServiceTaskDto;
import org.activiti.uboss.model.BpmTaskAssginDto;
import org.activiti.uboss.model.BpmTaskSiginType;

import com.ztesoft.uboss.bpm.runtime.persistence.TaskListEntity;

public interface ITaskListDAO {
	void save(TaskListEntity taskListEntity) throws BaseAppException;
	
	void saveParam(TaskListEntity e) throws BaseAppException;
	
	void updateTaskState(String taskListId, String state) throws BaseAppException;
	
	void compSignalTask(String taskListId, Long userId) throws BaseAppException;
	
	BpmServiceTaskDto getServiceName(String taskTemplateId) throws BaseAppException;
	
	BpmServiceTaskDto getBackServiceName(String taskTemplateId) throws BaseAppException;
	
	BpmTaskAssginDto selectTaskCandidate(String taskTemplateId) throws BaseAppException;
	
	String selectTaskId(String taskListId) throws BaseAppException;
	
	TaskListEntity selectTaskListDetail(String taskListId) throws BaseAppException;
	
	int updateTaskListOwner(String taskListId, Long userId) throws BaseAppException;
	
	List<TaskListEntity> selectSubTaskList(String taskListId) throws BaseAppException;
	
	BpmTaskSiginType selectTaskSiginType(String taskTemplateId) throws BaseAppException;
	
	List<Long> selectRoleUsers(Long roleId) throws BaseAppException;
	
	
	String qryHisAct(String hisTaskListId) throws BaseAppException;
	
	String qryTaskListByHolderId(String holderId, String templateCode) throws BaseAppException;
	
	String qryTaskListByHolderNo(String holderNo, String templateCode) throws BaseAppException;
	
	List<String> qryTaskListTemplateId(String HolderId) throws BaseAppException;
	
	List<String> qryTaskListTemplateIdByNo(String HolderNo) throws BaseAppException;
	
	TaskListEntity selTaskDetail(String taskId) throws BaseAppException;
	
}
