package org.activiti.uboss.notification;

import java.io.Serializable;

public interface ProcessObserver extends Serializable {
	void invoke(Notification notification);
}
