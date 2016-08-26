package org.activiti.uboss.behavior;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.uboss.model.BpmTaskAssginDto;
import org.activiti.uboss.model.BpmTaskSiginType;
import org.activiti.uboss.task.ITaskListEntity;
import org.activiti.uboss.task.ITaskSiginTypeEntity;

public abstract class ZMultiInstanceActivityBehavior extends MultiInstanceActivityBehavior{
	
	private String taskTemplateId = null;
	
	private List<BpmTaskAssginDto> users = null;
	
	private BpmTaskSiginType bpmTaskSiginType = null;
	
	public ZMultiInstanceActivityBehavior(ActivityImpl activity,
			AbstractBpmnActivityBehavior innerActivityBehavior,String taskTemplateId) {
		super(activity, innerActivityBehavior);
		this.taskTemplateId = taskTemplateId;
	}

	protected int resolveNrOfInstances(ActivityExecution execution){
		
		Object bpmTaskAssgin = execution.getVariableLocal("_sigin_designate_assgin");
		 
		if(bpmTaskAssgin == null)
		{
			BpmTaskAssginDto bpmTaskAssginDto = null;
			
			try
			{
				ITaskListEntity te = (ITaskListEntity) Class.forName(TASK_ENTITY_STR)
						.newInstance();
				
				bpmTaskAssginDto = te.getAssgin(taskTemplateId);
				
			}catch(Exception e)
			{
				throw new ActivitiException("创建会签任务失败", e);
			}
			
			if(bpmTaskAssginDto == null)
			{
				throw new ActivitiException("创建失败,未指定执行人.");
			}
			else
			{
				List<Long> userIds = bpmTaskAssginDto.getRoleUserIds();
				
				if(userIds == null || userIds.isEmpty())
				{
					throw new ActivitiException("创建失败,未指定执行人.");
				}
				else
				{
					List<BpmTaskAssginDto> users = new ArrayList<BpmTaskAssginDto>(userIds.size());
					
					for(Long userId:userIds)
					{
						BpmTaskAssginDto userDto = new BpmTaskAssginDto();
						userDto.setUserId(userId);
						users.add(userDto);
					}
					
					this.users = users;
					
				}

				return users.size();
			}
		}
		else
		{
			BpmTaskAssginDto bpmTaskAssginDto = (BpmTaskAssginDto)bpmTaskAssgin;
			List<Long> userIds = bpmTaskAssginDto.getRoleUserIds();
			
			if(userIds == null || userIds.isEmpty())
			{
				throw new ActivitiException("创建失败,未指定执行人.");
			}
			else
			{
				List<BpmTaskAssginDto> users = new ArrayList<BpmTaskAssginDto>(userIds.size());
				
				for(Long userId:userIds)
				{
					BpmTaskAssginDto userDto = new BpmTaskAssginDto();
					userDto.setUserId(userId);
					users.add(userDto);
				}
				
				this.users = users;
			}
			return users.size();
		}
		
	}
	
	protected boolean completionConditionSatisfied(ActivityExecution execution) {
		
		initBpmTaskSiginType();
		
		int nrOfInstances = getLoopVariable(execution, NUMBER_OF_INSTANCES);
	    int nrOfCompletedInstances = getLoopVariable(execution, NUMBER_OF_COMPLETED_INSTANCES);
		
	    String siginType = bpmTaskSiginType.getSiginType();
	    //全部通过
	    if("A".equals(siginType))
	    {
	    	if(nrOfInstances == nrOfCompletedInstances)
	    	{
	    		return true;
	    	}
	    }
	    //一票通过
	    else if("B".equals(siginType))
	    {
	    	if(nrOfCompletedInstances == 1)
	    	{
	    		return true;
	    	}
	    }
	    //百分比
	    else if("C".equals(siginType))
	    {
	    	if(nrOfInstances / nrOfCompletedInstances >= bpmTaskSiginType.getVoteCnt())
	    	{
	    		return true;
	    	}
	    }
	    //绝对值
	    else if("D".equals(siginType))
	    {
	    	if(nrOfCompletedInstances == bpmTaskSiginType.getVoteCnt())
	    	{
	    		return true;
	    	}
	    }
	    
		return false;
	}
	
	private void initBpmTaskSiginType()
	{
		if(bpmTaskSiginType == null)
		{
			try
			{
				ITaskSiginTypeEntity taskSiginTypeEntity = (ITaskSiginTypeEntity) Class
						.forName(
								"com.ztesoft.uboss.bpm.runtime.persistence.TaskSiginTypeEntity")
						.newInstance();
				
				bpmTaskSiginType = taskSiginTypeEntity.selectTaskSiginType(taskTemplateId);
			}
			catch(Exception e)
			{
				throw new ActivitiException("创建失败,初始化会签规则失败", e);
			}
		}
		
		if(bpmTaskSiginType == null)
		{
			throw new ActivitiException("创建失败,会签规则不存在taskTemplateId:" + taskTemplateId);
		}
	}

	public List<BpmTaskAssginDto> getUsers() {
		return users;
	}
}
