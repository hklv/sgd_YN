package com.ztesoft.uboss.bpm.taskcenter.dao;

import com.ztesoft.uboss.bpm.taskcenter.model.HolderQueryCondition;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;

import java.util.List;


public interface IWorkItemManagerDAO {

    List<DynamicDict> qryTaskList(DynamicDict bo, HolderQueryCondition condition, DynamicDict uboss_session) throws BaseAppException;

    List<DynamicDict> qryAbnoramlTaskList(DynamicDict bo) throws BaseAppException;

    List<DynamicDict> qryBlockTaskList(DynamicDict bo) throws BaseAppException;

    long qryAbnoramlTaskListCount(DynamicDict bo) throws BaseAppException;

    long qryBlockTaskListCount(DynamicDict bo) throws BaseAppException;

    DynamicDict selectTaskDetail(DynamicDict dict) throws BaseAppException;

    List<DynamicDict> qryHolderList(DynamicDict bo, HolderQueryCondition condition) throws BaseAppException;

    List<DynamicDict> qryTaskListByHolder(DynamicDict bo) throws BaseAppException;

    List<Long> qryProxyUser(Long userId) throws BaseAppException;

    List<DynamicDict> qryTaskListByTaskId(String taskId) throws BaseAppException;

    DynamicDict queryHolderBizKeyByHolderNo(String holderNo) throws BaseAppException;
}
