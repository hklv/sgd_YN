package com.ztesoft.uboss.bpm.analysis.dao.abstractimpl;

import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.analysis.dao.ITaskQueryDAO;
import com.ztesoft.uboss.bpm.analysis.model.UserDelayedTasks;
import com.ztesoft.uboss.bpm.analysis.model.UserTaskQryCondition;
import com.ztesoft.uboss.bpm.analysis.model.UserTotalTasks;
import com.ztesoft.zsmart.core.exception.BaseAppException;

import java.util.List;


public class TaskQueryDAO extends BusiBaseDAO implements ITaskQueryDAO {


    @Override
    public List<UserTotalTasks> selectTotalTasks(
            UserTaskQryCondition userTaskQryCondition) throws BaseAppException {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserDelayedTasks> selectDelayedTasks(
            UserTaskQryCondition userTaskQryCondition) throws BaseAppException {
        // TODO Auto-generated method stub
        return null;
    }

}
