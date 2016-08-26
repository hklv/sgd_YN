package org.activiti.engine.impl.notification;

import java.util.HashMap;

public class Notification extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	private final NotificationType notificationType;
	
	public Notification(NotificationType notificationType){
		this.notificationType = notificationType;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}
}
