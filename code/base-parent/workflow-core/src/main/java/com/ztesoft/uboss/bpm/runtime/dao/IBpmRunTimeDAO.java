package com.ztesoft.uboss.bpm.runtime.dao;

import java.util.List;

import com.ztesoft.uboss.bpm.runtime.beans.ProcessRunVar;
import com.ztesoft.uboss.bpm.runtime.beans.TaskListInfo;
import com.ztesoft.zsmart.core.exception.BaseAppException;


public interface IBpmRunTimeDAO {

	List<TaskListInfo> qryTaskList(String processInstanceId)
			throws BaseAppException;
	
	List<TaskListInfo> qryTaskListByHolderId(String holderId)
			throws BaseAppException;
	
	List<TaskListInfo> qryTaskListByHolderNo(String holderNo)
	throws BaseAppException;

	String qryTaskHolderState(String processInstanceId)
			throws BaseAppException;
	
	String qryTaskHolderStateByHolderId(String holderId)
		throws BaseAppException;

	String qryTaskId(String taskListId) throws BaseAppException;

	int updatePorcessState(String processInstanceId, String updateState,
						   String updateReason) throws BaseAppException;
	
	int updatePorcessStateByHolderId(String holderId, String updateState,
									 String updateReason) throws BaseAppException;

	List<ProcessRunVar>  qryProcessVar(String processInstanceId, String varName)
			throws BaseAppException;

	List<ProcessRunVar> qryAllProcessVar(String processInstanceId)
			throws BaseAppException;
	
	String qryProcInstIdByHolderId(String holderId)
	throws BaseAppException;
}
