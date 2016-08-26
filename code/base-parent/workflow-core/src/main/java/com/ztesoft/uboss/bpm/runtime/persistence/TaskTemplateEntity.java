package com.ztesoft.uboss.bpm.runtime.persistence;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.setup.dao.IProcessTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.ProcessTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.model.BpmTaskEventDto;
import org.activiti.uboss.task.ITaskTemplateEntity;

import java.util.List;

public class TaskTemplateEntity implements ITaskTemplateEntity {

    @Override
    public List<BpmTaskEventDto> getTaskTemplateEvent(String templateId)
            throws Exception {

        IProcessTemplateDAO processTemplateDAO = SgdDaoFactory
                .getDaoImpl(ProcessTemplateDAOMySQL.class);
        return processTemplateDAO
                .selTaskTempLateEvent(Long.valueOf(templateId));
    }

    @Override
    public List<ServiceParamDto> getTaskTemplatePara(String deployId,
                                                     String templateId, String tacheId) throws Exception {

        ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);
        List<ServiceParamDto> params = taskHolderDAO.selectTacheVarByDeployId(deployId, Long.valueOf(templateId), tacheId);

        if (params == null || params.isEmpty()) {

            ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
            params = taskTemplateDAO.qrySerParamByTemplatId(Long.valueOf(templateId));
        }

        return params;
    }
}
