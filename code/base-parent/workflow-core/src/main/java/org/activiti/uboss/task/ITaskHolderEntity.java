package org.activiti.uboss.task;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.uboss.model.HolderChangeInfo;

public interface ITaskHolderEntity {
	ITaskHolderEntity createEntity(ActivityExecution execution) throws Exception;
	String getId();
	void close(Object holderId) throws Exception;
	void changeHolder(HolderChangeInfo changeInfo) throws BaseAppException;
}
