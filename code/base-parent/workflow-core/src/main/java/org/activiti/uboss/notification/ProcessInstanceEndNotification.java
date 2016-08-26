package org.activiti.uboss.notification;

import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstanceEndNotification extends ProcessInstanceNotification {
	private static final long serialVersionUID = 1L;

	ProcessInstanceEndNotification(ProcessInstance processInstance) {
		super(NotificationType.ProcessInstanceEnd, processInstance);
	}
}
