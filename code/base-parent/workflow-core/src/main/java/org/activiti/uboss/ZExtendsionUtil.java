package org.activiti.uboss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ScopeImpl;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.model.BpmTaskEventDto;
import org.activiti.uboss.model.ZProperty;
import org.activiti.uboss.task.ITaskTemplateEntity;

public class ZExtendsionUtil {
	public final static String ExtendsionKey_Z = "Z";

	public final static String BPMN_20_N_ResourceRef = "resourceRef";
	public final static String BPMN_20_N_FormalExpression_ZExpr = "ZExpr";
	public final static String BPMN_20_N_FormalExpression_ZService = "ZService";
	public final static String BPMN_20_N_FormalExpression_ZRule = "ZRule";
	public final static String BPMN_20_N_ExtensionElements = "extensionElements";

	public static final String BPMN_20_ZEXTENDSION_ELE_SERVICE = "serviceDefinition";
	public static final String BPMN_20_ZEXTENDSION_ELE_TASK = "taskDefinition";
	public static final String BPMN_20_ZEXTENDSION_ATTR_TASKTEMPLATEID = "taskTemplateId";
	public static final String BPMN_20_ZEXTENDSION_ATTR_WAIT_SIGNAL = "waitForSignal";

	public static final String BPMN_20_ZEXTENDSION_SCHEMA_LOCATION = "org/activiti/impl/bpmn/parser/uboss-bpmn-extensions.xsd";
	public static final String BPMN_20_ZEXTENDSION_NS = "http://www.ztesoft.com/uboss/bpmn";

	public static final String PROPERTYNAME_ZPROPERTY_DECLARATIONS = "zPropertyDeclarations";
	public static final String PROPERTYNAME_ZACTIVITY_INFO = "zActivityInfo";
	public static final String PROPERTYNAME_ZACTIVITY_CODE = "zActivityCode";

	public static final String VARIABLE_EXCEPTION_INFO = "zExceptionInfo";
	public static final String VARIABLE_POSITION = "zPosition";

	public static final String SERVICE_TASK_STATE_EXCEPTION = "__z_exception__";
	public static final String SERVICE_TASK_STATE_WAIT_FOR_SIGNAL = "__z_wait_for_signal_";
	public static final String SERVICE_TASK_STATE_COMPLETED = "__z_complete__";

	public static final AtomicOperation TRANSITION_USER_TASK_BACK = new AtomicOperationTransitionZUserTaskBack();
	
	public static final String TASK_TEMPLATE_ENTITY_STR = "com.ztesoft.uboss.bpm.runtime.persistence.TaskTemplateEntity";

	public static List<ZProperty> getPropertyDeclarations(ScopeImpl scope) {
		@SuppressWarnings("unchecked")
		List<ZProperty> propertyDeclarations = (List<ZProperty>) scope
				.getProperty(ZExtendsionUtil.PROPERTYNAME_ZPROPERTY_DECLARATIONS);
		return propertyDeclarations;
	}

	public static ZProperty getPropertyDeclaration(ScopeImpl scope,
			String propertyId) {
		List<ZProperty> propertyList = getPropertyDeclarations(scope);
		if (propertyList != null) {
			for (ZProperty property : propertyList) {
				if (property.getId().equals(propertyId)) {
					return property;
				}
			}
		}
		return null;
	}

	public static void setActivityInfo(ActivityImpl activity,
			Object activityInfo) {
		Object old = activity.getProperty(PROPERTYNAME_ZACTIVITY_INFO);
		if (old != null) {
			throw new ActivitiException("acivity info has already been set : "
					+ old + ", new acivity info : " + activityInfo);
		}
		activity.setProperty(PROPERTYNAME_ZACTIVITY_INFO, activityInfo);
	}

	public static Object getActivityInfo(ActivityImpl activity) {
		return activity.getProperty(PROPERTYNAME_ZACTIVITY_INFO);
	}

	public static String getActivityType(ActivityImpl activity) {
		return (String) activity.getProperty("type");
	}

	public static String getActivityCode(ActivityImpl activity) {
		return (String) activity.getProperty(PROPERTYNAME_ZACTIVITY_CODE);
	}
	
	public static List<BpmTaskEventDto> qryTaskTemplateEvent(String taskTemplateId) {

		try {
			ITaskTemplateEntity te = (ITaskTemplateEntity) Class.forName(TASK_TEMPLATE_ENTITY_STR)
					.newInstance();
			
			return te.getTaskTemplateEvent(taskTemplateId);
			
		} catch (Exception e) {
			
			throw new ActivitiException("qryTaskTemplateEvent error", e);
		}
	}
	
