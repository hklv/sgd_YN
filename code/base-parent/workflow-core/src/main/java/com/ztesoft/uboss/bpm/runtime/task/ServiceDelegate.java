package com.ztesoft.uboss.bpm.runtime.task;

import com.ztesoft.sgd.base.factory.SgdDaoFactory;
import com.ztesoft.uboss.bpm.setup.dao.ITaskTemplateDAO;
import com.ztesoft.uboss.bpm.setup.dao.mysqlimpl.TaskTemplateDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskHolderDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.ITaskListDAO;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskHolderDAOMySQL;
import com.ztesoft.uboss.bpm.taskcenter.dao.mysqlimpl.TaskListDAOMySQL;
import com.ztesoft.uboss.bpm.utils.DateUtil;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.service.ServiceFlow;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.ext.CallableDefinition;
import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.ext.ZServiceDefinition;
import org.activiti.uboss.model.BpmServiceTaskDto;

import java.util.List;

public class ServiceDelegate extends ServiceCaller implements JavaDelegate {
    private ZSmartLogger logger = ZSmartLogger.getLogger(getClass());

    @Override
    public void setCallableDefinition(CallableDefinition callableDefinition) {
        if (callableDefinition instanceof ZServiceDefinition) {
            super.setCallableDefinition(callableDefinition);
        } else {
            throw new ActivitiException("callableDefinition must be a "
                    + ZServiceDefinition.class);
        }
    }

    public ZServiceDefinition getServiceDefinition() {
        return (ZServiceDefinition) super.getCallableDefinition();
    }

    public void execute(DelegateExecution execution) throws Exception {

		/*
         * serviceName用于确定唯一的服务，即BO_METHOD.ID
		 */
        String taskTemplateId = this.getServiceDefinition().getTaskTemplateId();

        logger.debug("BEGIN: call service \"" + taskTemplateId + "\"");

        try {

            String serviceName = null;
            BpmServiceTaskDto bpmServiceTaskDto = null;
            ITaskListDAO taskListDAO = SgdDaoFactory.getDaoImpl(TaskListDAOMySQL.class);

            // 反向补偿任务
            if ("B".equals(((ActivityExecution) execution).getDirection())) {
                bpmServiceTaskDto = taskListDAO
                        .getBackServiceName(taskTemplateId);
                if (bpmServiceTaskDto != null) {
                    serviceName = bpmServiceTaskDto.getServiceName();
                }

            } else {// 正常服务任务
                bpmServiceTaskDto = taskListDAO.getServiceName(taskTemplateId);
                if (bpmServiceTaskDto != null) {
                    serviceName = bpmServiceTaskDto.getServiceName();
                }
            }

            callTacheService(serviceName, execution);

        } catch (ActivitiException ex) {
            throw ex;
        } catch (Exception ex) {
            String msg = "Fail to call service,serviceName=" + taskTemplateId
                    + ",processInstanceId=" + execution.getProcessInstanceId();
            logger.error(msg, ex);
            throw ex;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("END: call service \"" + taskTemplateId + "\"");
        }

    }

    private void callTacheService(String serviceName,
                                  DelegateExecution execution) throws BaseAppException {
        if (isValidService(serviceName)) {

            DynamicDict dict = new DynamicDict();

            // 获取流程定义时设置的服务参数
            String deployId = ((ActivityExecution) execution).getActivity()
                    .getProcessDefinition().getDeploymentId();
            String templdateID = (String) ((ActivityExecution) execution)
                    .getActivity().getProperty("taskTemplateId");

            if (templdateID != null) {

                ITaskHolderDAO taskHolderDAO = SgdDaoFactory.getDaoImpl(TaskHolderDAOMySQL.class);

                //自定义图元模式查实例化参数
                List<ServiceParamDto> serviceParamList = taskHolderDAO
                        .selectTacheVarByDeployId(deployId,
                                Long.valueOf(templdateID), ((ActivityExecution) execution).getActivity().getId());

                //普通模式查配置参数
                if (serviceParamList == null || serviceParamList.size() == 0) {
                    ITaskTemplateDAO taskTemplateDAO = SgdDaoFactory.getDaoImpl(TaskTemplateDAOMySQL.class);
                    serviceParamList = taskTemplateDAO.qrySerParamByTemplatId(Long.valueOf(templdateID));
                }

                if (serviceParamList != null) {
                    for (ServiceParamDto param : serviceParamList) {

                        String varType = param.getVarType();
                        String defaultValue = param.getVarValue();
                        if (StringUtil.isEmpty(defaultValue)) {
                            continue;
                        }
                        if ("String".equals(varType)) {
                            dict.set(param.getVarCode(), defaultValue);
                        } else if ("Long".equals(varType)) {
                            dict.set(param.getVarCode(),
                                    Long.valueOf(defaultValue));
                        } else if ("Date".equals(varType)) {
                            String[] dateValue = defaultValue.split(" ");
                            if (dateValue != null && dateValue.length == 1) {
                                defaultValue += " 00:00:00";
                            }
                            dict.set(param.getVarCode(), DateUtil
                                    .string2SQLDate(defaultValue,
                                            DateUtil.DATETIME_FORMAT_1));
                        }

                        // 设置流程变量
                        if ("1".equals(param.getScope())) {
                            Object obj = dict.get(param.getVarCode());
                            if (obj != null) {
                                execution.getVariables().put(
                                        param.getVarCode(), obj);
                            }
                        }
                    }
                }
            }

            String[] services = serviceName.split("\\.");

            dict.serviceName = services[0];
            dict.set("method", services[1]);
            dict.set("FLOW_VAR", execution.getVariables());
            dict.set("zsmart_session",execution.getVariables().get("zsmart_session"));
            ServiceFlow.callService(dict);

        } else {
            logger.error("service not found!!taskTemplateId");
        }

    }

    private boolean isValidService(String serviceName) {
        return StringUtil.isNotEmpty(serviceName)
                && serviceName.indexOf(".") != -1;
    }
}
