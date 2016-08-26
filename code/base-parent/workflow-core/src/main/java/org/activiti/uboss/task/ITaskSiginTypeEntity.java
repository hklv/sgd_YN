package org.activiti.uboss.task;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import org.activiti.uboss.model.BpmTaskSiginType;


public interface ITaskSiginTypeEntity {
    BpmTaskSiginType selectTaskSiginType(String taskTemplateId) throws BaseAppException;


}
