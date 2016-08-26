package org.activiti.uboss.notification;

import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstanceNotification extends Notification {
	private static final long serialVersionUID = 1L;

	ProcessInstanceNotification(NotificationType notificationType, ProcessInstance processInstance){
		super(notificationType);
		this.put("processInstance", processInstance);
		this.put("processInstanceId", processInstance.getId());
	}
}
