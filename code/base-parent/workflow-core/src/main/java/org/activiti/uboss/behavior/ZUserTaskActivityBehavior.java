package org.activiti.uboss.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.bpmn.behavior.GatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.uboss.model.BpmTaskAssginDto;
import org.activiti.uboss.model.HolderChangeInfo;
import org.activiti.uboss.model.ZFlowDef;
import org.activiti.uboss.task.ITaskHolderEntity;
import org.activiti.uboss.task.ITaskListEntity;

public class ZUserTaskActivityBehavior extends UserTaskActivityBehavior {

	private String taskTemplateId = null;

	public ZUserTaskActivityBehavior(String taskTemplateId,
			ExpressionManager expressionManager, TaskDefinition taskDefinition) {
		super(expressionManager, taskDefinition);
		this.taskTemplateId = taskTemplateId;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doUserTask(TaskEntity task, ActivityExecution execution)
			throws Exception {

		ITaskListEntity te = (ITaskListEntity) Class.forName(TASK_ENTITY_STR)
				.newInstance();

		// 获取上个环节/会签的指派
		Object bpmTaskAssginDto = execution
				.getVariableLocal("_designate_assgin");

		if (bpmTaskAssginDto == null) {
			bpmTaskAssginDto = execution.getVariable("_designate_assgin");
			execution.removeVariable("_designate_assgin");
		} else {
			execution.removeVariableLocal("_designate_assgin");
		}

		// liyb add, 使用简单类型，便于在脚本规则中控制
		if (bpmTaskAssginDto == null) {
			BpmTaskAssginDto assginDto = null;
			Object uid = execution.getVariable("bpm_user_id");
			Object rid = execution.getVariable("bpm_role_id");
			Object jid = execution.getVariable("bpm_job_id");
			Object oid = execution.getVariable("bpm_org_id");

			bpmTaskAssginDto = new BpmTaskAssginDto();
			if (uid != null) {
				assginDto = new BpmTaskAssginDto();
				assginDto.setUserId(new Long(uid.toString()));
				execution.removeVariable("bpm_user_id");
			} else if (rid != null) {
				assginDto = new BpmTaskAssginDto();
				assginDto.setRoleId(new Long(rid.toString()));
				execution.removeVariable("bpm_role_id");
			} else if (jid != null) {
				assginDto = new BpmTaskAssginDto();
				assginDto.setJobId(new Long(jid.toString()));
				execution.removeVariable("bpm_job_id");
			} else if (oid != null) {
				assginDto = new BpmTaskAssginDto();
				assginDto.setOrgId(new Long(oid.toString()));
				execution.removeVariable("bpm_org_id");
			}
			bpmTaskAssginDto = assginDto;
		}
		
		if (bpmTaskAssginDto == null) {
			// 会签
			Object uids = execution.getVariable("bpm_user_list");
			if (uids != null && uids instanceof List) {
				List ulist = new ArrayList();
				for (Object o : (List) uids) {
					BpmTaskAssginDto assginDto = new BpmTaskAssginDto();
					assginDto.setUserId(new Long(o.toString()));
					ulist.add(assginDto);
				}
				bpmTaskAssginDto = ulist;
				execution.removeVariable("bpm_user_list");
			}
		}
		
		
		// 指派给一个人
		if (bpmTaskAssginDto != null
				&& bpmTaskAssginDto instanceof BpmTaskAssginDto) {
			te.createUserTask(taskTemplateId, task, execution,
					(BpmTaskAssginDto) bpmTaskAssginDto);
		}
		// 指派给多个人
		else if (bpmTaskAssginDto instanceof List) {
			te.createMultiUserTask(taskTemplateId, task, execution,
					(List) bpmTaskAssginDto);
		}
		// 没有指派人
		else {

			te.createUserTask(taskTemplateId, task, execution);
		}

		System.out.println("UserTask doUserTask");
	}
	
	@SuppressWarnings({ "unchecked"})
	public void signal(ActivityExecution execution, String signalName,
			Object signalData) throws Exception {
		
		//反向工单
		if("back".equals(signalName)){
			
			if(signalData != null){
				
				Map<String, Object> taskVariables = (Map<String, Object>)signalData;
				
				if("Last".equals((String)taskVariables.get("directionAct"))){
					
					//修改流程状态并记录日志
					ITaskHolderEntity th = (ITaskHolderEntity) Class.forName(TASK_HOLDER_ENTITY_STR).newInstance();
			    	HolderChangeInfo holderChangeInfo = new HolderChangeInfo();
					holderChangeInfo.setAction("N");
					holderChangeInfo.setHolderId((String)execution.getVariable(ZFlowDef.BPM_TASK_HOLDER_ID));
					th.changeHolder(holderChangeInfo);
					
					leave(execution);
				}else{
					
					// Join
				    PvmActivity activity = execution.getActivity();

				    execution.inactivate();
				    boolean taken = true;
				    
				    if(execution.isConcurrent()){
				    	
				    	List<? extends ActivityExecution> executions = execution.getParent().getExecutions();
					    
					    for(ActivityExecution e : executions){
					    	
					    	if(e.isActive()){
					    		
					    		taken = false;
					    	}
					    }
				    }
				    
				    if(taken){
				    	
				    	//当前环节在流程定义上的所有的入口
				        List<PvmTransition> incomings = activity.getIncomingTransitions();
				        List<ActivityImpl> preActs = new ArrayList<ActivityImpl>();
				        for(PvmTransition incoming : incomings){
				        	
				        	ActivityImpl act = (ActivityImpl)incoming.getSource();
				        	
				        	//如果遇到网关，则继续往前找
				        	preActs.addAll(findTaskIncomingAct(act));
				        }
				        
				        HistoryService historyService = Context.getProcessEngineConfiguration().getHistoryService();
				        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().processInstanceId(execution.getProcessInstanceId());
				        
				        // 获取历史轨迹
				        List<HistoricActivityInstance> hisQueryList = query.orderByHistoricActivityInstanceId().asc().list();
				        
				        //分拣出流程走过的入口
				        List<ActivityImpl> traces = new ArrayList<ActivityImpl>();
				        
				        for(ActivityImpl preAct : preActs){
				    		
				        	for(HistoricActivityInstance hisQuery : hisQueryList){
				            	
				            	if(hisQuery.getActivityId().equals(preAct.getId())){
				            		
				            		traces.add(preAct);
				            	}
				            }
				    	}
				      
				      //主路径回退
				        if(traces.size() == 1){
				        	
				        	if(traces.get(0).getActivityBehavior() instanceof ZNoneStartEventActivityBehavior){//撤单
			                	
			                	//撤单完成
			                	withDrawProcess((InterpretableExecution)execution);
			        			return;
			                }else{//回退
			                	
			                	((InterpretableExecution)execution).setActivity(traces.get(0));
					        	 ((InterpretableExecution)execution).setDirection("B");
					        	 ((InterpretableExecution)execution).setTransition((TransitionImpl)traces.get(0).getIncomingTransitions().get(0));
					        	 ((InterpretableExecution)execution).performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
			                }
				        	
				        	 
				        }else{//并行路径(join)回退
				        	
				        	String gatewayId = null;
				        	
				            for(ActivityImpl trace : traces){
				            	
				            	ActivityImpl act = (ActivityImpl)trace.getIncomingTransitions().get(0).getSource();
				            	
				            	//通过网关fork的join
				            	if(act.getActivityBehavior() instanceof GatewayActivityBehavior && !act.getId().equals(gatewayId)){
				            		gatewayId = act.getId();
				            		((InterpretableExecution)execution).setActivity(act);
				            		((InterpretableExecution)execution).setDirection("B");
				                     ((InterpretableExecution)execution).setTransition((TransitionImpl)act.getIncomingTransitions().get(0));
				                     ((InterpretableExecution)execution).performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
				                     
				            	}else if(!(act.getActivityBehavior() instanceof GatewayActivityBehavior)){//么有通过网关fork的join
				            		
				            		((InterpretableExecution)execution).setActivity(trace);
				            		((InterpretableExecution)execution).setDirection("B");
				            		((InterpretableExecution)execution).setTransition((TransitionImpl)trace.getIncomingTransitions().get(0));
				            		((InterpretableExecution)execution).performOperation(AtomicOperation.TRANSITION_CREATE_SCOPE);
				            	}
				            }
				        }
				    }
				   
				}
			}
			
		}else{
			
			leave(execution);
		}
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
