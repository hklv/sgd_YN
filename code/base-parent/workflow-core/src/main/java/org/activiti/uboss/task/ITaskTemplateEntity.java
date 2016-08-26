package org.activiti.uboss.task;

import java.util.List;

import org.activiti.uboss.ext.ServiceParamDto;
import org.activiti.uboss.model.BpmTaskEventDto;

public interface ITaskTemplateEntity {
	
	List<BpmTaskEventDto>  getTaskTemplateEvent(String templateId) throws Exception;
	
	List<ServiceParamDto>  getTaskTemplatePara(String deployId, String templateId, String tacheId) throws Exception;
}
