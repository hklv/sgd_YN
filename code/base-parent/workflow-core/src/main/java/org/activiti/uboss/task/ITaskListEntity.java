package org.activiti.uboss.task;

import java.util.List;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.model.BpmTaskAssginDto;


public interface ITaskListEntity {
	
	void createSysTask(ActivityExecution execution, String taskTemplateId, boolean isBlocked)
			throws Exception;
	
	void createSysExTask(ActivityExecution execution, String taskTemplateId, String exceptionMsg)
			throws Exception;

	void createUserTask(String taskTemplateId, TaskEntity task,
						ActivityExecution execution) throws Exception;
	
	void createMultiUserTask(String taskTemplateId, TaskEntity task,
							 ActivityExecution execution, List<BpmTaskAssginDto> designates) throws Exception;
	
	void updateSysTaskState(String exceptionTaskListId, String state) throws Exception;
	
	void compSignalSysTask(String taskListId, Long userId) throws Exception;
	
	BpmTaskAssginDto getAssgin(String taskTemplateId) throws Exception;
	
	void createUserTask(String taskTemplateId, TaskEntity task,
						ActivityExecution execution, BpmTaskAssginDto designate) throws BaseAppException;
}
