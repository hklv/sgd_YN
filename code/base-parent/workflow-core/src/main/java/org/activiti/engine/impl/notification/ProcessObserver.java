package org.activiti.engine.impl.notification;

import java.io.Serializable;

public interface ProcessObserver extends Serializable {
	void invoke(Notification notification);
}