	public static List<ServiceParamDto> qryTaskTemplateParam(String deployId, String taskTemplateId, String tacheId) {

		try {
			ITaskTemplateEntity te = (ITaskTemplateEntity) Class.forName(TASK_TEMPLATE_ENTITY_STR)
					.newInstance();
			
			return te.getTaskTemplatePara(deployId, taskTemplateId, tacheId);
			
		} catch (Exception e) {
			
			throw new ActivitiException("qryTaskTemplateParam error", e);
		}
	}
 

	public static ProcessDefinitionEntity getProcessDefinitionByInstId(
			RuntimeService runtimeService, RepositoryService repository,
			String processInstanceId) {
		ProcessInstanceQuery processInstanceQuery = runtimeService
				.createProcessInstanceQuery();
		processInstanceQuery = processInstanceQuery
				.processInstanceId(processInstanceId);
		ExecutionEntity processExecution = (ExecutionEntity) processInstanceQuery
				.singleResult();

		return ZExtendsionUtil.getProcessDefinition(repository,
				processExecution.getProcessDefinitionId());
	}

	public static ProcessDefinitionEntity getProcessDefinition(
			String processDefinitionId) {
		RepositoryService repositoryService = Context
				.getProcessEngineConfiguration().getRepositoryService();
		return getProcessDefinition(repositoryService, processDefinitionId);
	}

	public static ProcessDefinitionEntity getProcessDefinition(
			RepositoryService repository, String processDefinitionId) {
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repository;
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryServiceImpl
				.getDeployedProcessDefinition(processDefinitionId);
		return processDefinitionEntity;
	}

	public static Execution assertSingleExecution(String processInstanceId,
			String executionId) {
		return assertSingleExecution(Context.getProcessEngineConfiguration()
				.getRuntimeService(), processInstanceId, executionId);
	}

	public static Execution assertSingleExecution(
			RuntimeService runtimeService, String processInstanceId,
			String executionId) {
		ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
		if (processInstanceId != null) {
			executionQuery = executionQuery
					.processInstanceId(processInstanceId);
		}
		if (executionId != null) {
			executionQuery = executionQuery.executionId(executionId);
		}
		List<Execution> executionList = executionQuery.list();
		if (executionList == null || executionList.size() == 0) {
			throw new ActivitiException("Cannot find execution for process "
					+ processInstanceId + ", executionId = " + executionId);
		}
		if (executionList.size() > 1) {
			throw new ActivitiException("to many execution for process "
					+ processInstanceId + ", executionId = " + executionId);
		}
		return executionList.get(0);
	}

	public static ActivityImpl getActivity(RepositoryService repository,
			String processDefinitionId, String activityId) {
		ProcessDefinitionEntity processDefinitionEntity = getProcessDefinition(
				repository, processDefinitionId);
		ActivityImpl activityImpl = processDefinitionEntity
				.findActivity(activityId);
		return activityImpl;
	}

	public static List<HistoricActivityInstance> orderHisList(
			List<HistoricActivityInstance> list, final boolean asc) {
		List<HistoricActivityInstance> result = new ArrayList<HistoricActivityInstance>();
		result.addAll(list);
		Comparator<HistoricActivityInstance> c = new Comparator<HistoricActivityInstance>() {
			public int compare(HistoricActivityInstance o1,
					HistoricActivityInstance o2) {
				if (asc) {
					return o1.getId().compareTo(o2.getId());
				} else {
					return o2.getId().compareTo(o1.getId());
				}
			}
		};
		Collections.sort(result, c);
		return result;
	}

	public static List<ExecutionEntity> filterEeList(List<Execution> list,
			final boolean asc) {
		List<ExecutionEntity> result = new ArrayList<ExecutionEntity>();
		for (Execution execution : list) {
			if (execution instanceof ExecutionEntity) {
				result.add((ExecutionEntity) execution);
			}
		}
		Comparator<ExecutionEntity> c = new Comparator<ExecutionEntity>() {
			public int compare(ExecutionEntity o1, ExecutionEntity o2) {
				if (asc) {
					if (o1.getRevision() == o2.getRevision()) {
						return o1.getId().compareTo(o2.getId());
					}
					return (o1.getRevision() - o2.getRevision());
				} else {
					if (o1.getRevision() == o2.getRevision()) {
						return o2.getId().compareTo(o1.getId());
					}
					return (o2.getRevision() - o1.getRevision());
				}
			}
		};
		Collections.sort(result, c);
		return result;
	}

	public static void userTaskBack(RuntimeService runtimeService,
			String processInstanceId, String executionId, Integer position) {
		userTaskBack(runtimeService, processInstanceId, executionId, position,
				null);
	}

	public static void userTaskBack(RuntimeService runtimeService,
			String processInstanceId, String executionId, Integer position,
			Map<String, Object> variables) {
		((ZRuntimeServiceImpl) runtimeService).userTaskBack(processInstanceId,
				executionId, position, variables);
	}
}