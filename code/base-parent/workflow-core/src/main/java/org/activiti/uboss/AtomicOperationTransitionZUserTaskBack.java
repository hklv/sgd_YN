package org.activiti.uboss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.bpmn.behavior.GatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.history.handler.ActivityInstanceEndHandler;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskManager;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.uboss.behavior.ZNoneStartEventActivityBehavior;
import org.activiti.uboss.model.HolderChangeInfo;
import org.activiti.uboss.model.ZFlowDef;
import org.activiti.uboss.parser.ZClassDelegate;
import org.activiti.uboss.task.ITaskHolderEntity;

public class AtomicOperationTransitionZUserTaskBack implements AtomicOperation {
    private static final Logger LOG = Logger
            .getLogger(AtomicOperationTransitionZUserTaskBack.class.getName());
    private static final String DELETE_REASON_ROLLBACKED = "rollbacked";

    public void execute(InterpretableExecution execution) {
        LOG.fine("#### START UserTaskBack, executionId=" + execution.getId());
        // 取消当前UserTask
        cancelIt(execution);

//         backIt(execution);
        
        // 回退到上一个UserTask
        //backIt2(execution);
        backToPreTask(execution);
    }

    public boolean isAsync(InterpretableExecution execution){
    	return false;
    }
    
    private void cancelIt(InterpretableExecution execution) {
        // TODO ==cwz:more cancel : cancel the his execution
        String processInstanceId = execution.getProcessInstanceId();

        TaskService taskService = Context.getProcessEngineConfiguration()
                .getTaskService();

        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = taskQuery.processInstanceId(processInstanceId);
        taskQuery = taskQuery.executionId(execution.getId());
        List<Task> tasks = taskQuery.list();
        if (tasks == null || tasks.size() == 0) {
            throw new ActivitiException(
                    "Not a sequential user task for backing process "
                            + processInstanceId + ", executionId = "
                            + execution.getId());
        }
        if (tasks.size() > 1) {
            throw new ActivitiException("Too many task for backing process "
                    + processInstanceId + ", executionId = "
                    + execution.getId());
        }

        Task target = tasks.get(0);
        TaskManager taskManager = Context.getCommandContext().getTaskManager();
        TaskEntity task = taskManager.findTaskById(target.getId());
        LOG.fine("Cancel current task, try to delete task entity "
                + (task != null ? task : "null") + ", processInstanceId="
                + processInstanceId + ", executionId" + execution.getId());
        taskManager.deleteTask(task, DELETE_REASON_ROLLBACKED, false);
    }

    private void backToPreTask(InterpretableExecution execution){
    	
    	String processInstanceId = execution.getProcessInstanceId();
    	
    	 RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();
		 RepositoryService repositoryService = Context.getProcessEngineConfiguration().getRepositoryService();
		 // 获取流程定义
		 ProcessDefinitionEntity processDefinitionEntity = ZExtendsionUtil
		         .getProcessDefinitionByInstId(runtimeService,repositoryService, processInstanceId);
		 // 获取当前活动节点ID
		 List<String> activityIds = runtimeService.getActiveActivityIds(processInstanceId);
		 if (activityIds == null || activityIds.size() > 1) {
		     throw new ActivitiException("Only one active actvity can exist in backing process "
		                     + processInstanceId + ", executionId = " + execution.getId());
		 }
		 
		 ActivityImpl curAct = processDefinitionEntity.findActivity(activityIds.get(0));

        HistoryService historyService = Context.getProcessEngineConfiguration().getHistoryService();
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId);
        
        // 获取历史轨迹
        List<HistoricActivityInstance> hisQueryList = query.orderByHistoricActivityInstanceId().asc().list();

        // 去掉当前停留的节点
        if(hisQueryList.size() > 0){
            HistoricActivityInstance lastHisActivity = hisQueryList.get(hisQueryList.size() - 1);
            if (lastHisActivity.getEndTime() == null && lastHisActivity.getActivityId().equals(curAct.getId())) {
                hisQueryList.remove(hisQueryList.size() - 1);
            }
        }
        
        if (hisQueryList.size() == 0) {
        	
            // 没有回退的节点
        	throw new ActivitiException("can not find a active for process back "
		                     + processInstanceId + ", executionId = " + execution.getId());
        }
        
        //当前环节在流程定义上的所有的入口
        List<PvmTransition> incomings = curAct.getIncomingTransitions();
        List<ActivityImpl> preActs = new ArrayList<ActivityImpl>();
        for(PvmTransition incoming : incomings){
        	
        	ActivityImpl act = (ActivityImpl)incoming.getSource();
        	
        	//如果遇到网关，则继续往前找
        	preActs.addAll(findTaskIncomingAct(act));
        }
        
