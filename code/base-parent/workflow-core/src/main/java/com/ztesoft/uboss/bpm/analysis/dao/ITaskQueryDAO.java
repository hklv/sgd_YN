package com.ztesoft.uboss.bpm.analysis.dao;

import java.util.List;

import com.ztesoft.uboss.bpm.analysis.model.UserDelayedTasks;
import com.ztesoft.uboss.bpm.analysis.model.UserTaskQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.UserTotalTasks;
import com.ztesoft.zsmart.core.exception.BaseAppException;

public interface ITaskQueryDAO {

    List<UserTotalTasks> selectTotalTasks(UserTaskQryCondition userTaskQryCondition) throws BaseAppException;

    List<UserDelayedTasks> selectDelayedTasks(UserTaskQryCondition userTaskQryCondition) throws BaseAppException;
}
