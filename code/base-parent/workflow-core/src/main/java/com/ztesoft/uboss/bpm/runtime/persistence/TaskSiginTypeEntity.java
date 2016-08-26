package com.ztesoft.uboss.bpm.runtime.persistence;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.sgd.base.helper.BusiBaseDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.uboss.model.BpmTaskSiginType;
import org.activiti.uboss.task.ITaskSiginTypeEntity;

public class TaskSiginTypeEntity extends BusiBaseDAO implements
        ITaskSiginTypeEntity {

    public BpmTaskSiginType selectTaskSiginType(String taskTemplateId)
            throws BaseAppException {
        ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

        return taskListDAO.selectTaskSiginType(taskTemplateId);
    }

}