        //分拣出流程走过的入口
        List<ActivityImpl> traces = new ArrayList<ActivityImpl>();
        
        for(ActivityImpl preAct : preActs){
    		
        	for(HistoricActivityInstance hisQuery : hisQueryList){
            	
            	if(hisQuery.getActivityId().equals(preAct.getId())){
            		
            		traces.add(preAct);
            	}
            }
    	}
        
        //在act_hi_actinst中为activity设置结束标识
        markActivityEnd(execution);

        //主路径回退
        if(traces.size() == 1){
        	
        	if(traces.get(0).getActivityBehavior() instanceof ZNoneStartEventActivityBehavior){//撤单
        		
        		//撤单完成
            	withDrawProcess((InterpretableExecution)execution);
    			return;
        	}else{//回退
        		
        		execution.setActivity(traces.get(0));
                execution.setDirection("B");
                execution.setTransition((TransitionImpl)traces.get(0).getIncomingTransitions().get(0));
                execution.performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
        	}
        	
        	 
        }else{//并行路径(join)回退
        	
        	String gatewayId = null;
        	
            for(ActivityImpl trace : traces){
            	
            	ActivityImpl act = (ActivityImpl)trace.getIncomingTransitions().get(0).getSource();
            	
            	//通过网关fork的join
            	if(act.getActivityBehavior() instanceof GatewayActivityBehavior && !act.getId().equals(gatewayId)){
            		gatewayId = act.getId();
            		 execution.setActivity(act);
                     execution.setDirection("B");
                     execution.setTransition((TransitionImpl)act.getIncomingTransitions().get(0));
                     execution.performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
                     
            	}else if(!(act.getActivityBehavior() instanceof GatewayActivityBehavior)){//么有通过网关fork的join
            		
            		execution.setActivity(trace);
                    execution.setDirection("B");
                    execution.setTransition((TransitionImpl)trace.getIncomingTransitions().get(0));
                    execution.performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
            	}
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void backIt(InterpretableExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();

        RuntimeService runtimeService = Context.getProcessEngineConfiguration()
                .getRuntimeService();
        RepositoryService repositoryService = Context
                .getProcessEngineConfiguration().getRepositoryService();
        HistoryService historyService = Context.getProcessEngineConfiguration()
                .getHistoryService();
        // 查询历史活动列表，按id降序
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService
                .createHistoricActivityInstanceQuery();
        historicActivityInstanceQuery = historicActivityInstanceQuery
                .processInstanceId(execution.getProcessInstanceId());
        List<HistoricActivityInstance> historyList = historicActivityInstanceQuery
                .list();
        historyList = ZExtendsionUtil.orderHisList(historyList, false);

        ProcessDefinitionEntity processDefinitionEntity = ZExtendsionUtil
                .getProcessDefinitionByInstId(runtimeService,
                        repositoryService, processInstanceId);
        // 取的上一步的UserTask
        Map<String, Integer> canceledList = new HashMap<String, Integer>();
        HistoricActivityInstance target = null;
        ActivityImpl targetActivity = null;
        for (HistoricActivityInstance hisotry : historyList) {
            targetActivity = processDefinitionEntity.findActivity(hisotry
                    .getActivityId());
            ActivityBehavior behavior = targetActivity.getActivityBehavior();
            if (behavior instanceof UserTaskActivityBehavior) {
                Integer counter = canceledList.get(hisotry.getActivityId());
                if (hisotry.getEndTime() == null) {// 发现还未执行完的task
                    if (counter == null) {
                        canceledList.put(hisotry.getActivityId(), 1);
                    } else {
                        canceledList
                                .put(hisotry.getActivityId(), (counter + 1));
                    }
                    continue;
                }
                if (counter != null) {
                    if (counter > 0) {
                        canceledList
                                .put(hisotry.getActivityId(), (counter - 1));
                        continue;
                    }
                }
                target = hisotry;
                break;
            }
        }

        if (target == null) {
            throw new ActivitiException(
                    "Cannot find a valid user task for backing process "
                            + processInstanceId + ", executionId = "
                            + execution.getId());
        }

        Map<String, PvmTransition> targetTransitionList = new HashMap<String, PvmTransition>();
        List<PvmTransition> transitions = targetActivity
                .getIncomingTransitions();
        for (PvmTransition transition : transitions) {
            PvmActivity activity = transition.getSource();
            targetTransitionList.put(activity.getId(), transition);
        }

        PvmTransition targetTransition = null;
        int preIndex = historyList.indexOf(target) + 1;
        for (; preIndex < historyList.size(); preIndex++) {
            HistoricActivityInstance hisotry = historyList.get(preIndex);
            ActivityImpl hisotryActivity = processDefinitionEntity
                    .findActivity(hisotry.getActivityId());
            ActivityBehavior behavior = hisotryActivity.getActivityBehavior();
            if (behavior instanceof UserTaskActivityBehavior) {
                if (hisotry.getEndTime() == null) {
                    continue;
                }
                targetTransition = targetTransitionList.get(hisotry
                        .getActivityId());
                if (targetTransition != null) {
                    break;
                }
            } else {
                targetTransition = targetTransitionList.get(hisotry
                        .getActivityId());
                if (targetTransition != null) {
                    break;
                }
            }
        }
        if (targetTransition == null) {
            ActivityImpl hisotryActivity = processDefinitionEntity.getInitial();
            targetTransition = targetTransitionList
                    .get(hisotryActivity.getId());
        }

        // preActivity = processDefinitionEntity.getInitial();
        if (targetTransition == null) {
            throw new ActivitiException(
                    "Cannot find a valid target transition for backing process "
                            + processInstanceId + ", executionId = "
                            + execution.getId());
        }

        LOG.fine("Try to back usertask, target activity=" + targetActivity
                + ", target transition=" + targetTransition + ", executionId="
                + execution.getId());
        execution.setActivity(targetActivity);
        execution.setTransition((TransitionImpl) targetTransition);
        execution.performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
    }

    private void backIt2(InterpretableExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        Integer position = (Integer)execution.getVariableLocal(ZExtendsionUtil.VARIABLE_POSITION);
        if(position == null || position < 0){
            position = -1;
        }

        RuntimeService runtimeService = Context.getProcessEngineConfiguration()
                .getRuntimeService();
        RepositoryService repositoryService = Context
                .getProcessEngineConfiguration().getRepositoryService();
        // 获取流程定义
        ProcessDefinitionEntity processDefinitionEntity = ZExtendsionUtil
                .getProcessDefinitionByInstId(runtimeService,
                        repositoryService, processInstanceId);
        // 获取当前活动节点ID
        List<String> activityIds = runtimeService.getActiveActivityIds(processInstanceId);
        if (activityIds == null || activityIds.size() > 1) {
            throw new ActivitiException(
                    "Only one active actvity can exist in backing process "
                            + processInstanceId + ", executionId = "
                            + execution.getId());
        }
        ActivityImpl curAct = processDefinitionEntity.findActivity(activityIds
                .get(0));

        List<ActivityImpl> trace = findTrace(curAct, processInstanceId);
        ActivityImpl targetUserTask = null;
        PvmTransition targetTransition = null;
        if(position < 0){
            for (int i = 0; i < trace.size(); i++) {
                //if (trace.get(i).getActivityBehavior() instanceof UserTaskActivityBehavior) {
            	if (trace.get(i).getActivityBehavior() instanceof UserTaskActivityBehavior 
            			|| trace.get(i).getActivityBehavior() instanceof ZClassDelegate) {
                    targetUserTask = trace.get(i);
                    if (i + 1 < trace.size()) {
                        targetTransition = findTransition(trace.get(i + 1), targetUserTask);
                    }
                    break;
                }else if(trace.get(i).getActivityBehavior() instanceof ZNoneStartEventActivityBehavior){//开始节点
                	
                	//撤单完成
                	withDrawProcess(execution);
        			return;
                }
            }
        }else{
            for(int i=trace.size()-1, count=0; i>=0; i--){
                if (trace.get(i).getActivityBehavior() instanceof UserTaskActivityBehavior 
                		|| trace.get(i).getActivityBehavior() instanceof ZClassDelegate) {
                    if (count == position) {
                        targetUserTask = trace.get(i);
                        if (i + 1 < trace.size()) {
                            targetTransition = findTransition(trace.get(i + 1), targetUserTask);
                        }
                        break;
                    }else{
                        if(count > position){
                            break;
                        }else{
                            count++;
                        }
                    }
                }else if(trace.get(i).getActivityBehavior() instanceof ZNoneStartEventActivityBehavior){//开始节点
                	
                	//撤单完成
                	withDrawProcess(execution);
        			return;
                }
            }
        }
        if(targetUserTask == null){
            throw new ActivitiException(
                    "Cannot find a valid user task for backing process "
                            + processInstanceId + ", executionId = "
                            + execution.getId());
        }
        if (targetTransition == null) {
            throw new ActivitiException(
                    "Cannot find a valid target transition for backing process "
                            + processInstanceId + ", executionId = "
                            + execution.getId());
        }
        LOG.fine("Try to back usertask, target activity=" + targetUserTask
                + ", target transition=" + targetTransition + ", executionId="
                + execution.getId());
        
        //在act_hi_actinst中为activity设置结束标识
        markActivityEnd(execution);
        
        execution.setActivity(targetUserTask);
        execution.setDirection("B");
        execution.setTransition((TransitionImpl) targetTransition);
        execution.performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
    }
    
    private PvmTransition findTransition(ActivityImpl sourceActivity, ActivityImpl targetUserTask){
        List<PvmTransition> transitions = targetUserTask.getIncomingTransitions();
        for (PvmTransition transition : transitions) {
            if (transition.getSource().getId()
                    .equals(sourceActivity.getId())) {
                return transition;
            }
        }
        return null;
    }
    
    private List<ActivityImpl> findTaskIncomingAct(ActivityImpl act){
    	
    	List<ActivityImpl> acts = new ArrayList<ActivityImpl>();
    	
    	if(act.getActivityBehavior() instanceof GatewayActivityBehavior){
    		
    		List<PvmTransition> incomings = act.getIncomingTransitions();
    		
    		for(PvmTransition incoming : incomings){
    			
    			ActivityImpl act1 = (ActivityImpl)incoming.getSource();
    			if(act1.getActivityBehavior() instanceof GatewayActivityBehavior){
    				
    				acts.addAll(findTaskIncomingAct(act1));
    			}else{
    				acts.add(act1);
    			}
    		}
    	}else{
    		acts.add(act);
    	}
    	
    	return acts;
    }
    
    /**
     * 根据流程执行历史和流程定义来搜索轨迹
     * 
     * @param start
     * @param processInstanceId
     * @return
     */
    private List<ActivityImpl> findTrace(ActivityImpl start,
            String processInstanceId) {
        List<ActivityImpl> trace = new ArrayList<ActivityImpl>();

        HistoryService historyService = Context.getProcessEngineConfiguration()
                .getHistoryService();
        HistoricActivityInstanceQuery query = historyService
                .createHistoricActivityInstanceQuery().processInstanceId(
                        processInstanceId);
        // 获取历史轨迹
        List<HistoricActivityInstance> hisQueryList = query
                .orderByHistoricActivityInstanceId().asc().list();

        // 去掉当前停留的节点
        if(hisQueryList.size() > 0){
            HistoricActivityInstance lastHisActivity = hisQueryList.get(hisQueryList.size() - 1);
            if (lastHisActivity.getEndTime() == null
                    && lastHisActivity.getActivityId().equals(start.getId())) {
                hisQueryList.remove(hisQueryList.size() - 1);
            }
        }
        
        if (hisQueryList.size() == 0) {
            // 没有回退的节点
            return trace;
        }

        int endpos = hisQueryList.size() - 1;
        EXIT: while (endpos >= 0) {
            List<PvmTransition> incomings = start.getIncomingTransitions();
            if (incomings != null) {
                int index = -1;
                for (PvmTransition incoming : incomings) {
                    ActivityImpl source = (ActivityImpl) incoming.getSource();
                    if (source.getActivityBehavior() instanceof NoneStartEventActivityBehavior) {
                        // 已走到开始节点，子流程不允许跳出
                        trace.add(source);
                        break EXIT;
                    }
                    // 找离当前节点最近的
                    for (int i = endpos; i >= 0; i--) {
                        if (hisQueryList.get(i).getActivityId()
                                .equals(source.getId())) {
                            if (i > index) {
                                index = i;
                                start = source;
                                break;
                            }
                        }
                    }
                }
                trace.add(start);

                endpos = index;
            } else {
                throw new ActivitiException(
                        "Cannot find incomings for activity " + start
                                + ", processInstanceId=" + processInstanceId);
            }
        }
        return trace;
    }
    
    private void markActivityEnd(InterpretableExecution execution){
        //在act_hi_actinst中设置结束标识
        HistoricActivityInstanceEntity currentActivity = 
            ActivityInstanceEndHandler.findActivityInstance((ExecutionEntity)execution);
            
            if(currentActivity != null){
            	currentActivity.markEnded("back");
            }
        
    }
    
    private void withDrawProcess(InterpretableExecution execution){
    	
    	try{
    		//撤单完成
        	ITaskHolderEntity th = (ITaskHolderEntity) Class.forName("com.ztesoft.uboss.bpm.runtime.persistence.TaskHolderEntity").newInstance();
        	HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
    		holderChangeInfo.setAction("M");
    		holderChangeInfo.setHolderId((String)execution.getVariable(ZFlowDef.BPM_TASK_HOLDER_ID));
    		
    		th.changeHolder(holderChangeInfo);
    		execution.end();
    	}catch(Exception e){
    		
    		throw new ActivitiException("withDrawProcess has error", e);
    	}
    	
    }
}
